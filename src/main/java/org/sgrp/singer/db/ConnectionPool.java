package org.sgrp.singer.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

public class ConnectionPool {
	public class WakeUp extends Thread {
		ConnectionPool	cp	= null;

		WakeUp(ConnectionPool cp) {
			this.cp = cp;
			setDaemon(true);
		}

		@Override
		public void run() {
			while (true) {
				try {
					sleep(WAKE_UP_INTERVAL);
				} catch (InterruptedException e) {
				}
				cp.freeConnectionMaintenance();
			}
		}
	}

	public static final String					ACCESS_PRODUCT			= "ACCESS";

	public static final String					CLOUDSCAPE_PRODUCT		= "CLOUDSCAPE";

	private static HashMap<Connection, String>	connMap					= new HashMap<Connection, String>();

	public static final String					DERBY_PRODUCT			= "Apache Derby";

	public static final String					HYPERSONIC_PRODUCT		= "HSQL Database Engine";

	public static final String					HYPERSONIC_PRODUCT_NEW	= "HSQL Database Engine";

	public static final int						MAX_CONNECTS			= 30;

	public static final String					MYSQL_PRODUCT			= "MySQL";

	public static final String					ORACLE_PRODUCT			= "Oracle";

	static final long							TIME_OUT				= 10 * 1000;

	static final long							WAKE_UP_INTERVAL		= 30 * 1000;

	public static HashMap<Connection, String> getConnectionMap() {
		return connMap;
	}

	public static boolean isAccess(Connection con) throws SQLException {
		return con.getMetaData().getDatabaseProductName().equals(ACCESS_PRODUCT);
	}

	public static boolean isDerby(Connection con) throws SQLException {
		return con.getMetaData().getDatabaseProductName().equals(DERBY_PRODUCT);
	}

	public static boolean isHypersonic(Connection con) throws SQLException {
		return con.getMetaData().getDatabaseProductName().equals(HYPERSONIC_PRODUCT) || con.getMetaData().getDatabaseProductName().equals(HYPERSONIC_PRODUCT_NEW);
	}

	public static boolean isMySQL(Connection con) throws SQLException {
		return con.getMetaData().getDatabaseProductName().equals(MYSQL_PRODUCT);
	}

	public static boolean isOracle(Connection con) throws SQLException {
		return con.getMetaData().getDatabaseProductName().equals(ORACLE_PRODUCT);
	}

	private String				connect_string	= "jdbc:oracle:thin:demo/demo@xxxx:1521:fs";

	private DatabaseMetaData	db_metadata		= null;

	private String				driver_class	= "oracle.jdbc.driver.OracleDriver";

	private Vector<Connection>	free_pool		= null;

	private Vector<Long>		free_pool_dates	= null;

	private int					num_connects	= 0;

	private String				password		= null;

	private Connection			roCon			= null;

	private int					roConRefCount	= 0;

	private String				userid			= null;

	private WakeUp				wake_up			= null;

	public ConnectionPool() throws SQLException {
		free_pool = new Vector<Connection>();
		free_pool_dates = new Vector<Long>();

		Connection conn = newConnection(this.getClass().getName());
		db_metadata = conn.getMetaData();
		freeConnection(conn);

		wake_up = new WakeUp(this);
		wake_up.start();
	}

	public ConnectionPool(String connect_string, String driver) throws SQLException {
		this.connect_string = connect_string;
		this.driver_class = driver;
		free_pool = new Vector<Connection>();
		free_pool_dates = new Vector<Long>();
		Connection conn = newConnection(this.getClass().getName());
		db_metadata = conn.getMetaData();
		freeConnection(conn);

		wake_up = new WakeUp(this);
		wake_up.start();
	}

	public ConnectionPool(String connect_string, String driver, String userid, String password) throws SQLException {
		this.connect_string = connect_string;
		this.driver_class = driver;
		this.userid = userid;
		this.password = password;
		free_pool = new Vector<Connection>();
		free_pool_dates = new Vector<Long>();
		Connection conn = newConnection(this.getClass().getName());
		db_metadata = conn.getMetaData();
		freeConnection(conn);

		wake_up = new WakeUp(this);
		wake_up.start();
	}

	public void closeDown() throws SQLException {
		if (roCon != null) {
			try {
				roCon.close();
			} catch (SQLException e) {
				System.out.println("Error closing readOnlyConnection " + e.getMessage());
			}
		}
		for (int i = 0; i < free_pool.size(); i++) {
			Connection conn = free_pool.elementAt(i);
			try {
				conn.close();
			} catch (SQLException e) {
				System.out.println("Error closing connection " + e.getMessage());
			} finally {
				connMap.remove(conn);
				free_pool.removeElementAt(i);
				free_pool_dates.removeElementAt(i);
				num_connects--;
			}
		}
		// should close down active connections
		wake_up.interrupt();
	}

