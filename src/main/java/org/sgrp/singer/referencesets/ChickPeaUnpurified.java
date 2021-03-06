package org.sgrp.singer.referencesets;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

import java.util.ArrayList; 
import java.util.Map; 
import java.util.HashMap; 

// this has some DB api
import org.sgrp.singer.AccessionServlet;

import org.sgrp.singer.SqlDataInterface;

/**
 * It's just a data container for our SQL query. 
 * Implements the SqlData interface
 */
public class ChickPeaUnpurified implements SqlDataInterface {

    public String getSql() {
        String sql = "select referencesets.*, accdata.`accenumb` as anumb, accdata.accenumb_, accdata.instname, accdata.collname, accdata.taxname, accdata.origname from referencesets left join accdata on accdata.accenumb = acc_id where accdata.`taxname` like 'Cicer%' and ispure = false and crop = 'chickpea'";
        return sql;
    }

    public int getItemsPerPage() {
        return 30;
    }
    public int getNumberOfPages() {
        return 10;
    }


    
}
