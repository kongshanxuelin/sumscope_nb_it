package com.sumslack.compile.dyna.java;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.sumslack.compile.dyna.domain.CodeInfo;
import com.sumslack.compile.dyna.domain.Task;
import com.sumslack.compile.dyna.exception.CodeRuntimeException;
import com.sumslack.compile.dyna.java.anno.DefaultExecute;
import com.sumslack.compile.dyna.java.anno.Execute;
import com.sumslack.compile.dyna.java.anno.Single;
import com.sumslack.compile.dyna.schedule.RunTaskJob;
import com.sumslack.compile.dyna.schedule.TaskRunManager;
import com.sumslack.compile.dyna.schedule.ThreadManager;
import com.sumslack.compile.dyna.util.MapCount;
import com.sun.org.apache.bcel.internal.classfile.CodeException;


public class JavaRunner {

	private static final Logger LOG = LoggerFactory.getLogger(JavaRunner.class);

	private Task task = null;

	private CodeInfo codeInfo;

	private Object objInstance;

	public JavaRunner(Task task) {
		this.task = task;
		this.codeInfo = task.codeInfo();
	}

	/**
	 * compile to class if , class in task , it nothing to do!
	 * 
	 * @return
	 * @throws CodeException
	 */
	public JavaRunner compile() {

		if (codeInfo.getClassz() != null) {
			return this;
		}

		synchronized (codeInfo) {
			if (codeInfo.getClassz() == null) {
				try {

					String code = task.getCode();

					DynamicEngine de = DynamicEngine.getInstance();

					String pack = JavaSourceUtil.findPackage(code);

					String className = JavaSourceUtil.findClassName(code);

					LOG.info("to compile " + pack + "." + className);

					if (className == null) {
						throw new Exception("not find className");
					}

					codeInfo.setClassLoader(de.getParentClassLoader());

					Class<?> clz = (Class<?>) de.javaCodeToClass(pack + "." + className, code);

					Single single = clz.getAnnotation(Single.class);

					codeInfo.setClassz(clz);

					if (single != null) {
						codeInfo.setSingle(single.value());
					}

					MapCount<String> mc = new MapCount<>();
//					// set execute method
					for (Method method : clz.getMethods()) {
						if (!Modifier.isPublic(method.getModifiers()) || method.isBridge() || method.getDeclaringClass() != clz) {
							continue;
						}
						DefaultExecute dExecute = null;
						Execute execute = null; //DefaultExecute的别名
						if ((dExecute = JavaSourceUtil.getAnnotationDeep(method, DefaultExecute.class)) != null) {
							codeInfo.setDefaultMethod(method, Sets.newHashSet(Arrays.asList(dExecute.methods())), dExecute.rpc(), dExecute.restful());
							mc.add(method.getName());
						} else if ((execute = JavaSourceUtil.getAnnotationDeep(method, Execute.class)) != null) { // 先default
							codeInfo.addMethod(method, Sets.newHashSet(Arrays.asList(execute.methods())), execute.rpc(), execute.restful());
							mc.add(method.getName());
						}
					}

					if (mc.size() == 0) {
						throw new CodeRuntimeException("you must set a Execute or DefaultExecute annotation for execute");
					}

					StringBuilder sb = null;
					for (Entry<String, Double> entry : mc.get().entrySet()) {
						if (entry.getValue() > 1) {
							if (sb == null) {
								sb = new StringBuilder();
							}
							sb.append(entry.getKey() + " ");
						}
					}

					if (sb != null && sb.length() > 0) {
						sb.append(" Execute method name repetition");
						throw new CodeRuntimeException(sb.toString());
					}

					codeInfo.getDefaultMethod(); // 空取一下，如果报异常活该

					codeInfo.setClassz(clz);

				} catch (Exception e) {
					e.printStackTrace();
					throw new CodeRuntimeException(e);
				}
			}
		}

		return this;
	}

