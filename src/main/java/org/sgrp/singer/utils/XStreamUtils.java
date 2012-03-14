package org.sgrp.singer.utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XStreamUtils {
	public static XStream	domXstream;
	public static XStream	jsonXstream;

	static {
		domXstream = new XStream(new DomDriver());
		jsonXstream = new XStream(new JsonHierarchicalStreamDriver());
		domXstream.setMode(XStream.NO_REFERENCES);
		jsonXstream.setMode(XStream.NO_REFERENCES);
		domXstream.alias("StringComparableValues", org.sgrp.singer.StringComparableValues.class);
		domXstream.alias("CompTermInfo", org.sgrp.singer.indexer.CompTermInfo.class);
		domXstream.alias("marker", org.sgrp.singer.Marker.class);
		/*Added by Gautier for save of SearchHistory in a File*/
		domXstream.alias("MySearchHistory", org.sgrp.singer.MySearchHistory.class);
		/*End Added By Gautier*/
	}

	public static Object getObjectFromFile(File file) throws Exception {
		Object obj = null;
		FileReader reader = new FileReader(file);
		try {
			obj = getObjectFromReader(reader);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	public static Object getObjectFromFile(String file) throws Exception {
		return getObjectFromFile(new File(file));
	}

	public static Object getObjectFromReader(Reader reader) throws Exception {
		Object obj = null;
		try {
			obj = domXstream.fromXML(reader);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	public static Object getObjectFromXMLString(String xml) throws Exception {
		Object obj = null;
		try {
			obj = domXstream.fromXML(xml);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	@SuppressWarnings("unchecked")
	public static Object getObjectFromXMLString(String xml, String alias, Class className) throws Exception {
		Object obj = null;
		try {
			domXstream.alias(alias, className);
			obj = domXstream.fromXML(xml);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	public static String getXMLfromObject(Object obj) throws Exception {
		return domXstream.toXML(obj);
	}

	/*
	 * public static Object getObjectFromXMLStringAttributes(String xml, String alias, Class className) throws Exception{ Object obj = null; try { xstream.alias(alias, className);
	 * xstream.useAttributeFor(int.class); xstream.useAttributeFor(String.class); xstream.useAttributeFor(boolean.class); obj = xstream.fromXML(xml); } catch (Exception e) { e.printStackTrace(); }
	 * return obj; }
	 */

	public static boolean saveMarkerObjectToFile(File file, Object obj) throws Exception {
		boolean fileSaved = false;
		FileWriter writer = null;
		try {
			jsonXstream.alias("marker", org.sgrp.singer.Marker.class);
			writer = new FileWriter(file);
			jsonXstream.toXML(obj, writer);
			fileSaved = true;
			writer.close();
		} catch (Exception e) {
			fileSaved = false;
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
		return fileSaved;
	}

	public static boolean saveObjectToFile(File file, Object obj) throws Exception {
		boolean fileSaved = false;
		FileWriter writer = null;
		try {
			// System.out.println("*** In save file");
			

			writer = new FileWriter(file);
			domXstream.toXML(obj, writer);
			fileSaved = true;
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
			fileSaved = false;
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
		// System.out.println("**** File saved"+fileSaved);
		return fileSaved;
	}

	/*
	 * public static Element getElementfromFile(Document domDoc, File file) throws Exception { return getElementfromObject(domDoc, getObjectFromXML(file)); } public static Element
	 * getElementfromObject(Document domDoc, Object obj) throws Exception { String xmlStr = getXMLfromObject(obj); Document doc = null; try { if (xmlStr.length() > 0) { doc = parseXMLReader(new
	 * StringReader(xmlStr)); } } catch (Exception e) { throw new Exception(e); } return ((doc != null) ? importNode(domDoc, doc) : null); }
	 */

	/*
	 * public static Element importNode(Document dom_doc, Document source) { Element dom_root = null; dom_root = (Element) dom_doc.importNode(source.getDocumentElement(), true); //dom_root =
	 * importNodeAGT(dom_doc, source); return (dom_root); } public static Document parseXMLReader(Reader reader) throws SAXException, IOException, ParserConfigurationException { Document document =
	 * null; DOMParser parser; parser = new DOMParser(); //parser.setFeature("http://xml.org/sax/features/validation", true); parser.parse(new InputSource(reader)); document = parser.getDocument();
	 * return document; }
	 */
}