	public String dump() {
		String result = "Connection Pool Dump ";
		result += " driver_class = " + driver_class;
		result += " connect_strint = " + connect_string;
		result += " free_pool.size() = " + free_pool.size();
		result += " free_pool_dates.size() = " + free_pool_dates.size();
		result += " num_connects = " + num_connects;
		return result;
	}

	synchronized public void freeConnection(Connection conn) {
		if (conn == null)
			return;
		
		int conn_ind = free_pool.indexOf(conn);
		if (conn_ind != -1) {
			connMap.remove(conn);
			System.out.println("ConnectionPool tried to free an already free connection");
		}

		try {
			if (!conn.isClosed()) {
				String name = connMap.get(conn);
				name = name + "_free";
				// connMap.remove(conn);
				connMap.put(conn, name);
				free_pool.addElement(conn);
				free_pool_dates.addElement(System.currentTimeMillis());
			}
		} catch (SQLException e) {
			System.out.println("ConnectionPool isClosed() error");
		}
	}

	synchronized public void freeConnectionMaintenance() {
		if (num_connects > 5) {
			System.out.println("You have more than 5 connections open num_connects=" + num_connects);
		}

		for (int i = 0; i < free_pool.size(); i++) {
			long last_used = (free_pool_dates.elementAt(i)).longValue();

			if (System.currentTimeMillis() - last_used > TIME_OUT) {
				removeConFromPool(i);
			}
		}
	}

	/**
	 * Frees readonly connections many threads can use one readonly connection! Tested by SR in questionnaire
	 * 
	 * @see getReadOnlyConnection
	 */
	public synchronized int freeReadOnlyConnection(Connection con) throws SQLException {

		if ((con == null) || (con != roCon)) { throw new SQLException("You have tried to call ConnectionPool.freeReadOnlyConnection(Connection) with a connection you did not get with .getReadOnlyConnection()"); }

		if (roConRefCount < 0) { throw new SQLException("You have tried to called ConnectionPool.freeReadOnlyConnection(Connection) more than you have called getReadOnlyConnection()"); }

		roConRefCount--;

		if (roConRefCount == 0) {
			roCon.close();
			roCon = null;
		}

		return roConRefCount;
	}

	public DatabaseMetaData getMetaData() throws SQLException {
		return db_metadata;
	}

	public int getNumConnects() {
		return num_connects;
	}

	synchronized public Connection newConnection(String className) throws SQLException {
		Connection conn = newConnectionImpl();

		connMap.put(conn, className + " " + (new java.util.Date()).toString());

		return conn;
	}

	private Connection newConnectionImpl() throws SQLException {
		Connection conn = null;
		if (free_pool.size() == 0) {
			if (num_connects > MAX_CONNECTS) {
				System.out.println("Closing down due to excessive number of concurrent database connects");
				try {
					closeDown();
				} finally {
					System.exit(1);
				}
			}
			int max_connection_allowed = 5;
			if (num_connects > max_connection_allowed) { throw new SQLException("TOO_MANY_SESSIONS_OPEN"); }
			try {
				Class.forName(driver_class);
			} catch (ClassNotFoundException e) {
				throw new SQLException("driver not found in ConnectionPool.newConnectionImpl() " + driver_class);
			}
			if ((userid != null) && (password != null)) {
				conn = DriverManager.getConnection(connect_string, userid, password);
			} else {
				conn = DriverManager.getConnection(connect_string);
			}
			conn.setAutoCommit(false);
			freeConnection(conn);
			num_connects++;
		}
		conn = free_pool.elementAt(0);

		connMap.remove(conn);
		free_pool.removeElementAt(0);
		free_pool_dates.removeElementAt(0);
		return conn;
	}

	public synchronized Connection newReadOnlyConnection() throws SQLException {
		if (roCon == null) {
			try {
				Class.forName(driver_class);
				if ((userid != null) && (password != null)) {
					roCon = DriverManager.getConnection(connect_string, userid, password);
				} else {
					roCon = DriverManager.getConnection(connect_string);
				}
				roCon.setAutoCommit(true);
				// roCon.setReadOnly(true);
				roConRefCount = 1;
				if (roConRefCount > MAX_CONNECTS) {
					System.out.println("WARNING you have more than " + MAX_CONNECTS + " references to a readonly connection");
				}
			} catch (ClassNotFoundException e) {
				throw new SQLException("driver not found " + driver_class);
			}
		} else {
			roConRefCount++;
		}
		return roCon;
	}

	private void removeConFromPool(int i) {
		String name = null;
		Connection conn = free_pool.elementAt(i);
		try {
			if (conn != null) {
				name = connMap.get(conn);
				conn.close();
			}
		} catch (SQLException e) {
			System.out.println("ConnectionPool: error closing connection " + name);
		} finally {
			connMap.remove(conn);
			free_pool.removeElementAt(i);
			free_pool_dates.removeElementAt(i);
			num_connects--;
		}
	}

}
