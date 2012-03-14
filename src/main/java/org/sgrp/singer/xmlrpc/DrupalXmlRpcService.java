package org.sgrp.singer.xmlrpc;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.common.XmlRpcExtensionException;
import org.sgrp.singer.Main;

/**
 * XML-RPC client for the server offered by Drupal.
 * Drupal has a weird authentication scheme on the server side involving to sign every method
 * call using a HMAC signature generated with SHA256 hashing and the API key of Drupal as 
 * key. This class implements this authentication scheme and some method calls based on the 
 * apache XML-RPC implementation.
 * 
 * Usage:
 <pre>
 DrupalXmlRpcService service = new DrupalXmlRpcService("domain", "442c5629267cc4568ad43ceaa7f3dbe4", "http://www.example.com/drupal/?q=services/xmlrpc");
 service.connect();
 service.login(user, password);
 service.nodeSave(mynode);
 service.logout();
 </pre>
 * 
 * see http://drupal.org/node/632844, with adaptations by Leo Sauermann
 * 
 * Changelog 16.2.2010 - made exceptions where exceptions are due, changed logging to JUL
 * 
 * This class is written by Leo Sauermann on the basis of work published by Aaron Moline.
 * 
 * It is currently part of the Aperture sourceforge project which is BSD licensed,
 * if you want to put it elsewhere, do so under this license.
 * 
 * @author Aaron Moline <Aaron.Moline@molinesoftware.com>
 * @author Leo Sauermann <leo.sauermann@dfki.de>
 */
public class DrupalXmlRpcService {
	
	/**
	 * Method names
	 * @author sauermann
	 */
	public static final String MethodNodeSave = "node.save";
	public static final String MethodNodeGet = "node.get";
	public static final String MethodSystemConnect = "system.connect";
	public static final String MethodUserLogout = "user.logout";
	public static final String MethodUserLogin = "user.login";
	public static final String MethodViewsGet = "views.get";
	public static final String MethodFileSave = "file.save";
	public static final String MethodTestCount = "test.count";
	public static final String MethodExternalDocumentUpdate = "externaldocument.update";
	public static final String MethodExternalDocumentDelete = "externaldocument.delete";

	private static final Logger LOG = Logger.getLogger(Main.class);

	final String serviceURL;
	final String serviceDomain;
	final String apiKey;
	/**
	 * SessionID is set by "connect"
	 */
	String sessionID;
	XmlRpcClient xmlRpcClient;

	/**
	 * needed for signing
	 */
	final Charset asciiCs = Charset.forName("US-ASCII");
	
	/**
	 * Message authentification code algorithm already initialized with the ApiKey.
	 */
	Mac apikeyMac;

	/**
	 * Initialize the Drupal XML-RPC Service.
	 * The serviceURL must be a valid URL.
	 * @param serviceDomain domain
	 * @param apiKey the API key with the domain
	 * @param serviceURL the URL of the Drupal service. example: http://www.example.com/drupal/?q=services/xmlrpc
	 * @throws MalformedURLException if the serviceURL is not a URL
	 */
	public DrupalXmlRpcService(String serviceDomain, String apiKey,
			String serviceURL) throws MalformedURLException {
		this.serviceDomain = serviceDomain;
		this.apiKey = apiKey;
		this.serviceURL = serviceURL;
		
		// initialize the xmlRpcClient, it won't change as the parameters are final
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		config.setServerURL(new URL(this.serviceURL));
		config.setEncoding("UTF-8"); //Leo Sauermann: experimental, I had problems with umlauts.
		xmlRpcClient = new XmlRpcClient();
		xmlRpcClient.setConfig(config);
	}


	/**
	 * generate a random string for signing methods
	 * @return
	 */
	private String generateNonce(){
		/*
		 * //TODO:Get None Generator Working String allowedCharacters =
		 * "abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ23456789";
		 * StringBuilder password = new StringBuilder();
		 * Random rand = new Random(); for (int i = 0; i < length; i++) {
		 * password.append() //password.append(append); }
		 * return password.toString();
		 */
		return "" + System.currentTimeMillis();
	}

	/**
	 * Generate the default parameters that need to precede every call
	 * @param methodname pass in the method name which should be signed.
	 * @return the default parameters. The Vector can be reused to pass other objects via XML-RPC, 
	 * hence the resulting vector is typed to "Object" rather than String.
	 * @throws NoSuchAlgorithmException 
	 * @throws IllegalStateException 
	 * @throws InvalidKeyException 
	 */
	private Vector<Object> generateDefaultParams(String methodname) throws InvalidKeyException, IllegalStateException, NoSuchAlgorithmException  {
		String nonce = generateNonce();
		long timestamp = System.currentTimeMillis();
		// Build String for hashing. As this is one line, no StringBuilder is used.
		String hashString = Long.toString(timestamp) + ";" + serviceDomain + ";" + nonce + ";" + methodname;
		String hash = generateHmacHash(hashString);
		
		Vector<Object>  params = new Vector<Object>();
		
		params.add(hash);
		params.add(this.serviceDomain);
		params.add(Long.toString(timestamp));
		params.add(nonce);
		params.add(this.sessionID);
		return params;
	}

