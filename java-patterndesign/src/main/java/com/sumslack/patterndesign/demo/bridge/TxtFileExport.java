package com.sumslack.patterndesign.demo.bridge;

import java.util.List;

public class TxtFileExport extends AbsFileExport {

	@Override
	public String export() {
		List data = datasource.readData("testTable2",null);
		System.out.println("导出成TXT");
		return "";
	}

}
