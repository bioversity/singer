package org.sgrp.singer.filters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.xerces.parsers.DOMParser;
import org.sgrp.singer.AccessionConstants;
import org.sgrp.singer.Utils;
import org.sgrp.singer.db.UserManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class SingerLoginFilter implements Filter {
	public static int		redirctMCount	= 0;
	public static int		redirectCount	= 0;
	public static final int	refreshGCPer	= 50;

	public static int getRedirctMCount() {
		return redirctMCount;
	}

	public static int getRedirectCount() {
		return redirectCount;
	}

	public static void setRedirctMCount(int redirctMCount) {
		SingerLoginFilter.redirctMCount = redirctMCount;
	}

	public static void setRedirectCount(int redirectCount) {
		SingerLoginFilter.redirectCount = redirectCount;
	}

	public FilterConfig			filterConfig	= null;
	/**
	 * The file containing the redirection mappings.
	 */
	private File				rolesFile		= null;

	/**
	 * Redirection map.
	 */
	private Map<String, String>	rolesMap		= null;

	public void destroy() {
		// TODO Auto-generated method stub

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if ((request instanceof HttpServletRequest) && rolesMap != null) {
			String request_uri = ((HttpServletRequest) request).getRequestURI();
			String query_string = ((HttpServletRequest) request).getQueryString();
			if ((query_string != null) && (query_string.length() > 0)) {
				request_uri += ("?" + query_string);
			}
			//System.out.println("[SingerLoginFilter] request_uri = " + request_uri);
			
			// String roles_uri = request_uri;

			if ((rolesMap != null) && rolesMap.containsKey(request_uri.toLowerCase()) || (request_uri!=null && request_uri.startsWith("/prepareMemberForm.do?"+AccessionConstants.ACTION+"="+AccessionConstants.ACTION_EDIT))) {
				Iterator<String> itr = rolesMap.keySet().iterator();
				while (itr.hasNext()) {
					String page = itr.next();
					// System.out.println("Page Name is :"+page);
					if (request_uri.toLowerCase().startsWith(page)) {
						// System.out.println("[RolesFilter] found match for " +
						// request_uri);
						HttpServletRequest req = (HttpServletRequest) request;
						boolean redirect = true;
						String udata[] = getUserData(req);
						if(udata!=null)
						{
							redirect = false;
							String userid = udata[0];
							if (userid != null) {
								userid = userid.toLowerCase();
							}
							boolean isValid = true;
							/*UserManager.getInstance().loginValid(userid, password);
							
							if(request_uri.startsWith("/prepareMemberForm.do?"+AccessionConstants.ACTION+"="+AccessionConstants.ACTION_EDIT))
							{
								String nuserid = ((HttpServletRequest)request).getParameter("nuserid");
								if(!nuserid.equals(userid))
								{
									isValid = false;
								}
							}*/
							if (!isValid) {
								redirect = true;
							}
						}
						if (redirect) {
							//System.out.println("Redirect to login from SingerLoginFilter");
							((HttpServletResponse) response).sendRedirect("/index.jsp");
							return;
						}
					}
				}
			}
			redirectCount++;
			if (redirectCount % refreshGCPer == 0) {
				// System.out.println("GC Refresh Called");
				System.gc();
				redirctMCount++;
				redirectCount = 0;
			}
		}
		chain.doFilter(request, response);
	}

	public static String[] getUserData(HttpServletRequest req)
	{
		String udata[] = null;
		
		String s = (String) req.getSession(true).getAttribute(AccessionConstants.AUTHORIZATION);
		if (s != null) {
			udata = new String[2];
			String decoded = Utils.b64decode(s.substring(s.lastIndexOf(32) + 1));
			String userid = decoded.substring(0, decoded.indexOf(58));
			String password = decoded.substring(decoded.indexOf(58) + 1);
			udata[0]=userid;
			udata[1]=password;
		}
		return udata;
	}
	
	public void init(FilterConfig filterConfig) {
		String roles_file = filterConfig.getInitParameter("rolesFile");
		if ((roles_file != null) && (roles_file.trim().length() > 0)) {
			rolesFile = new File(filterConfig.getServletContext().getRealPath(roles_file.trim()));
		} else {
			rolesFile = new File("rolesMap.xml");
		}

		loadRolesMap();

		this.filterConfig = filterConfig;
	}

	public Document loadFile(File f) throws Exception {
		Document metadata_doc = null;
		DOMParser parser = null;
		Reader reader = null;
		try {
			parser = new DOMParser();
			// turn off validation
			parser.setFeature("http://xml.org/sax/features/validation", false);

			reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));

			parser.parse(new InputSource(reader));
			metadata_doc = parser.getDocument();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return metadata_doc;
	}

	private void loadRolesMap() {
		if ((rolesFile != null) && rolesFile.exists()) {
			try {
				Document doc = loadFile(rolesFile);
				if (doc != null) {
					Element elem = doc.getDocumentElement();
					NodeList nl = elem.getElementsByTagName("role");
					if ((nl != null) && (nl.getLength() > 0)) {
						rolesMap = new HashMap<String, String>();
						for (int i = 0; i < nl.getLength(); i++) {
							Element rolesElem = (Element) nl.item(i);
							String page = rolesElem.getAttribute("page");
							String type = rolesElem.getAttribute("type");
							System.out.println("Page: " + page + " - type: " + type);
							rolesMap.put(page.toLowerCase(), type);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace(System.out);
			}
		}
	}

}