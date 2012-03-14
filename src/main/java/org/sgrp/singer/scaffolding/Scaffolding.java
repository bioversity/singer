package org.sgrp.singer.scaffolding;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

import java.util.ArrayList; 
import java.util.Map; 
import java.util.HashMap; 

import javax.servlet.*;
import javax.servlet.http.*;

import org.sgrp.singer.SqlDataInterface;

/**
 * Scaffolding works with SqlData interfaces to
 * create data layouts with pagination and sorting capabilites.
 */
public class Scaffolding {

    public SqlDataInterface sqlData;
    public Connection conn;
    public String pageNum;
    public String orderColumn;
    public String link;

    public String sql;

    private int pageNumInt;

    public Scaffolding(SqlDataInterface s, Connection c, String pn, String oc, String lnk) {
        sqlData = s;
        conn = c;
        pageNum = pn;
        orderColumn = oc;
        link = lnk;

        if(pageNum == null) pageNum = "1";
        pageNumInt = validatePageNum(pageNum);

        setSql();
        setSqlOrder();
        setSqlLimit();
        setSqlOffset();
    }

    private void setSql() {
        // get the main sql string from our generic object
        sql = sqlData.getSql();
    }
    private void setSqlOrder() {
        if(orderColumn != null) {
            String[] arr = orderColumn.split("\\_");
            if(arr.length == 2 && (arr[1].equals("desc") || arr[1].equals("asc")) ) {
                // XXX fix possible sql injection
                // be sure arr[0] is actually a column that exists
                sql += " order by "+ arr[0]+" "+arr[1];
            }
        }

    }
    private void setSqlLimit() {
        int itemsPerPage = sqlData.getItemsPerPage();
        if(itemsPerPage != 0) {
            sql += " limit "+itemsPerPage;
        }
    }
    

    private void setSqlOffset() {

        int offset = (pageNumInt -1) * sqlData.getItemsPerPage();

        sql += " offset "+offset;
    }

    private int validatePageNum(String pageNumParameter) {
        int pageNum = 1;
        if(pageNumParameter != null) {
            pageNum = Integer.parseInt(pageNumParameter.trim());

            if(pageNum <= 0) pageNum = 1;
        }

        return pageNum;
    }

    /**
     * Returns the result set of the given page
     */
    public ResultSet getCurrentPage() throws SQLException{
        Statement stmt = conn.createStatement();

        ResultSet rs = stmt.executeQuery(sql);

        return rs;

    }

    /**
     * Given the current pageNum it returns an array
     * of pages in the current offset - used for showing
     * the pagination links
     */
    private ArrayList<Integer> getPageLinks() {
        int numberOfPages = sqlData.getNumberOfPages();

        int n = (pageNumInt / numberOfPages);
        Integer startPage = n * numberOfPages;

        ArrayList<Integer> pageLinks = new ArrayList<Integer>();

        // iterate and add to the array
        for(int i=0; i < numberOfPages; i++) {
            pageLinks.add(++startPage); 
        }

        return pageLinks;
        
    }

    private String makePaginationHtml(ArrayList<Integer> pageLinks, String link) {
        String str = "";
        int prev = pageNumInt-10;
        str += "<a class=\"menubarlinks\" href=\""+link+"&pageNum="+prev+"&orderColumn="+orderColumn+"\">&lt; Prev 10</a> ";
        for(Integer i : pageLinks) {
            if(i == pageNumInt)
                str += i + " ";
            else
                str += "<a href=\""+link+"&pageNum="+i+"&orderColumn="+orderColumn+"\">"+ i + "</a> ";
        }
        int next = pageNumInt+10;
        str += " <a class=\"menubarlinks\" href=\""+link+"&pageNum="+next+"&orderColumn="+orderColumn+"\">Next 10 &gt;</a>";

        return str;

    }

    public String getPagination() throws SQLException {
        ArrayList<Integer> pageLinks = getPageLinks();
        String str = makePaginationHtml(pageLinks, link);

        return str;
    }
    private String showDefaultArrow(String columnName) {

        String arrow = "<span style=\"opacity:0.3;\"><a href=\""+link+"&pageNum="+pageNum+"&orderColumn="+columnName+"_asc\"><img src=\"/img/arrow-down.jpg\"></a></span>";

        return arrow;
    }
    public String createSortArrow(String columnName) {
        String sort = "desc";
        if(orderColumn == null) return showDefaultArrow(columnName);

        String[] arr = orderColumn.split("\\_");
        
        // don't show the arrow, we're not sorting by this column
        if(!columnName.equals(arr[0]))
            return showDefaultArrow(columnName);

        if(arr.length == 2) {
            if(arr[1].equals("asc")) {
                sort = "asc"; 
            }             
        }
        
        String arrow = "<a href=\""+link+"&pageNum="+pageNum+"&orderColumn="+columnName+"_";
        if(sort.equals("desc")) {
            arrow += "asc\"><img src=\"/img/arrow-down.jpg\">";
        } else {
            arrow += "desc\"><img src=\"/img/arrow-up.jpg\">";
        }

        arrow += "</a>";
        
        return arrow;
    }

    public ResultSet getAll() throws SQLException{
        Statement stmt = conn.createStatement();

        ResultSet rs = stmt.executeQuery(sqlData.getSql());

        return rs;
    }

    public String downloadAsCsv(String fileName, ResultSet rs, HttpServletResponse response) throws SQLException{
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".csv;");
        
        String ret = "";

        // Get result set meta data
        ResultSetMetaData rsmd = rs.getMetaData();
        int numColumns = rsmd.getColumnCount();

        ret += "Accession Number,";
        ret += "Institute Name,";
        ret += "Collection Name,";
        ret += "Taxon,";
        ret += "Country Source";
        ret += "\n";
        while(rs.next()) { // loop rows
            
            ret += rs.getString("anumb")+",";
            ret += rs.getString("instname")+",";
            ret += rs.getString("collname")+",";
            ret += rs.getString("taxname")+",";
            ret += rs.getString("origname")+"";
            ret += "\n";
        }

        return ret;
    }

}
