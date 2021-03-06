package org.sgrp.singer.db;

import java.net.*;
import java.io.*;
import java.util.*;

import java.net.MalformedURLException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpSession;

import org.apache.xmlrpc.XmlRpcException;
import org.sgrp.singer.AccessionConstants;
import org.sgrp.singer.AccessionServlet;
import org.sgrp.singer.ResourceManager;
import org.sgrp.singer.form.MemberForm;
import org.sgrp.singer.form.PIDMemberForm;
import org.sgrp.singer.indexer.Keywords;
import org.sgrp.singer.xmlrpc.DrupalXmlRpcService;

import org.json.*;

public class UserManager {
	public static UserManager	mgr;

	private static Logger LOG = Logger.getLogger("");

	public void saveUser(MemberForm memb) throws SQLException {
		if (memb == null) { throw new SQLException(ResourceManager.getString("save.user.null")); }
		Connection conn = null;
		try {
			conn = AccessionServlet.getCP().newConnection(this.toString());

			PreparedStatement pstmt = conn.prepareStatement("insert into memb(userid,password, firstname,lastname,instname, street, zip, city,ctycode_, email, usercode_,url,faocode) values(?,?,?,?,?,?,?,?,?,?,?,?,?)");
			pstmt.setString(1, memb.getNuserid());
			pstmt.setString(2, memb.getNpassword());
			pstmt.setString(3, memb.getNfname());
			pstmt.setString(4, memb.getNlname());
			pstmt.setString(5, memb.getNiname());
			pstmt.setString(6, memb.getNstreet());
			pstmt.setString(7, memb.getNzip());
			pstmt.setString(8, memb.getNcity());
			String cid = AccessionConstants.replaceString(memb.getNctycode(), AccessionConstants.COUNTRY, "", 0);
			pstmt.setString(9, cid.toUpperCase());
			pstmt.setString(10, memb.getNemail());
			String uid = AccessionConstants.replaceString(memb.getNusercode(), AccessionConstants.USER, "", 0);
			pstmt.setInt(11, Integer.parseInt(uid));
			pstmt.setString(12, memb.getNurl());
			pstmt.setString(13, memb.getNfaocode());
			//System.out.println("Inserted member with userid:"+memb.getNuserid());
			pstmt.executeUpdate();
			pstmt.close();

			conn.commit();
		} catch (SQLException se) {
			throw new SQLException();
		} finally {
			AccessionServlet.getCP().freeConnection(conn);
		}
		
	}

	public void updateUser(MemberForm memb) throws SQLException {
		if (memb == null) { throw new SQLException(ResourceManager.getString("save.user.null")); }
		Connection conn = null;
		try {
			conn = AccessionServlet.getCP().newConnection(this.toString());

			PreparedStatement pstmt = conn.prepareStatement("update memb set password=?, firstname=?, lastname=?, instname=?, street=?, zip=?, city=?, ctycode_=?, email=?,usercode_=?,url=?,faocode=? where userid=?");
			//System.out.println("Memb fname "+memb.getNfname());
			pstmt.setString(1, memb.getNpassword());
			pstmt.setString(2, memb.getNfname());
			pstmt.setString(3, memb.getNlname());
			pstmt.setString(4, memb.getNiname());
			pstmt.setString(5, memb.getNstreet());
			pstmt.setString(6, memb.getNzip());
			pstmt.setString(7, memb.getNcity());
			String cid = AccessionConstants.replaceString(memb.getNctycode(), AccessionConstants.COUNTRY, "", 0);
			pstmt.setString(8, cid.toUpperCase());
			pstmt.setString(9, memb.getNemail());
			String uid = AccessionConstants.replaceString(memb.getNusercode(), AccessionConstants.USER, "", 0);
			pstmt.setInt(10, Integer.parseInt(uid));
			pstmt.setString(11, memb.getNurl());
			pstmt.setString(12, memb.getNfaocode());
			pstmt.setString(13, memb.getNuserid());
			//int up = pstmt.executeUpdate();
			//System.out.println("Updated "+up+" member with userid:"+memb.getNuserid());
			
			conn.commit();
		} catch (SQLException se) {
			throw new SQLException();
		} finally {
			AccessionServlet.getCP().freeConnection(conn);
		}
				
	}

	public boolean userExists(String userId) {
		Connection conn = null;
		boolean userExists = false;
		try {
			conn = AccessionServlet.getCP().newConnection(this.toString()); 
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from memb where userid='" + userId + "'");
			if (rs.next()) {
				// System.out.println("User Exists with name:" + userId);
				userExists = true;
			}
			rs.close();
			stmt.close();
			conn.commit();
		} catch (SQLException se) {

		} finally {
			AccessionServlet.getCP().freeConnection(conn); 
		}
		return userExists;
	}

	/*public List<String> getAllUsers() throws SQLException {
		Connection conn = null;
		List<String> users = new ArrayList<String>();

		try {
			conn = AccessionServlet.getCP().newConnection(this.toString());
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select userid from memb where type='" + AccessionConstants.MEMBER + "'");
			while (rs.next()) {
				users.add(rs.getString("userid"));
			}
			rs.close();
			stmt.close();
			conn.commit();
		} catch (SQLException se) {

		} finally {
			AccessionServlet.getCP().freeConnection(conn);
		}
		return users;
	}*/
	
