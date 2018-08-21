package com.sumslack.patterndesign.demo.bridge;

import java.util.List;
import java.util.Map;

public abstract class AbsDataSource {
	public abstract List readData(String tableName,Map<String,String> filter);
}
