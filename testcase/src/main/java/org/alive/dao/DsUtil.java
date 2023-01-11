package org.alive.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Class to load db.properties files
 * 
 * @author hailin84
 * 
 */
public class DsUtil {
	private static final String DS_FILE = "/ds.properties";

	// == Keys for jdbc in ds.properties file
	private static String KEY_JDBC_URL = "jdbc.url";
	private static String KEY_JDBC_DRIVERCLASS = "jdbc.driverClass";
	private static String KEY_JDBC_USER = "jdbc.user";
	private static String KEY_JDBC_PASSWORD = "jdbc.password";

	private Properties p;

	// private DataSource ds;

	private DsUtil() {
		p = new Properties();
		try {
			// DsUtil.class.getClassLoader().getResourceAsStream(name)
			p.load(this.getClass().getResourceAsStream(DS_FILE));
			// ds = new
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			Class.forName(p.getProperty(KEY_JDBC_DRIVERCLASS));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Connection getConnection() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(p.getProperty(KEY_JDBC_URL),
					p.getProperty(KEY_JDBC_USER),
					p.getProperty(KEY_JDBC_PASSWORD));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return conn;
	}

	private static final class Holder {
		static DsUtil instance = new DsUtil();
	}

	public static DsUtil getInstance() {
		return Holder.instance;
	}
}
