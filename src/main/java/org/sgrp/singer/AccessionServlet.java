package org.sgrp.singer;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.xml.serialize.DOMSerializer;
import org.apache.xml.serialize.Method;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.sgrp.singer.db.ConnectionPool;
import org.sgrp.singer.indexer.Keywords;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@SuppressWarnings("serial")
public class AccessionServlet extends org.apache.struts.action.ActionServlet {

	static ConnectionPool	cp			= null;

	static Keywords			keywords	= null;
		

	public static ConnectionPool getCP() {
		try {
			if (cp == null) {

				String jdbcDriver = PropertiesManager.getString(AccessionConstants.JDBC_DRIVER);
				String jdbcUser = PropertiesManager.getString(AccessionConstants.JDBC_USER);
				String jdbcPassword = PropertiesManager.getString(AccessionConstants.JDBC_PASSWORD);
				String jdbcConStr = PropertiesManager.getString(AccessionConstants.JDBC_CONNECT_STRING);
				System.out.println("JDBC Driver = " + jdbcDriver);
				if (jdbcUser != null) {
					cp = new ConnectionPool(jdbcConStr, jdbcDriver, jdbcUser, jdbcPassword);
				} else {
					cp = new ConnectionPool(jdbcConStr, jdbcDriver);
				}
			}
		} catch (SQLException se) {
			System.out.println("SQL Exception caused in " + se.toString());
		}
		return cp;
	}

    /**
     * It's a wrapper on top of native Connection
     * to estabilish a connection given what's been already loaded from the 
     * singer.properties file
     */
    public static Connection openConnection() {
        Connection conn = null;

		String jdbcDriver = PropertiesManager.getString(AccessionConstants.JDBC_DRIVER);
        // connection string
        String jdbcConStr = PropertiesManager.getString(AccessionConstants.JDBC_CONNECT_STRING);

        try {
            Class.forName(jdbcDriver);
            conn = DriverManager.getConnection(jdbcConStr);
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return conn;
    }

	public static Keywords getKeywords() {
		return keywords;
	}

	public static void loadInitData(String propFile) {
		System.setProperty("disableLuceneLocks", "true");
		try {
			PropertiesManager.loadProperties(propFile);
			PropertiesManager.loadProperties(PropertiesManager.getString(AccessionConstants.SQL_STATEMENTS));
		} catch (Exception e) {
			System.out.println("Error while loading properties :" + e.toString());
		}
		System.out.println("Finished loading properties");
		keywords = Keywords.getInstance();
	}

	public static Node replaceTextInNode(Node n, String value) {
		Node newTextNode = (n.getOwnerDocument()).createTextNode("text");
		newTextNode.setNodeValue(value == null ? "" : value);
		boolean replaced = false;
		if (n.hasChildNodes()) {
			NodeList nl = n.getChildNodes();
			for (int i = 0; i < nl.getLength(); i++) {
				Node tn = nl.item(i);
				if (tn.getNodeType() == Node.TEXT_NODE) {
					n.replaceChild(newTextNode, tn);
					replaced = true;
					break;
				}
			}
		}
		if (!replaced) {
			n.appendChild(newTextNode);
		}
		return n;
	}

	public static void saveXML(Document meta_dom, String fileName) throws Exception {
		Writer writer = null;
		OutputFormat outputformat = null;
		DOMSerializer hammer = null;
		try {
			if (meta_dom == null) { throw new Exception("[KO.storeMetaData] called with null meta_dom parameter"); }
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(fileName)), "UTF-8"));
			outputformat = new OutputFormat(Method.XML, "UTF-8", false);
			hammer = new XMLSerializer(writer, outputformat);
			hammer.serialize(meta_dom);
		} catch (IOException e) {
			throw new Exception(e);
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
			}
		}
	} // of storeMetaData

	public static void writetoStream(HttpServletResponse response, File tempFile) {
		ServletOutputStream sout = null;
		try {
			sout = response.getOutputStream();
			byte b[] = new byte[4096];
			BufferedInputStream in = null;
			try {
				in = new BufferedInputStream(new FileInputStream(tempFile));
				response.setContentLength((int) tempFile.length());

				int size = 0;
				while (true) {
					size = in.read(b, 0, 4096);
					if (size == -1) {
						break;
					}
					sout.write(b, 0, size);
				}
				sout.flush();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (in != null) {
						in.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (sout != null) {
				try {
					sout.flush();
					sout.close();
				} catch (Exception e) {
				}
			}

		}
	}

	protected boolean	loaded	= false;

	public AccessionServlet() {
		super();
	}

	@Override
	public void destroy() {
		System.out.println("Shutdown of SINGER @ " + (new Date()).toString());
	}

	@Override
	public void init() throws ServletException {
		super.init();
		System.setProperty("disableLuceneLocks", "true");
		System.out.println("Startup of SINGER @ " + (new Date()).toString());
		if (!loaded) {
			String propFile = AccessionConstants.getPropertiesFile(this);
			loadInitData(getServletConfig().getServletContext().getRealPath(propFile));
			loaded = true;
		}
	}
}
