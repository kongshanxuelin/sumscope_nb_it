package com.sumslack.patterndesign.demo.bridge;

public abstract class AbsFileExport {
	protected AbsDataSource datasource;

	public void setDatasource(AbsDataSource datasource) {
		this.datasource = datasource;
	}
	
	public abstract String export();
}
