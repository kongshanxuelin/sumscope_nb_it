package com.sumslack.patterndesign.demo.bridge;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class OracleDataSource extends AbsDataSource{
	@Override
	public List readData(String tableName, Map<String, String> filter) {
		System.out.println("从Oracle导出数据...");
		return Collections.EMPTY_LIST;
	}
}
