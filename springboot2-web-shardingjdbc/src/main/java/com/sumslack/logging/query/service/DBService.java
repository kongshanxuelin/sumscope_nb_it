package com.sumslack.logging.query.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class DBService {
	
	@Resource
	private DataSource dataSource;
	
	public static String fmt_datetimeFormat = "yyyy-MM-dd HH:mm:ss";
	public static final class SQL {
		public static final String LOG_QUERY_BYPROJECT = "select * from `t_logging` where project=? limit ?,?";
		public static final String LOG_QUERY_BYPROJECTNULL = "select * from `t_logging` where project is null limit ?,?";
	}
	
	public Collection queryLogging(String project,int start,int pagesize) {
		if(StringUtils.isEmpty(project)) {
			return getCollection(SQL.LOG_QUERY_BYPROJECTNULL,start,pagesize);
		}else {
			return getCollection(SQL.LOG_QUERY_BYPROJECT,project,start,pagesize);
		}
	}
	
	public final Collection getCollection(Connection conn,
			String command, Object[] parameters) {
		List list = null;
		ResultSet resultset = null;
		try {
			resultset = executeQuery(conn, command, parameters);
			list = resultSetToList(resultset);
		} catch (Exception ex) {
			throw new RuntimeException(ex.getMessage() + "", ex);
		} finally {
			try {
				if (resultset != null) {
					close(resultset, resultset.getStatement());
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return (Collection) list;
	}
	
	public final int executeUpdate(String command, Object... parameters) {
		Connection conn = null;
		try{
			conn = dataSource.getConnection();
			int res = executeUpdate(conn,command,parameters);
			return res;
		}catch(Exception ex){
			ex.printStackTrace();
			return 0;
		}finally {
			close(conn);
		}
	}
	public final Map getMap(String sql,Object... parameters) {
		Connection conn = null;
		try{
			conn = dataSource.getConnection();
			return getMap(conn,sql,parameters);
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}finally {
			close(conn);
		}	
	}
	public final Collection getCollection(String sql,Object... parameters) {
		Connection conn = null;
		try{
			conn = dataSource.getConnection();
			return getCollection(conn, sql, parameters);
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}finally {
			close(conn);
		}	
	}
	
	public final int executeUpdate(Connection connection,
			String command, Object... parameters) throws SQLException {
		PreparedStatement statement = null;
		int result = 0;

		try {
			statement = connection.prepareStatement(command);
			if (parameters != null) {
				for (int i = 0; i < parameters.length; i++) {
					if (parameters[i] instanceof String) {
						String paraStr = (String) parameters[i];
						if (paraStr.length() > 255) {
							statement.setCharacterStream(i + 1,
									new java.io.StringReader(paraStr), paraStr
											.length());
						} else
							statement.setString(i + 1, paraStr);
					} else
						statement.setObject(i + 1, parameters[i]);
				}
			}
			result = statement.executeUpdate();
		}finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}
		}
		return result;
	}
	
	public void close(Connection conn) {
		if (conn != null) {
			close(null, null, conn);
		}
	}

	public final Map getMap(Connection conn, String command,
			Object... parameters) {
		Map map = null;
		ResultSet resultset = null;
		try {
			resultset = executeQuery(conn, command, parameters);
			map = resultSetToMap(resultset);
		} catch (Exception ex) {
			throw new RuntimeException(ex.getMessage() + "", ex);
		} finally {
			try {
				if (resultset != null) {
					close(resultset, resultset.getStatement());
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return map;
	}
	
	
	private void close(ResultSet rs, Statement stmt) {
		close(rs, stmt, null);
	}

	
	private void close(ResultSet rs) {
		if (rs != null) {
			try {
				close(rs, rs.getStatement());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	private void close(ResultSet rs, Statement stmt, Connection conn) {
		if (rs != null) {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (java.sql.SQLException ex) {
				ex.printStackTrace();
			}
		}
		if (stmt != null) {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (java.sql.SQLException ex) {
				ex.printStackTrace();
			}
		}
		if (conn != null) {
			try {
				if (conn != null) {
					if (!conn.isClosed())
						conn.close();
				}
			} catch (java.sql.SQLException ex) {
				ex.printStackTrace();
			}
		}
	}
	private Map resultSetToMap(ResultSet rs) {
		if (rs == null) {
			return null;
		}
		try {
			ResultSetMetaData md = rs.getMetaData();
			int columnCount = md.getColumnCount();
			List list = new ArrayList();
			if (rs.next()) {
				Map rowData = new HashMap(columnCount);
				for (int i = 1; i <= columnCount; i++) {
					Object colcont = rs.getObject(i);
					String col = md.getColumnLabel(i);
					if(col.equals("")){
						col = md.getColumnName(i).toLowerCase();
					}
					rowData.put(col,colcont);
					if ((md.getColumnType(i) == Types.VARCHAR)
							|| (md.getColumnType(i) == Types.CHAR)){
						rowData.put(col, formatNullStr(colcont));
					}else if(md.getColumnType(i) == Types.BIT){
						if(colcont == null){
							rowData.put(col, null);
						}else if(colcont instanceof Boolean){
							int bc = rs.getInt(i);
							if(bc>=1){
								rowData.put(col, bc);
							}else{
								rowData.put(col, 0);
							}
						}else{
							rowData.put(col, (Byte)colcont);
						}
						
					}else if(colcont instanceof byte[]){
						String temp = new String((byte[])colcont);
						rowData.put(col,temp);
					}
				}
				return rowData;
			} else {
				return null;
			}
		} catch (SQLException ex) {
			throw new RuntimeException("resultSetToMap error:", ex);
		}
	}
	private final ResultSet executeQuery(Connection connection,
			String command, Object... parameters) {
		Statement statement = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			if ((parameters == null) || (parameters.length == 0)) {
				statement = connection.createStatement();
				resultSet = statement.executeQuery(command);
			} else {
				preparedStatement = connection.prepareStatement(command,
						ResultSet.TYPE_SCROLL_SENSITIVE,
						ResultSet.CONCUR_READ_ONLY);

				for (int i = 0; i < parameters.length; i++) {
					preparedStatement.setObject(i + 1, parameters[i]);
				}

				resultSet = preparedStatement.executeQuery();
			}
		} catch (Exception ex) {
			throw new RuntimeException("error:"+ex.getMessage(), ex);
		}
		return resultSet;
	}
	private List resultSetToList(ResultSet rs) {
		try {
			if (rs == null) {
				return Collections.EMPTY_LIST;
			}

			ResultSetMetaData md = rs.getMetaData();
			int columnCount = md.getColumnCount();
			List list = new ArrayList();
			Map rowData;
			

			while (rs.next()) {
				rowData = new HashMap(columnCount);

				for (int i = 1; i <= columnCount; i++) {
					String col = md.getColumnLabel(i);
					if(col.equals("")){
						col = md.getColumnName(i).toLowerCase();
					}
					Object colcont = rs.getObject(i);
					rowData.put(col, colcont);
	
					if ((md.getColumnType(i) == Types.VARCHAR)
							|| (md.getColumnType(i) == Types.CHAR )){
						rowData.put(col, formatNullStr(colcont));
					}					
					else if(md.getColumnType(i) == Types.BIT){
						if(colcont == null){
							rowData.put(col, null);
						}else if(colcont instanceof Boolean){
							//boolean bc = (Boolean)colcont;
							int bc = rs.getInt(i);
							if(bc>=1){
								rowData.put(col, bc);
							}else{
								rowData.put(col, 0);
							}
						}else{
							rowData.put(col, (Byte)colcont);
						}
						
					}else if(colcont instanceof byte[]){
						String temp = new String((byte[])colcont);
						rowData.put(col,temp);
					}
				}
				list.add(rowData);
			}
			return list;
		} catch (Exception ex) {
			throw new RuntimeException("error:", ex);
		}
	}
	
	public String formatNullStr(Object obj) {
		if(obj == null) {
			return null;
		}else {
			return obj.toString();
		}
	}
	
	public String format(Date date) {
		return format(date,fmt_datetimeFormat);
	}
	
	public String format(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern(format);
		return sdf.format(date);
	}
}
