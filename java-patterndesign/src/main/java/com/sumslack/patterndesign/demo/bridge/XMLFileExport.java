package com.sumslack.patterndesign.demo.bridge;

import java.util.List;

public class XMLFileExport extends AbsFileExport {

	@Override
	public String export() {
		List data = datasource.readData("testTable",null);
		System.out.println("导出成XML");
		return "";
	}

}
