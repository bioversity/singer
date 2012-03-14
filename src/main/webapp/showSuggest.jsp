<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="org.sgrp.singer.SearchResults"%>
<%=SearchResults.getInstance().getSuggestString(request.getParameter("type"),request.getParameter("search"))%>