<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.Enumeration"  %>
<%@ page import="java.util.List"  %>
<%@ page import="org.sgrp.singer.MyShopCart" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

	<logic:present name="Nrecovered"><br><div style='padding:2px; border:2px solid red; text-align:center;'><b><bean:write name="Nrecovered" /> orders</b> have been recovered from your previous account.</div><br></logic:present>
	<logic:present name="suggestOldOrderRecovery"><br><div style='padding:2px; border:2px solid red; text-align:center;'><b>If you lost previous orders by switching to the new login system, <a href="/index.jsp?page=recoverPreviousOrders">click here to recover them now</a>.</b></div><br></logic:present>
	<logic:empty name="ordersDate"><b>It seems that you have no previous order.</b></logic:empty>
	<logic:notPresent name="ordersDate"><b>You must log in to see them.</b><br /></logic:notPresent>
	<logic:notEmpty name="ordersDate"> 
	
	    <logic:iterate id="order" name="ordersDate">
	        <table width="100%">
	        <bean:define id="orderId" name="order" property="key" />
            <tr>
	            <td class="cartHead" colspan="6" align="center" style="background-color: #D8D8D8">
	               <b>Shopping Cart of the
	               <bean:write name="order" property="value" format="MM/dd/yyyy"  /> 
	              <!--  at <bean:write name="order" property="value" format="HH:mm:ss"  /> -->
	              
	               </b>
	            </td>
            </tr>
            <tr>
                <td class="cartHead" align="center"><img src="/img/add_icon.gif" /></td>
                <td class="cartHead" align="center">Accession</td>
                <td class="cartHead" align="center">Collection</td>
                <td class="cartHead" align="center">Genus</td>
                <td class="cartHead" align="center">Species</td>
                <td class="cartHead" align="center">Country Source</td>
            </tr>
            <logic:iterate name="accessionByOrder" property='<%= (String)orderId %>' id="accession" indexId="index" >
				<bean:define id="accessionInfo" name="accessionInformation" property='<%= (String)accession %>' />               
                <bean:define id="tdClass" value='<%= ""+index%2 %>' />
                <tr>
                	<logic:notPresent name="mycart">
                	    <td class="cartValue<bean:write name="tdClass" />"  align="center"><img src="/img/add_icon.gif" /></td>
                	</logic:notPresent>
                	<logic:present name="mycart">
                	    <bean:define id="mycart" name="mycart" />
                	    <bean:define id="accessionInCart" value='<%= ((MyShopCart) mycart).inCart((String)accession)?"true":"false" %>' />
                	    
                	        <logic:equal name="accessionInCart" value='<%= "true" %>' >
                	    		<td class="cartValue<bean:write name="tdClass" />"  align="center">
                	    		    <a href="/index.jsp?page=seePreviousOrder.do&caction=remove&caccId=<bean:write name="accession" />" title="remove from current ShopCart" >
                	    		        <img src="/img/icon_tick.gif" />
                	    		    </a>
                	    		</td>
                	    	</logic:equal>
                	    	 <logic:equal name="accessionInCart" value='<%= "false" %>' >
                	    		<td class="cartValue<bean:write name="tdClass" />"  align="center">
                	    		    <a href="/index.jsp?page=seePreviousOrder.do&caction=add&caccId=<bean:write name="accession" />" title="add to current ShopCart" >
                	    		        <img src="/img/add_icon.gif" />
                	    		    </a>
                	    		</td>
                	    	</logic:equal>
                	    	
                	    
                	</logic:present >
                    <td class="cartValue<bean:write name="tdClass" />"  align="center"><a href="<bean:write name="accessionInfo" property="url" />"><bean:write name="accessionInfo" property="simpleId" /></a></td>
                    <td class="cartValue<bean:write name="tdClass" />"  align="center"><bean:write name="accessionInfo" property="collection" /></td>
                    <td class="cartValue<bean:write name="tdClass" />"  align="center"><bean:write name="accessionInfo" property="genus" /></td>
                    <td class="cartValue<bean:write name="tdClass" />"  align="center"><bean:write name="accessionInfo" property="species" /></td>
                    <td class="cartValue<bean:write name="tdClass" />"  align="center"><bean:write name="accessionInfo" property="origCountry" /></td>
                </tr>
            </logic:iterate>
            </table>
            <br /><br />
          
         </logic:iterate>
     
	</logic:notEmpty>
</body>
</html>
