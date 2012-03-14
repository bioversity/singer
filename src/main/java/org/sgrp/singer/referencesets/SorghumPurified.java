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

public class SorghumPurified implements SqlDataInterface {

    public String getSql() {
        String sql = "select referencesets.*, accdata.`accenumb` as anumb, accdata.accenumb_, accdata.instname, accdata.collname, accdata.taxname, accdata.origname from referencesets left join accdata on accdata.accenumb = acc_id where ispure = true and crop = 'sorghum' and accdata.taxname like 'Sorghum%'";
        return sql;
    }

    public int getItemsPerPage() {
        return 30;
    }
    public int getNumberOfPages() {
        return 5;
    }


    
}
