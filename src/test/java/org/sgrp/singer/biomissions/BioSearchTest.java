package org.sgrp.singer.biomissions;

import java.io.*;
import java.util.ArrayList;
import org.apache.lucene.document.Document; 
import org.apache.lucene.queryParser.ParseException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;

import java.util.Map;
import java.util.Collection;
import java.util.Iterator;

import org.sgrp.singer.AccessionServlet;
import org.sgrp.singer.AccessionConstants;
import org.sgrp.singer.PropertiesManager;

public class BioSearchTest extends TestCase {
    static {
        AccessionServlet.loadInitData("/Users/lucamatteis/bioversity-singer/trunk/src/main/webapp/WEB-INF/singer.properties");
    }

    public void testSearch() throws SQLException, IOException, ParseException{
      /*
        Connection conn = AccessionServlet.openConnection();

        BioSearch b = new BioSearch(conn);
        b.performSearch("cicer");

        conn.close();

        */
    }

    /*
    public void testGetSampleData() throws SQLException{
        Connection conn = AccessionServlet.openConnection();

        BioSearch b = new BioSearch(conn);
        ResultSet rs = b.getSampleData();
        if(rs.next())
            assertTrue(rs.getString("TAXON") != null);
        conn.close();
    }
    public void testGetLucenePath() {
        String lucenePath = PropertiesManager.getString(AccessionConstants.FT_INDEX_ROOT);
        assertTrue(lucenePath != null);
    }

    public void testWriteDataToLuceneFile() throws SQLException, IOException{
        Connection conn = AccessionServlet.openConnection();

        // get data from mysql
        BioSearch b = new BioSearch(conn);
        ResultSet rs = b.getSampleData();

        // write the data
        BioLucene luc = new BioLucene(PropertiesManager.getString(AccessionConstants.FT_INDEX_ROOT) + "/biomissions");
        luc.write(rs);
    }

    public void testReadDataFromLucene() throws SQLException, IOException, ParseException {
        BioLucene luc = new BioLucene(PropertiesManager.getString(AccessionConstants.FT_INDEX_ROOT) + "/biomissions");

        ArrayList<Document> result = luc.read("cicer");
        for(Document d : result) {
            System.out.println(d.get("all"));
        }
    }
    */

}
