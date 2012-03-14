<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ include file="/functions.jsp"%>
<%@ page import="org.sgrp.singer.SearchResults"%>
<%@ page import="org.sgrp.singer.AccessionServlet"%>
<%@ page import="org.sgrp.singer.form.AccessionForm"%>
<%@ page import="java.util.HashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.sgrp.singer.AccessionConstants"%>
<%@page import="org.sgrp.singer.MyShopCart"%>
<%@page import="java.util.Enumeration"%>
<%@page import="org.sgrp.singer.indexer.BaseIndexer"%>
<div><br />
<%
	String prevlink = request.getRequestURI();
	Enumeration enm = request.getParameterNames();
	String queryStr = "";
	while (enm.hasMoreElements()) {
		String param = (String) enm.nextElement();
		if (!(param.equals("caccId") || param.equals("caction") || param.equals("ccollId"))) {
			queryStr = queryStr +"&"+ param + "=" + request.getParameter(param);
		}
	}
	if (queryStr != null && queryStr.trim().length() > 0) {
		prevlink = prevlink + "?" + queryStr;
	}

	if (prevlink.indexOf("?") < 0) {
		prevlink = prevlink + "?";
	}
%> <%
 	MyShopCart shopCart = (MyShopCart) request.getSession(true).getAttribute("mycart");
 	HashMap<String, String> map = shopCart.getCartMap();
 %>
<table width="100%">
  <tr>
    <td class="cartHead" colspan="6" align="center"><b>Shopping Cart</b></td>
  </tr>
  <tr>
    <td class="cartHead" align="center"><img src="/img/add_icon.gif" /></td>
    <td class="cartHead" align="center">Accession</td>
    <td class="cartHead" align="center">Collection</td>
    <td class="cartHead" align="center">Genus</td>
    <td class="cartHead" align="center">Species</td>
    <td class="cartHead" align="center">Country Source</td>
  </tr>
  <%
  	if (map != null && map.size() > 0) {
  		int posstart = 0;
  		String cv = posstart + "";
  		for (Iterator<String> it = map.keySet().iterator(); it.hasNext();) {
  			cv = (posstart % 2 == 0) ? "0" : "1";
  			String caccId = it.next();
  			String ccollId = map.get(caccId);
            String naccId = BaseIndexer.mangleKeywordValue(caccId);
  			AccessionForm accForm = SearchResults.getInstance().getAccession(naccId, new String[] { ccollId });
  			if (accForm != null) {
  %>
  <tr>
    <td align="center" class="cartValue<%=cv%>"><a href="<%=prevlink%>&caction=remove&caccId=<%=caccId%>"><img src="/img/remove_icon.gif" /></a></td>
    <td class="cartValue<%=cv%>"><a
      href="/index.jsp?page=showaccession&<%=AccessionConstants.ACCESSIONCODE+AccessionConstants.SPLIT_KEY+accForm.getAcceid()%>&<%=AccessionConstants.COLLECTIONCODE+AccessionConstants.SPLIT_KEY+accForm.getCollcode()%>"><%=accForm.getAccenumb()%></a></td>
    <td class="cartValue<%=cv%>"><%=accForm.getCollname()%></td>
    <td class="cartValue<%=cv%>"><i><%=AccessionConstants.makeProper(accForm.getGenusname())%></i></td>
    <td class="cartValue<%=cv%>"><i><%=accForm.getSpeciesname().toLowerCase()%></i></td>
    <td class="cartValue<%=cv%>"><%=accForm.getOrigname()%></td>
  </tr>
  <%
  	posstart++;
  			}
  		}
  %>
  <tr>
    <td colspan="6" align="center" class="cartValue0">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="6" align="center" class="cartValue1">
    <input type="button" onclick="javascript:document.location.href='<%=prevlink%>&caction=removeall'" name="removeall" value="Remove All"/> &nbsp;
    <%
 	if (isLoggedIn(request.getSession(true)))
  {
 %>
 <input type="button" onclick="javascript:document.location.href='/index.jsp?page=shopnow'" name="proceed" value="Proceed >>"/>
  <%
 	}
 	else
 	{
 %>
 <b>Login to order now !!</b>
 <%}%>
    </td>
  </tr>

  <%
  	} else {
  %>
  <tr>
    <td colspan="6" align="center" class="cartValue1"><b>No Item(s) selected</b></td>
  </tr>
  <%
  	}
  %>
  
   <tr>
    <td colspan="6" align="left" class="cartValue1"><i><font color="red">* - Maximum of <%=AccessionConstants.getMaxItemsPerOrder()%> accession(s) only are allowed per order</font></i></td>
  </tr>
  <!-- tr><td colspan="5" align="left" class="cartValue">
<font color="red"><b>* - Work in Progress</b></font>
</td></tr-->
</table>
</div>
