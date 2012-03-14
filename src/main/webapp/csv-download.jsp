<%@page trimDirectiveWhitespaces="true"%>
<%@ page import="org.sgrp.singer.biomissions.SubMissions"%>
<%@ page import="org.sgrp.singer.AccessionServlet"%>
<%@ page import="java.sql.*"%>
<%@ page import="java.util.ArrayList"%><%
// create db connection
Connection conn = AccessionServlet.openConnection();

String[] acceNumbs = request.getParameterValues("acceNumbs");

//select * from accdata where `accenumb` in ('WAB0007370','WAB0007291')
//
String query =  "select * from accdata where `accenumb` in (";

for(int i=0; i<acceNumbs.length; i++) {
    if(i > 0)
        query += ",";

    query += "'"+acceNumbs[i]+"'";
}
query += ");";

// i know, sql exceptions
Statement stmt = conn.createStatement();
ResultSet rs = stmt.executeQuery(query);

response.setContentType("text/csv");
response.setHeader("Content-Disposition", "attachment; filename=SINGER-order.csv;");
%>Accession Number,Accession Name,Institute Name,Genus,Species
<%
while(rs.next()) {
    out.print(rs.getString("accenumb") + ",");
    out.print(rs.getString("accename") + ",");
    out.print(rs.getString("instname") + ",");
    out.print(rs.getString("genus") + ",");
    out.print(rs.getString("species"));
    out.println("");
}
conn.close();
%>
