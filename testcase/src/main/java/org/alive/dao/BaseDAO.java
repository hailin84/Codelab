package org.alive.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseDAO {
	/**
	 * close statements and connection
	 * 
	 * @param stmts
	 * @param conn
	 */
	protected void close(Statement[] stmts, Connection conn) {
		if (stmts != null && stmts.length > 0) {
			for (Statement s : stmts) {
				if (s != null) {
					try {
						s.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}

		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	protected Connection getConnection() {
		return DsUtil.getInstance().getConnection();
	}

	/**
	 * 查询一条数据，以Map形式返回. Map中key为大写的数据列名，值为数据的String形式(rs.getString)
	 * 
	 * @param sql
	 * @param parameters
	 *            参数列表，为null表示没有参数
	 * @return
	 */
	public Map<String, String> queryForMap(String sql, List<?> parameters) throws SQLException {
		Connection conn = getConnection();

		PreparedStatement stmt = null;
		ResultSet rs = null;
		Map<String, String> data = new HashMap<String, String>();
		System.out.println("执行SQL语句: [" + sql + "],参数: " + parameters);
		try {
			stmt = conn.prepareStatement(sql);
			int index = 1;
			if (parameters != null) {
				for (Object object : parameters) {
					if (object != null) {
						stmt.setObject(index++, object);
					} else {
						stmt.setNull(index++, Types.VARCHAR);
					}
				}
			}
			rs = stmt.executeQuery();
			if (rs.next()) {
				ResultSetMetaData rsmd = rs.getMetaData();
				int colCount = rsmd.getColumnCount();
				String colName = null;
				for (int i = 1; i <= colCount; i++) {
					colName = rsmd.getColumnName(i);
					data.put(colName.toUpperCase(), rs.getString(colName));
				}
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			close(rs, stmt, conn);
		}

		return data;
	}

	/**
	 * queryForMap：接收可变长参数的版本
	 * 
	 * @param sql
	 * @param parameters
	 *            可变长参数，一个方法只能有一个可变长参数并且必须是最后一个参数
	 * @return
	 */
	public Map<String, String> queryForMap(String sql, Object... parameters) throws SQLException {
		if (parameters == null) {
			return this.queryForMap(sql, (List<?>) null);
		}
		return this.queryForMap(sql, Arrays.asList(parameters));
	}

	/**
	 * 执行SQL语句，如更新、插入、删除等
	 * 
	 * @param sql
	 * @param parameters
	 * @return
	 */
	public int executeUpdate(String sql, List<?> parameters) throws SQLException {
		Connection conn = getConnection();
		System.out.println("执行SQL语句: [" + sql + "],参数: " + parameters);
		PreparedStatement stmt = null;
		int retValue = 0;
		try {
			stmt = conn.prepareStatement(sql);
			// stmt.executeBatch()
			int index = 1;
			if (parameters != null) {
				for (Object object : parameters) {
					if (object != null) {
						stmt.setObject(index++, object);
					} else {
						stmt.setNull(index++, Types.VARCHAR);
					}
				}
			}
			retValue = stmt.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			close(null, stmt, conn);
		}
		return retValue;
	}

	public int executeUpdate(String sql, Object... parameters) throws SQLException {
		if (parameters == null) {
			return this.executeUpdate(sql, (List<?>) null);
		}
		return this.executeUpdate(sql, Arrays.asList(parameters));
	}

	/**
	 * 执行批量SQL语句
	 * 
	 * @param sql
	 * @param parameters
	 * @return
	 */
	public int[] executeBatch(String sql, List<List<?>> parameters) throws SQLException {
		Connection conn = getConnection();
		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement(sql);
			// stmt.executeBatch()

			if (parameters != null) {
				for (List<?> eachOne : parameters) {
					int index = 1;
					for (Object object : eachOne) {
						if (object != null) {
							stmt.setObject(index++, object);
						} else {
							stmt.setNull(index++, Types.VARCHAR);
						}
					}
					stmt.addBatch();
				}
				return stmt.executeBatch();
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			close(null, stmt, conn);
		}

		return new int[0];
	}

	/**
	 * 关闭使用过的数据库相关资源
	 * 
	 * @param rs
	 * @param stmt
	 * @param conn
	 */
	public static void close(ResultSet rs, Statement stmt, Connection conn) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			// Trace.logWarn("LianaJDBC", "JDBC.BaseDAO 对象关闭异常", e);
		}

		try {
			if (stmt != null) {
				stmt.close();
			}
		} catch (SQLException e) {
			// Trace.logWarn("LianaJDBC", "JDBC.BaseDAO 对象关闭异常", e);
		}
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (Exception e) {
			// Trace.logWarn("LianaJDBC", "JDBC.BaseDAO releaseConnection异常",
			// e);
		}
	}

}
