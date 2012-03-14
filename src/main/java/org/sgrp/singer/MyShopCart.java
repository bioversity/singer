package org.sgrp.singer;

import java.util.HashMap;

import org.sgrp.singer.db.ShoppingCartManager;

public class MyShopCart {

	public HashMap<String, String>	cartMap	= new HashMap<String, String>();

	public void addToCart(String oaccId, String collName) {
		if(canAdd())
		{
			cartMap.put(oaccId, collName);
		}
	}

	public HashMap<String, String> getCartMap() {
		return cartMap;
	}

	public boolean inCart(String oaccId) {
		// System.out.println("Call in cart with id :"+accId+"
		// "+cartMap.size());
		return cartMap.containsKey(oaccId);
	}

	public void removeAll() {
		cartMap.clear();
	}

	public void removeFromCart(String oaccId) {
		cartMap.remove(oaccId);
	}
	public boolean canAdd()
	{
		return cartMap.size()<AccessionConstants.getMaxItemsPerOrder();
	}
	
	public int processOrder(String userid)
	{
		int orderid = 0;
		try
		{
			orderid = ShoppingCartManager.getInstance().saveCart(userid, cartMap);
		}
		catch (Exception e) {
			orderid = 0;
			e.printStackTrace(System.out);
			// TODO: handle exception
		}
		return orderid;
	}
}
