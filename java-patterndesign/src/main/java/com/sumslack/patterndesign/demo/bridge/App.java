package com.sumslack.patterndesign.demo.bridge;

public class App {
	public static void main(String[] args) {
		AbsDataSource oracle = new OracleDataSource();
		AbsDataSource mysql = new MySQLDataSource();
		
		AbsFileExport xml = new XMLFileExport();
		xml.setDatasource(oracle);
		xml.export();
		
		AbsFileExport txt = new TxtFileExport();
		txt.setDatasource(mysql);
		txt.export();
		
	}
}
