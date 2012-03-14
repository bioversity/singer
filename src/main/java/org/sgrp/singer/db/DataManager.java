package org.sgrp.singer.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.sgrp.singer.AccessionServlet;
import org.sgrp.singer.action.GenericAction;

public class DataManager {

	// public int getNextCBID() throws SQLException {
	// return getNextID("cb");
	// }

	public static boolean isNull(String value) {
		return GenericAction.isNull(value);
	}

	public int getNextID(String tname) throws SQLException {
		int count = 1;
		Connection conn = null;
		try {
			conn = AccessionServlet.getCP().newConnection(this.toString());
			PreparedStatement ps = conn.prepareStatement("select tcount from status where tname=?");
			ps.setString(1, tname);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				count = rs.getInt("tcount");
				count = count + 1;
			}
			updateCount(tname, count);
			rs.close();
			ps.close();
			conn.commit();
		} catch (SQLException se) {
			count = 1;

		} finally {
			AccessionServlet.getCP().freeConnection(conn);
		}
		return count;
	}

	public void updateCount(String tname, int count) throws SQLException {
		Connection conn = null;
		try {
			conn = AccessionServlet.getCP().newConnection(this.toString());
			PreparedStatement ps = conn.prepareStatement("update status set tcount=? where tname=?");
			ps.setInt(1, count);
			ps.setString(2, tname);
			int iCount = ps.executeUpdate("update status set tcount=" + count + " where tname='" + tname + "'");
			ps.close();
			conn.commit();
		} catch (SQLException se) {

		} finally {
			AccessionServlet.getCP().freeConnection(conn);
		}
	}
}
