package com.sumslack.compile.dyna;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.sumslack.compile.dyna.domain.Task;
import com.sumslack.compile.dyna.java.JarService;
import com.sumslack.compile.dyna.java.JavaRunner;
import com.sumslack.compile.dyna.schedule.RunTaskJob;
import com.sumslack.compile.dyna.schedule.ThreadManager;

public class Bootstrap {
	public static void main(String[] args) throws Exception{
		
		String home = StaticValue.HOME;
		File jcoderHome = new File(home);
		String logPath = getOrCreateEnv(StaticValue.PREFIX + "log", "log/sumslack.log");
		makeFiles(jcoderHome, logPath);
		
		// 初始化Jar环境
		JarService.init();
		
		//举个例子，加载第三方依赖包
		String deps = "<dependency>\r\n" + 
				"	  <groupId>com.squareup.okhttp3</groupId>\r\n" + 
				"	  <artifactId>benchmarks</artifactId>\r\n" + 
				"	  <version>3.12.0</version>\r\n" + 
				"	</dependency>";
		JarService.savePom(mavenDeps(deps));
		
		ThreadManager.stopScheduler();
		ThreadManager.startScheduler();
		new Thread(new RunTaskJob()).start();
		String code = "package com.sumslack.compile.java.test;\r\n" + 
				"\r\n" + 
				"import com.sumslack.compile.dyna.java.anno.DefaultExecute;\r\n" + 
				"import java.util.Date;\r\n" + 
				"\r\n" + 
				"public class MyTestRun {\r\n" + 
				"\r\n" + 
				"	@DefaultExecute\r\n" + 
				"	public void defaultTest() {\r\n" + 
				"		System.out.println(new Date() + \" ===> test job\");\r\n" + 
				"	}\r\n" + 
				"}";
		Task task = new Task();
		task.setId(1L);
		task.setName("testTask");
		task.setStatus(1);
		task.setCode(code);
		task.setScheduleStr("*/4 * * * * ?");
		if(new JavaRunner(task).check()) {
			new JavaRunner(task).compile();
			RunTaskJob.tasks.put(task.getName(), task);
			ThreadManager.add(task);
		}
	}
	
	private static String mavenDeps(String content) {
		return "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
				+ "	xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n"
				+ "	<modelVersion>4.0.0</modelVersion>\n" + "	<groupId>org.nlpcn</groupId>\n" + "	<artifactId>jcoder</artifactId>\n" + "	<version>0.1</version>\n"
				+ "	\n" + "	<dependencies>\n" + content + "	</dependencies>\n" + "\n" + "	<build>\n" + "		<sourceDirectory>src/main/java</sourceDirectory>\n"
				+ "		<testSourceDirectory>src/test/java</testSourceDirectory>\n" + "		\n" + "		<plugins>\n" + "			<plugin>\n"
				+ "				<artifactId>maven-compiler-plugin</artifactId>\n" + "				<version>3.3</version>\n" + "				<configuration>\n"
				+ "					<source>1.8</source>\n" + "					<target>1.8</target>\n" + "					<encoding>UTF-8</encoding>\n"
				+ "					<compilerArguments>\n" + "						<extdirs>lib</extdirs>\n" + "					</compilerArguments>\n"
				+ "				</configuration>\n" + "			</plugin>\n" + "		</plugins>\n" + "	</build>\n" + "</project>\n" + "";
	}
	
	private static void makeFiles(File JcoderHome, String logPath) throws FileNotFoundException, IOException {
		File libDir = new File(JcoderHome, "lib"); // create jar dir
		if (!libDir.exists()) {
			libDir.mkdirs();
			

			
			wirteFile(new File(libDir, "pom.xml").getAbsolutePath(), "utf-8",
					mavenDeps(""));
		}

		File tmpDir = new File(JcoderHome, "tmp"); // create tmp dir
		if (!tmpDir.exists()) {
			tmpDir.mkdirs();
		}

		File pluginDir = new File(JcoderHome, "web"); // create web dir
		if (!pluginDir.exists()) {
			pluginDir.mkdirs();
		}

		File resourceDir = new File(JcoderHome, "resource"); // create resource dir
		if (!resourceDir.exists()) {
			resourceDir.mkdirs();
		}

		String uploadPath = System.getProperty(StaticValue.PREFIX + "upload"); //create upload file

		if (uploadPath == null) {
			uploadPath = new File(JcoderHome, "upload").getAbsolutePath();
			putEnv(StaticValue.PREFIX + "upload", uploadPath);
		}

		File upload = new File(uploadPath);

		if (!upload.exists()) {
			upload.mkdirs();
		}


		createLog4j2Config(new File(resourceDir, "log4j2.xml"), logPath);
	}
	
	private static String getOrCreateEnv(String key, String def) {

		if (!key.startsWith(StaticValue.PREFIX)) {
			key = StaticValue.PREFIX + key;
		}

		String value = System.getProperty(key);

		if (value == null) {
			value = System.getenv(key);
		}

		if (value == null) {
			putEnv(key, def);
			value = def;
		}

		return value;
	}
	private static void putEnv(String key, String value) {
		if (!key.startsWith(StaticValue.PREFIX)) {
			key = StaticValue.PREFIX + key;
		}
		if (value != null) {
			System.setProperty(key, value);
		}
	}
	private static void wirteFile(String filePath, String encoding, String content) throws FileNotFoundException, IOException {
		try (FileOutputStream fos = new FileOutputStream(new File(filePath))) {
			fos.write(content.getBytes(encoding));
			fos.flush();
		}
	}
	/**
	 * config log4j2 setting
	 * 
	 * @param logPath
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private static void createLog4j2Config(File log4jFile, String logPath) throws FileNotFoundException, IOException {

		if (log4jFile.exists()) {
			return;
		}

		String logTemplate = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"<Configuration status=\"INFO\">\n" + 
				"	<properties>\n" + 
				"		<property name=\"LOG_PATH\">{{LOG_PATH}}</property>\n" + 
				"	</properties>\n" + 
				"	<Appenders>\n" + 
				"		<Console name=\"Console\" target=\"SYSTEM_OUT\">\n" + 
				"			<PatternLayout pattern=\"%c-%-4r %-5p [%d{yyyy-MM-dd HH:mm:ss}]  %m%n\" />\n" + 
				"		</Console>\n" + 
				"\n" + 
				"		<RollingRandomAccessFile name=\"File\" fileName=\"${LOG_PATH}\"\n" + 
				"			filePattern=\"${LOG_PATH}-%d{yyyyMMdd}\">\n" + 
				"			<PatternLayout pattern=\"%m%n\" />\n" + 
				"			<Policies>\n" + 
				"				<TimeBasedTriggeringPolicy interval=\"1\"\n" + 
				"					modulate=\"true\" />\n" + 
				"			</Policies>\n" + 
				"		</RollingRandomAccessFile>\n" + 
				"\n" + 
				"	</Appenders>\n" + 
				"\n" + 
				"\n" + 
				"	<Loggers>\n" + 
				"		<Root level=\"info\">\n" + 
				"			<AppenderRef ref=\"Console\" />\n" + 
				"			<AppenderRef ref=\"File\" />\n" + 
				"		</Root>\n" + 
				"	</Loggers>\n" + 
				"</Configuration>";

		wirteFile(log4jFile.getAbsolutePath(), "utf-8", logTemplate.replace("{{LOG_PATH}}", logPath));

	}
}
