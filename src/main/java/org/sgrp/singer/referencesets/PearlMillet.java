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
public class PearlMillet implements SqlDataInterface {


    public String getSql() {
        String sql =  "select acc_id as anumb, accdata.`accenumb_`accenumb_, '' as cat, germplasm_id as SampleID, holding_institute as INSTCODE, coll_name as COLLNAME, pearl_millet.genus as taxname, country as origname from pearl_millet left join accdata on accdata.`accenumb` = pearl_millet.acc_id where acc_id != 0 and accdata.taxname like 'Pennisetum%'";
        return sql;
    }

    public int getItemsPerPage() {
        return 30;
    }
    public int getNumberOfPages() {
        return 10;
    }


    
}
