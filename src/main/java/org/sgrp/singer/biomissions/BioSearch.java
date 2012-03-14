package org.sgrp.singer.biomissions;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

import java.io.*;
import org.apache.lucene.document.Document; 
import org.apache.lucene.queryParser.ParseException;

import java.util.ArrayList; 
import java.util.Map; 
import java.util.HashMap; 

// this has some DB api
import org.sgrp.singer.AccessionServlet;
import org.sgrp.singer.AccessionConstants;
import org.sgrp.singer.PropertiesManager;

import java.util.ArrayList;                                                                                                                                                                                   

public class BioSearch {
    public Connection conn;

    public BioSearch(Connection c) {
        conn = c;
    
    }

    /**
     * gets generic sample data so that we can stuff it
     * in a Lucene index file
     */
    public ResultSet getSampleData() throws SQLException{
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select * from samples group by taxon");
        return rs;
    }

    /**
     * checks if index exists and creates it otherwise
     * and performs a search on the index
     */
    public ArrayList<Document> performSearch(String queryStr) throws SQLException, IOException, ParseException {
        BioLucene luc = new BioLucene(PropertiesManager.getString(AccessionConstants.FT_INDEX_ROOT) + "/biomissions");

        if(!luc.indexExists()) { // if index doesn't exist, re-run query and create index
            ResultSet rs = getSampleData();
            luc.write(rs);
        }

        // do search
        ArrayList<Document> result = luc.read(queryStr);
        return result;
    }


}
