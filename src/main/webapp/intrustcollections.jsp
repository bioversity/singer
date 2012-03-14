<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.sgrp.singer.SearchResults"%>
<%@ page import="java.util.Map"%>
<%@page import="java.util.Iterator"%>
<%@page import="org.sgrp.singer.AccessionConstants"%>
<span class="keyTitle"><b>SINGER Intrust collections</b></span>
<br/>
<br/>
<table width="100%">
<tr>
<td class="collHead">Centre</td>
<td class="collHead">Collection</td>
<td class="collHead">Count</td>
</tr>
<%
        int itcount = 0;
        Map<String,Integer> itmap = SearchResults.getInstance().getIntrustCollectionMap();
		Map<String,String> map = SearchResults.getInstance().getKeywordListBySearch("type="+AccessionConstants.INSTITUTE);
		for (Iterator<String> it=map.keySet().iterator(); it.hasNext(); ) {
				String keyid = it.next();
				String keyname = map.get(keyid);
		%>
    
			<%
				Map<String,String> colmap = SearchResults.getInstance().getInstituteCollections(keyid);
          int i=0;
		for (Iterator<String> colit=colmap.keySet().iterator(); colit.hasNext(); ) {
				String colkeyid = colit.next();
				String colkeyname = colmap.get(colkeyid);
                if(itmap.containsKey(colkeyid))
                {
                  
                int itccount = itmap.get(colkeyid);
                if(itccount>0)
                {
                  itcount = itcount+itccount;
				 %>
         <tr>
          <td class="collValue"><b><%=i==0?keyname:""%></b></td>
           <td class="collValue"><%=colkeyname%></td>
           <td align="right" class="collValue"><%=itccount%></td>
         </tr>
			<%
              i =1;
                }
			}
		}
			%>
		<%
		}
		%>
    <%if(itcount>0)
    {
    %>
<tr>
<td class="collValue"><b>Total</b></td>
<td class="collValue">&nbsp;</td>
<td align="right" class="collValue"><b><%=itcount%></b></td>
</tr>
    <%
    }%>
</table>
				
</div>
