package org.sgrp.singer.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.sgrp.singer.AccessionConstants;
import org.sgrp.singer.AccessionServlet;
import org.sgrp.singer.SearchResults;
import org.sgrp.singer.form.AccessionForm;
import org.sgrp.singer.indexer.BaseIndexer;

public class PreviousOrderManager extends DataManager {
	
	static PreviousOrderManager mgr;
	
	
	/**
	 * Return all the orders of the user matching the userId
	 * 
	 * @param userId
	 * @return Map with ordersId as key and the Date of the order as value
	 * @throws SQLException
	 */
	public Map<String, Date> getPreviousOrdersDateByUserId(String userId) throws SQLException
	{
		Connection conn = null;
		LinkedHashMap<String, Date> ordersDate = new LinkedHashMap<String, Date>();
		try {
			conn = AccessionServlet.getCP().newConnection(this.toString());
			Statement stmt = conn.createStatement();
			/*We recover the orders ID in the DB*/
			String sql = "select orderid, odate from orders where userid = \""+userId+"\"";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				String orderId = rs.getString("orderid");
				String oDate = rs.getString("odate");
				
				java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.US);
				
				Date orderingDate = sdf.parse(oDate);
				ordersDate.put(orderId, orderingDate);
			}
			
		}
		catch (SQLException se) 
		{
			ordersDate = null;
			se.printStackTrace();
			throw new SQLException();
		} 
		catch(ParseException pe)
		{
			ordersDate =null;
			pe.printStackTrace();
		}
		finally 
		{		
			AccessionServlet.getCP().freeConnection(conn);
		}
		
		return ordersDate;		
	}
	
	public Map<String, List<String>> getAccessionsByOrder(Collection<String> ordersId)
	{
		LinkedHashMap<String, List<String>> accessionsByOrder = new LinkedHashMap<String, List<String>>();
		try
		{
			Iterator<String> ite = ordersId.iterator();
			while(ite.hasNext())
			{
				String orderId = ite.next();
				List<String> accessions = getAccessionsByOrder(orderId);
				accessionsByOrder.put(orderId, accessions);
			}
		}
		catch (SQLException se) 
		{
			accessionsByOrder = null;
			se.printStackTrace();
		}
		
		return accessionsByOrder;
	}
	
	public List<String> getAccessionsByOrder(String orderId)throws SQLException
	{
		Connection conn=null;
		ArrayList<String> accessions =new ArrayList<String>();
		
		
		try
		{
			conn = AccessionServlet.getCP().newConnection(this.toString());
			Statement stmt = conn.createStatement();
			
			String sql = "select accenumb_, collcode_ from orderitems where orderid =\""+orderId+"\"";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				String accessionNum = rs.getString("accenumb_");
				accessions.add(accessionNum);
			}
		
		}
		catch (SQLException se) 
		{
			accessions = null;
			se.printStackTrace();
			throw new SQLException();
		} 
		finally 
		{
			AccessionServlet.getCP().freeConnection(conn);
		}
		
		return accessions;
	}
	
	public Map<String, Map<String, String>> getAccessionsInfo(Collection<String> accessionsId)
	{
		
		HashMap<String,Map<String,String>> accessionsInfo = new HashMap<String, Map<String,String>>();
		try
		{
			Iterator<String> ite = accessionsId.iterator();
			while(ite.hasNext())
			{
				String accessionId = ite.next();
				Map<String,String> info = getAccessionsInfo(accessionId);
				accessionsInfo.put(accessionId, info);
			}
		}
		catch (SQLException se) 
		{
			accessionsInfo = null;
			se.printStackTrace();
		}
		
		return accessionsInfo;
	}
	
	public Map<String, String> getAccessionsInfo(String accessionId) throws SQLException
	{
		Connection conn=null;
		Map<String,String> accessionInfo = new HashMap<String,String>();
		
		try
		{
			conn = AccessionServlet.getCP().newConnection(this.toString());
			Statement stmt = conn.createStatement();
			
			String sql = "select distinct collcode_ from orderitems where accenumb_ =\""+accessionId+"\"";
			ResultSet rs = stmt.executeQuery(sql);
			
			if(rs.next())
		    {
				String collCode = rs.getString("collcode_");
				
				AccessionForm accForm = SearchResults.getInstance().getAccession(BaseIndexer.mangleAttribName(accessionId), new String[] {"co"+collCode });
				accessionInfo.put("url", "/index.jsp?page=showaccession&"+AccessionConstants.ACCESSIONCODE+AccessionConstants.SPLIT_KEY+accForm.getAcceid()+"&"+AccessionConstants.COLLECTIONCODE+AccessionConstants.SPLIT_KEY+accForm.getCollcode());
				accessionInfo.put("simpleId", accForm.getAccenumb());
				accessionInfo.put("collection", accForm.getCollname());
				accessionInfo.put("genus", AccessionConstants.makeProper(accForm.getGenusname()));
				accessionInfo.put("species",accForm.getSpeciesname().toLowerCase());
				accessionInfo.put("origCountry",accForm.getOrigname());
		    }
		
		
		
		}
		catch (SQLException se) 
		{
			accessionInfo = null;
			se.printStackTrace();
			throw se;
		}
		finally
		{
			AccessionServlet.getCP().freeConnection(conn);
		}
		
		return accessionInfo;
		
	}
	
	public Set<String> getAccessionListFromOrder(Collection<String> ordersId)
	{
		Set<String> accessions = new HashSet<String>();
		try
		{
			Iterator<String> ite = ordersId.iterator();
			while(ite.hasNext())
			{
				String orderId = ite.next();
				Set<String> orderIdAccessions = getAccessionListFromOrder(orderId);
				accessions.addAll(orderIdAccessions);
			}
		}
		catch (SQLException se) 
		{
			accessions = null;
			se.printStackTrace();
		}
		
		return accessions;
		
	}
	
	/**
	 * This method return a Set of accession from an orderId
	 * 
	 * @param orderId
	 * @return a Set of accessions
	 * @throws SQLException
	 */
	
	public Set<String> getAccessionListFromOrder(String orderId) throws SQLException
	{
		Connection conn=null;
		Set<String> accessions = new HashSet<String>();
		
		try
		{
			conn = AccessionServlet.getCP().newConnection(this.toString());
			Statement stmt = conn.createStatement();
			
			String sql = "select accenumb_ from orderitems where orderid =\""+orderId+"\"";
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next())
		    {
				accessions.add(rs.getString("accenumb_"));
		    }
			
		}
		catch (SQLException se) 
		{
			accessions = null;
			se.printStackTrace();
			throw se;
		}
		finally
		{
			AccessionServlet.getCP().freeConnection(conn);
		}
		return accessions;
	}

	public static PreviousOrderManager getInstance() {
		if (mgr == null) {
			mgr = new PreviousOrderManager();
		}
		return mgr;
	}

}