	/**
	 * Compute the HMAC-SHA256 hash of the passed message.
	 * As key, the API key is used.
	 * @param message the message to hash
	 * @return the hash as hex-encoded string.
	 * @throws NoSuchAlgorithmException 
	 * @throws IllegalStateException 
	 * @throws InvalidKeyException 
	 * @throws Exception if the encoding algorithm HmacSHA256 can't be found or other problems arise.
	 */
	public String generateHmacHash(String message) throws InvalidKeyException, IllegalStateException, NoSuchAlgorithmException  {
		byte[] hash = getApikeyMac().doFinal(asciiCs.encode(message).array());
		
		String result = "";
		for (int i = 0; i < hash.length; i++) {
			// Leo: I don't understand why Aaron put this here. If its overcomplex, please simplify. I don't care for now.
			result += Integer.toString((hash[i] & 0xff) + 0x100, 16)
					.substring(1);
		}
		LOG.debug("Created HMAC: " + result+ " of "+message);
		return result;
	}
	
	/**
	 * Getter for HMAC, as this is used on every call, its good to buffer it.
	 * As the ApiKey is final, this can be buffered.
	 * @return
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 */
	private Mac getApikeyMac() throws NoSuchAlgorithmException, InvalidKeyException  {
		if (apikeyMac==null)
		{
			SecretKeySpec keySpec = new javax.crypto.spec.SecretKeySpec(asciiCs
					.encode(this.apiKey).array(), "HmacSHA256");
			apikeyMac = javax.crypto.Mac.getInstance("HmacSHA256");
			apikeyMac.init(keySpec);
		}
		return apikeyMac;
	}
	
	/**
	 * Connect to the remote service
	 * 
	 * @return
	 * @throws XmlRpcException 
	 * @throws Exception
	 */
	public void connect() throws XmlRpcException  {
		try {
			Map map = (Map) xmlRpcClient
					.execute(MethodSystemConnect,
							new Object[] {});
			this.sessionID = (String) map.get("sessid");
			LOG.warn("Connected to server using SessionID: " + this.sessionID);
		} catch (Exception x) {
			throw new XmlRpcException("cannot connect to "+serviceURL+": "+x.getMessage(),x);
		}
	}
	
	/**
	 * Get the current Session ID.
	 * This is set during "connect"
	 * @return the session ID or null, if not connected yet. Call connect to get one.
	 */
	public String getSessionID() {
		return this.sessionID;
	}

	/**
	 * Call user.login
	 * @return
	 * @throws NoSuchAlgorithmException 
	 * @throws IllegalStateException 
	 * @throws InvalidKeyException 
	 * @throws XmlRpcException 
	 */
	public Integer login(String username, String password) throws InvalidKeyException, IllegalStateException, NoSuchAlgorithmException, XmlRpcException  {
		Vector<Object> params = generateDefaultParams(MethodUserLogin);
		// Add Login Paramaters
		params.add(username);
		params.add(password);

		Map o = (Map) xmlRpcClient.execute(MethodUserLogin, params);
		// IMPORTANT: the login changes the session id! The new session ID is authorized, the old one not.
		this.sessionID = (String) o.get("sessid");

		LOG.warn("Successfull Login");
		return Integer.valueOf(((Map<String, String>)o.get("user")).get("uid"));
	}

	/**
	 * Call views.get
	 * @return
	 * @throws NoSuchAlgorithmException 
	 * @throws IllegalStateException 
	 * @throws InvalidKeyException 
	 */
	public Object getUserDetails(int n) throws XmlRpcException, InvalidKeyException, IllegalStateException, NoSuchAlgorithmException {
		Vector<Object> params = generateDefaultParams(MethodViewsGet);
		params.add("details_user");
		params.add("feed_1");
		params.add(new Integer[] {n});
		
		Object o =  xmlRpcClient.execute(MethodViewsGet, params);
		
		LOG.warn("Successful call to getUserData");
		
		return o;
	}
	
	/**
	 * Call user.logout
	 * @throws XmlRpcException 
	 * @throws NoSuchAlgorithmException 
	 * @throws IllegalStateException 
	 * @throws InvalidKeyException 
	 */
	public void logout() throws XmlRpcException, InvalidKeyException, IllegalStateException, NoSuchAlgorithmException  {
		Vector<Object> params = generateDefaultParams(MethodUserLogout);
		params.add(this.sessionID);
		xmlRpcClient.execute(MethodUserLogout, params);
		LOG.info("Logout Sucessful");
	}