	public PIDMemberForm getUser(HttpSession session) throws Exception
	{
		if(session == null || session.getAttribute("memberDetails")==null
				|| !(session.getAttribute("memberDetails") instanceof PIDMemberForm))
		{
			throw new Exception(ResourceManager.getString("retrieve.user.null"));
		}
		
		PIDMemberForm memberDetails = (PIDMemberForm)session.getAttribute("memberDetails");
		
		return memberDetails;
		
	}
	
	public MemberForm getUser(String userId) throws SQLException {
		if (userId == null || userId.length() == 0) { throw new SQLException(ResourceManager.getString("retrieve.user.null")); }
		Connection conn = null;
		MemberForm memb = null;
		try {
			conn = AccessionServlet.getCP().newConnection(this.toString());
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from memb where userid='" + userId + "'");
			if (rs.next()) {
				memb = new MemberForm(userId);
				memb.setNpassword(rs.getString("password"));
				memb.setNconfimpass(memb.getNpassword());
				memb.setNfname(rs.getString("firstname"));
				memb.setNlname(rs.getString("lastname"));
				memb.setNiname(rs.getString("instname"));
				memb.setNstreet(rs.getString("street"));
				memb.setNcity(rs.getString("city"));
				memb.setNzip(rs.getString("zip"));
				memb.setNctycode(AccessionConstants.COUNTRY+rs.getString("ctycode_").toLowerCase());
				memb.setNusercode(AccessionConstants.USER+rs.getInt("usercode_")+"".toLowerCase());
				memb.setNemail(rs.getString("email"));
				memb.setNfaocode(rs.getString("faocode"));
				memb.setNurl(rs.getString("url"));
			}
			rs.close();
			stmt.close();			
			conn.commit();
		} catch (SQLException se) {

		} finally {
			AccessionServlet.getCP().freeConnection(conn); 
		}
		return memb;
	}

	/**
	 * This method check if the user/password is valid on the PID Server
	 * 
	 * @param userId
	 * @param password
	 * @return a boolean whether the user/password combination is valid
	 * @throws NoSuchAlgorithmException 
	 * @throws IllegalStateException 
	 * @throws InvalidKeyException 
	 * @throws MalformedURLException 
	 * @throws XmlRpcException 
	 * @throws Exception 
	 */
	public PIDMemberForm loginPID (String userId, String password) 
	{
		PIDMemberForm member = new PIDMemberForm();
		//final String serviceURL = "http://mls.planttreaty.org/itt/services/xmlrpc";
		//final String serviceURL = "http://mls.planttreaty.org/Ex_itt/services/xmlrpc";
		//final String serviceURL = "http://www.itworks.it/itt/index.php?r=extsys/getUserDetails&esUsername=SINGER&esPassword=F3rrar1n0&username=" + userId + "&password=" + password;
		final String serviceURL = "https://mls.planttreaty.org/itt/index.php?r=extsys/getUserDetails&esUsername=SINGER&esPassword=F3rrar1n0&username=" + userId + "&password=" + password;
		DrupalXmlRpcService service;
		try
		{
            // make request to the treaty server
            URL pidServer = new URL(serviceURL);
            URLConnection pidConn = pidServer.openConnection();
            BufferedReader in = new BufferedReader(
                                    new InputStreamReader(
                                    pidConn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) 
                response.append(inputLine);

            in.close();

            // parse the json response
            JSONObject obj = new JSONObject(response.toString());
            if(obj.has("errorCode")) {
                throw new Exception(obj.getString("errorMessage"));
            }

            // login successful
			member.setNfname(obj.getString("name"));
			member.setNlname(obj.getString("surname"));
			member.setNemail(null);
			member.setNCompany(null);
			member.setNType(obj.getString("type"));
			//String cty = userDetails.get(AccessionConstants.USER_COUNTRY);
			member.setNCountry(obj.getString("country"));
			member.setNuserpid(obj.getString("pid"));

            member.setNShippingAddress(obj.getString("shipAddress"));
            member.setNZip(null);
            member.setNCity(null);
            /*
			if(userDetails.get(AccessionConstants.USER_ADDRESS_TO_USE).equals("Another"))
			{
				member.setNShippingAddress(userDetails.get(AccessionConstants.USER_SHIPPING_ADDRESS));
				member.setNZip(userDetails.get(AccessionConstants.USER_SHIPPING_POSTAL_CODE));
				member.setNCity(userDetails.get(AccessionConstants.USER_SHIPPING_CITY));
			}
			else
			{
				member.setNShippingAddress(userDetails.get(AccessionConstants.USER_ADDRESS));
                member.setNZip(userDetails.get(AccessionConstants.USER_POSTAL_CODE));
                member.setNCity(userDetails.get(AccessionConstants.USER_CITY));
			}
            */
			member.setNemail(obj.getString("email"));
			member.setValidated(true);
		}
		//This Exception is raised when the authentication failed
		catch (Exception e)
		{
			//e.printStackTrace();
			member.setValidated(false);
            return null;
		}	
		
		return member;
	}
	
	public boolean loginValid(String userId, String password) {
		Connection conn = null;
		boolean valid = false;
		try {
			conn = AccessionServlet.getCP().newConnection(this.toString());
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from memb where userid='" + userId + "' and password='" + password + "'");
			if (rs.next()) {
				// System.out.println("User login valid :" + userId);
				valid = true;
			}
			rs.close();
			stmt.close();
			conn.commit();
		} catch (SQLException se) {

		} finally {
			AccessionServlet.getCP().freeConnection(conn);
		}
		return valid;
	}

	public static UserManager getInstance() {
		if (mgr == null) {
			mgr = new UserManager();
		}
		return mgr;
	}
}
