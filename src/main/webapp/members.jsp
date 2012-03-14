<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="org.sgrp.singer.SearchResults"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="org.sgrp.singer.AccessionConstants"%>
<%@page import="org.sgrp.singer.form.InstituteForm"%>
<table width="100%">
  <tr>
    <td width="80%" valign="top"><br />
    <br/>
    <span class="keyTitle"><b>SINGER members and contacts</b> </span> <br />
    <br/>
    <table width="100%" class="dispTable">
      <tr>
        <td class="dispHead" nowrap="nowrap">FAO Code</td>
        <td class="dispHead">Acronym</td>
        <td class="dispHead">Name & Address</td>
        <td class="dispHead">Contact & Address</td>
        <td class="dispHead">Email / Link</td>
      </tr>
      <%
      int posstart = 0;
      String cv = posstart+"";
      	Map<String, String> map = SearchResults.getInstance()
      			.getKeywordListBySearch(
      					"type=" + AccessionConstants.INSTITUTE);
      	for (Iterator<String> it = map.keySet().iterator(); it.hasNext();) 
        {
      		cv = (posstart % 2==0)?"0":"1";
      		String keyid = it.next();
          InstituteForm instForm = SearchResults.getInstance().getInstitute(keyid);
      		
      %>
      <tr>
        <td class="dispValue<%=cv%>" valign="top"><%=instForm.getFaocode()%></td>
        <td class="dispValue<%=cv%>" valign="top"><%=instForm.getName()%></td>
        <td class="dispValue<%=cv%>" valign="top"><b><%=instForm.getFullname()%></b><br/><%=instForm.getAddress()%><br/>Phone: <%=instForm.getPhone()%><br/> Fax: <%=instForm.getFax()%></td>
        <td class="dispValue<%=cv%>" valign="top"><b><%=instForm.getContact()%></b><br/><%=instForm.getCaddress()%><br/> Email: <a href="mailto:<%=instForm.getCemail()%>"><%=instForm.getCemail()%></a></td>
        <td class="dispValue<%=cv%>" valign="top">Email: <a href="mailto:<%=instForm.getEmail()%>"><%=instForm.getEmail()%></a><br/> Link: <a target="_new" href="<%=instForm.getUrl()%>"><%=instForm.getUrl()%></a></td>
      </tr>
      <%
      posstart++;
      	}
      %>
    </table>
    </td>
    <td width="20%" valign="top" class="lborder" align="center"><jsp:include page="/logos.jsp" flush="true" /></td>
  </tr>
</table>
