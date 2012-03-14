package org.sgrp.singer.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;

import org.sgrp.singer.AccessionConstants;
import org.sgrp.singer.AccessionServlet;
import org.sgrp.singer.ResourceManager;

public class ShoppingCartManager extends DataManager{
	public static ShoppingCartManager	mgr;
	public static final String tname = "order";
	
	public synchronized int saveCart(String userid, Map<String,String> map) throws SQLException {
		int orderid = 0;
		if (userid == null || map==null) { throw new SQLException(ResourceManager.getString("save.user.null")); }
		Connection conn = null;
		try {
			conn = AccessionServlet.getCP().newConnection(this.toString());
			orderid = getNextID(tname); 
			PreparedStatement pstmt = conn.prepareStatement("insert into orders (orderid,userid,odate) values(?,?,?)");
			pstmt.setInt(1, orderid);
			pstmt.setString(2, userid);
			pstmt.setTimestamp(3, new java.sql.Timestamp(new java.util.Date().getTime()));
			pstmt.execute();
			pstmt.close();

			
			PreparedStatement postmt = conn.prepareStatement("insert into orderitems (orderid, accenumb_,collcode_) values(?,?,?)");
			for(Iterator<String> itr = map.keySet().iterator(); itr.hasNext();)
			{
				try{
				String accenumb = itr.next();
				String collcode = map.get(accenumb);
				collcode = AccessionConstants.replaceString(collcode, AccessionConstants.COLLECTION, "", 0);
				postmt.setInt(1, orderid);
				postmt.setString(2, accenumb);
				postmt.setInt(3, Integer.parseInt(collcode));
				postmt.execute();
				}
				catch(Exception e){
					e.printStackTrace(System.out);
				}
			}
			postmt.close();
			conn.commit();
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SQLException();
		} finally {
			AccessionServlet.getCP().freeConnection(conn);
		}
		return orderid;
	}

	public synchronized void updateOrderEmailStatus(int orderid, boolean emailSent) throws SQLException {
		if (orderid != 0) { throw new SQLException(ResourceManager.getString("save.user.null")); }
		Connection conn = null;
		try {
			conn = AccessionServlet.getCP().newConnection(this.toString());
			PreparedStatement pstmt = conn.prepareStatement("update orders set emailsent=?, emailsentdate=? where orderid=?");
			pstmt.setString(1, emailSent?"Y":"N");
			pstmt.setTimestamp(2, new java.sql.Timestamp(new java.util.Date().getTime()));
			pstmt.setInt(3, orderid);
			pstmt.execute();
			pstmt.close();
			conn.commit();
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SQLException();
		} finally {
			AccessionServlet.getCP().freeConnection(conn);
		}
	}
	
	public static ShoppingCartManager getInstance() {
		if (mgr == null) {
			mgr = new ShoppingCartManager();
		}
		return mgr;
	}
}