	/**
	 * Call file.save.
	 * Pass in the file as byte-array.
	 * TODO: Leo says: This does not conform to the Drupal interface of file.save - its a leftover from Aaron's code.
	 */
	public void fileSave(byte[] file) throws Exception {
		Vector<Object> params = generateDefaultParams(MethodFileSave);
		params.add(file);
		Object o = xmlRpcClient.execute(MethodFileSave, params);
		LOG.debug(MethodFileSave+" returned "+o.toString());
	}

	/**
	 * Call node.save
	 * @param node the node to save
	 */
	public int nodeSave(DrupalNode node) throws Exception {
		Vector<Object> params = generateDefaultParams(MethodNodeSave);
		params.add(node);
		Object o;
		try {
			o = xmlRpcClient.execute(MethodNodeSave, params);
		} catch (XmlRpcExtensionException x) {
			checkNullValue(x, node, params);
			throw x;
		}
		LOG.debug(MethodNodeSave+" returned "+o.toString());
		return methodReturnInteger(MethodNodeSave, o);
	}


	/**
	 * In case a NULL value exception was thrown, what caused it?
	 * @param x
	 */
	private void checkNullValue(XmlRpcExtensionException x, DrupalNode node, List params) {
		// was it a null value?
		// stupidity check
		if (node!=null)
			for (Map.Entry<String, Object> entry : node.entrySet())
			{
				if (entry.getValue() == null)
					LOG.error("key "+entry.getKey()+" has a <null> value.");
			}
		if (params!=null)
			for (int i=0; i<params.size(); i++)
				if (params.get(i) == null)
					LOG.error("param "+i+" is a <null> value.");
					
	}
	
	private void checkNullValue(XmlRpcExtensionException x, DrupalNode node) {
		checkNullValue(x, node, null);
	}


	/**
	 * This method call is provided on the server by 
	 * https://organik.opendfki.de/repos/trunk/drupal/contributions/modules/organik_aperture
	 * @param node must have URL and datasource set.
	 * @return the updated nid
	 */
	public int externaldocumentUpdate(DrupalNode node) throws Exception {
		Vector<Object> params = generateDefaultParams(MethodExternalDocumentUpdate);
		params.add(node);
		Object o;
		try {
			o = xmlRpcClient.execute(MethodExternalDocumentUpdate, params);
		} catch (XmlRpcExtensionException x) {
			checkNullValue(x, node, params);
			throw x;
		}
		LOG.debug(MethodExternalDocumentUpdate+" returned "+o.toString());
		return methodReturnInteger(MethodExternalDocumentUpdate, o);
	}
	
	/**
	 * convert a return parameter to integer. the method otherwise was successfull, so say this in the exception
	 * @param methodname the called method name
	 * @param o the returned object
	 * @return o as integer
	 * @throws Exception if the conversion fails
	 */
	private int methodReturnInteger(String methodname,
			Object o) throws Exception {
		if (o instanceof Integer)
			return (Integer)o;
		else if (o instanceof String) 
			try {
				return Integer.parseInt((String)o);
			} catch (NumberFormatException x) {
				throw new Exception(methodname+" completed successfully, but returned not a number but "+o); 
			}
		else {
			if (o == null)
				throw new Exception(methodname+" completed successfully, but returned NULL");
			else
				throw new Exception(methodname+" completed successfully, but returned not an integer but "+o+" of type "+o.getClass().getName());
		}
	}


	/**
	 * This method call is provided on the server by 
	 * https://organik.opendfki.de/repos/trunk/drupal/contributions/modules/organik_aperture
	 * @param uri the uri of the dataobject
	 * @param datasource the datasource of the dataobject to delete
	 * @return the deleted nid
	 */
	public int externaldocumentDelete(String uri, String datasource) throws Exception {
		Vector<Object> params = generateDefaultParams(MethodExternalDocumentDelete);
		params.add(uri);
		params.add(datasource);
		Object o;
		o = xmlRpcClient.execute(MethodExternalDocumentDelete, params);
		LOG.debug(MethodExternalDocumentDelete+" returned "+o.toString());
		return methodReturnInteger(MethodExternalDocumentDelete, o);
	}


	/**
	 * Get the node with the passed nid
	 * @param nid
	 */
	public DrupalNode nodeGet(int nid) throws Exception {
		Vector<Object> params = generateDefaultParams(MethodNodeGet);
		params.add(nid);
		Object o;
		o = xmlRpcClient.execute(MethodNodeGet, params);
		LOG.debug(MethodNodeGet+" returned "+o.toString());
		// result should be a struct
		if (!(o instanceof Map))
			throw new ClassCastException("Cannot convert returned node to a Map: "+o);
		return new DrupalNode((Map)o);
		
	}
}