	/**
	 * instance and inject objcet if object is created , it nothing to do !
	 * 
	 * @return
	 */
	public JavaRunner instance() {

		if (!codeInfo.isSingle()) {// if not single .it only instance by run
			_instance();
			return this;
		}

		if (codeInfo.getJavaObject() != null ) {
			this.objInstance = codeInfo.getJavaObject();
			return this;
		}

		synchronized (codeInfo) {
			if (codeInfo.getJavaObject() == null ) {
				_instance();
			} else {
				this.objInstance = codeInfo.getJavaObject();
			}
		}

		return this;
	}

	private void _instance() {
		try {
			LOG.info("to instance with ioc className: " + codeInfo.getClassz().getName());
			objInstance = codeInfo.getClassz().newInstance();
			if (codeInfo.isSingle()) {
				codeInfo.setJavaObject(objInstance);
			}
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			throw new CodeRuntimeException(e);
		}
	}

	/**
	 * @return
	 */
	public Task getTask() {
		return this.task;
	}

	private static final Object[] DEFAULT_ARG = new Object[0];

	/**
	 * execte task defaultExecute if not found , it execute excutemehtod ， if not found it throw Exception
	 * 
	 * @return
	 * 
	 * @throws CodeException
	 */
	public Object execute() {
		return execute(this.codeInfo.getDefaultMethod().getMethod(), DEFAULT_ARG);
	}

	/**
	 * execte task defaultExecute if not found , it execute excutemehtod ， if not found it throw Exception
	 * 
	 * @return
	 * 
	 * @throws CodeException
	 */
	public Object execute(Method method, Object[] args) {
		long start = System.currentTimeMillis();
		try {
			Object invoke = method.invoke(objInstance, args);
			String endInfo = "Execute OK  " + task.getName() + "/" + method.getName() + " succesed ! use Time : " + (System.currentTimeMillis() - start);
			task.setMessage(endInfo);
			LOG.info(endInfo);
			this.task.updateSuccess();
			return invoke;
		} catch (Exception e) {
			e.printStackTrace();
			this.task.updateError();
			task.setMessage("Execute ERR  " + task.getName() + "/" + method.getName() + " useTime " + (System.currentTimeMillis() - start));
			throw new CodeRuntimeException(e);
		}
	}

	/**
	 * to compile it and validate some function
	 * 
	 * @return it always return true
	 * @throws CodeException
	 * @throws IOException
	 */
	public boolean check() {
		try {
			String code = task.getCode();
			DynamicEngine de = DynamicEngine.getInstance();
			String pack = JavaSourceUtil.findPackage(code);
			String className = JavaSourceUtil.findClassName(code);
			if (className == null) {
				throw new Exception("not find className");
			}

			Class<?> clz = (Class<?>) de.javaCodeToClass(pack + "." + className, code);

			MapCount<String> mc = new MapCount<>();
			// set execute method
			for (Method method : clz.getMethods()) {
				if (!Modifier.isPublic(method.getModifiers()) || method.isBridge() || method.getDeclaringClass() != clz) {
					continue;
				}

				if (JavaSourceUtil.getAnnotationDeep(method, DefaultExecute.class) != null) {
					mc.add(method.getName());
				} else if (JavaSourceUtil.getAnnotationDeep(method, Execute.class) != null) { // 先default
					mc.add(method.getName());
				}
			}

			if (mc.size() == 0) {
				throw new CodeRuntimeException("you must set a Execute or DefaultExecute annotation for execute");
			}

			StringBuilder sb = null;
			for (Entry<String, Double> entry : mc.get().entrySet()) {
				if (entry.getValue() > 1) {
					if (sb == null) {
						sb = new StringBuilder();
					}
					sb.append(entry.getKey() + " ");
				}
			}

			if (sb != null && sb.length() > 0) {
				sb.append(" Execute method name repetition");
				throw new CodeRuntimeException(sb.toString());
			}

			return true;
		}catch(Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
}





