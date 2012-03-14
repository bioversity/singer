<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="org.sgrp.singer.referencesets.*"%>
<%@ page import="org.sgrp.singer.scaffolding.*"%>
<%@ page import="org.sgrp.singer.AccessionServlet"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.ResultSet"%>
<%@ page import="java.sql.SQLException"%>
<%@ page import="java.util.ArrayList"%>
<%
// create db connection
Connection conn = AccessionServlet.openConnection();

// which reference set?
String refSetPar = request.getParameter("refSet");
if(refSetPar == null) {
    refSetPar = "chickpea";
}

// reference set
ChickPea refSet = null;
if(refSetPar.equals("chickpea")) {
    refSet = new ChickPea();
} 

Scaffolding s = new Scaffolding(refSet, conn, "1", "0", "0");

String output = request.getParameter("output");
if(output != null && output.equals("csv")) {
    
    String csv = s.downloadAsCsv("chickpea", s.getAll(), response);
    out.println(csv);
}

conn.close();

%>
