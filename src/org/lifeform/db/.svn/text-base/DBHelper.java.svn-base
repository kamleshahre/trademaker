package org.lifeform.db;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import org.lifeform.core.AppException;

public class DBHelper {
	protected static String framework = "embedded";
	protected static String driver = "org.hsqldb.jdbcDriver";
	protected static String protocol = "jdbc:hsqldb:";
	private static Connection conn = null;

	public static Connection getConnection() throws Exception {
		if (conn != null) {
			return conn;
		}
		Class.forName(driver).newInstance();
		Properties props = new Properties();
		props.put("create", "true");
		props.put("databaseName", "db");
		props.put("user", "sa");
		props.put("password", "");

		conn = DriverManager.getConnection(protocol, props);
		return conn;
	}

	public static void closeConnection() {
		try {
			if (conn != null) {
				conn.commit();
				conn.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			conn = null;
		}
	}

	public static boolean startDB() throws Exception {
		File dir = new File(System.getProperty("hsqldb.system.home"));
		if (!dir.exists()) {
			return false;
		}
		if (getConnection() == null) {
			throw new AppException("Cannot connect to database!");
		}
		ResultSet rs = getConnection().getMetaData().getTables(null, null,
				"PKB_KNOWLEDGE", null);
		return rs.next();
	}

	public static void initDB() throws Exception {
		System.out.println("Initialize DB begins");

		Connection conn = getConnection();

		conn.setAutoCommit(false);

		Statement s = conn.createStatement();

		String sql = "CREATE TABLE pkb_knowledge(id INT generated always as identity, subject VARCHAR(128), keywords VARCHAR(128), content BLOB)";
		s.execute(sql);

		sql = "CREATE INDEX pkb_knowledge_subject ON pkb_knowledge(subject)";
		s.execute(sql);

		sql = "CREATE INDEX pkb_knowledge_keywords ON pkb_knowledge(keywords)";
		s.execute(sql);

		s.close();

		closeConnection();
	}

}
