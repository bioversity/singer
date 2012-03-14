<%@page import="org.sgrp.singer.xmlrpc.DrupalXmlRpcService,org.apache.xmlrpc.client.XmlRpcClient,org.apache.xmlrpc.client.XmlRpcClientConfigImpl"%>

<%
	//final String serviceURL = "http://localhost:8888/itt/services/xmlrpc";
	final String serviceURL = "http://mls.planttreaty.org/itt/services/xmlrpc";

    DrupalXmlRpcService service = new DrupalXmlRpcService("www.singer.cgiar.org", "HIDDEN_KEY", serviceURL);
    service.connect();
    Integer uid = service.login("HIDDEN_USER", "HIDDEN_PWD");
    out.println(service.getSessionID());
    for (Object o : ((Object[]) service.getUserDetails(uid)))
    {
   		out.println("<hr><pre>");
   		for (Object key : ((java.util.Map) o).keySet())
   			out.println(key + "\t" + ((java.util.Map) o).get(key));
   		out.println("</pre>");
    }
    service.logout();

%>