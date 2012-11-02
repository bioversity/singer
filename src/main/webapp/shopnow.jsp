<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ include file="/functions.jsp"%>
<%@ page import="org.sgrp.singer.SearchResults"%>
<%@ page import="org.sgrp.singer.form.AccessionForm"%>
<%@ page import="java.util.HashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="org.sgrp.singer.AccessionConstants"%>
<%@page import="org.sgrp.singer.MyShopCart"%>
<%@page import="java.util.Enumeration"%>
<%@page import="org.sgrp.singer.indexer.BaseIndexer"%>
<%@page import="org.sgrp.singer.db.OrderEmailManager"%>
<div>
<%
	String prevlink = request.getRequestURI();
	Enumeration enm = request.getParameterNames();
	String queryStr = "";
	while (enm.hasMoreElements()) {
		String param = (String) enm.nextElement();
		if (!(param.equals("caccId") || param.equals("caction") || param.equals("ccollId"))) {
			queryStr = queryStr + param + "=" + request.getParameter(param) + "&";
		}
	}
	if (queryStr != null && queryStr.trim().length() > 0) {
		prevlink = prevlink + "?" + queryStr.trim().substring(0, queryStr.trim().length() - 1) + "&";
	}

	if (prevlink.indexOf("?") < 0) {
		prevlink = prevlink + "?";
	}
%> <%
if (isLoggedIn(request.getSession(true))) 
{
	  MyShopCart shopCart = (MyShopCart) request.getSession(true).getAttribute("mycart");
	  HashMap<String, String> map = shopCart.getCartMap();

// Here is the payment gateway logic
  String doshop = request.getParameter("doshop");
  if(doshop!=null && doshop.equalsIgnoreCase("yes"))
  {
	  String iagree = request.getParameter("iagree");
	  String smta = request.getParameter("smta");
	  String specialComments = request.getParameter("specialComments");
	  if(iagree!=null && iagree.equalsIgnoreCase("yes")&& smta!=null && !smta.equalsIgnoreCase("no"))
	  { 
      // here happens the order stuff that just adds the items to the database
      // inside orders and orderitems
      int orderid = shopCart.processOrder(getUserID(request.getSession(true)));
      //int orderid=100;
      System.out.println("orderid "+orderid);
      if(orderid!=0)
      {
        // once the order is added in the database
        // we send the email to the genebank with data
        request.getSession(true).removeAttribute("mycart");
        OrderEmailManager.getInstance().processMapToEmail(orderid,request.getSession(true), map, specialComments, smta);
%> <br />
<br />
<br />
<b> Hello, thank your for Ordering through SINGER.<br />
Your order will be forwarded to the respective institutes for further processing.<br />
You will be directly contacted by the institute if and for any other information required.<br />
A confirmation email of the ordered material will be shortly sent to your email account. </b> <br />
<br />
<br />
<%
      }
    else
    {
%> <b> A problem was encountered while processing your order.<br />
This might be due to an temporary internal error. <br />
Please try re-ordering after some time. <br />
Thanks !!</b> <%
    }
      }
	  else
	  {
%> <br />
<br />
<br />
<b> You need to agree the terms and conditions before proceeding with Ordering.<br />
<a href="/index.jsp?page=shopnow">Return back</a> to accept the terms and conditions. </b> <br />
<br />
<br />
<%
      }
}
  else
  {
  %> <%
    if(map.size()>0)
	  {
  %> <script type="text/javascript">
    function AuthorizeValidation(myform) {
	var smta="";
	for(var i=0; i<myform.smta.length; i++)
	{
		 if(myform.smta[i].checked)
		 {
			 smta=myform.smta[i].value;
		 }
	}

    if(myform.iagree.checked == true && smta!="no") {myform.validation.disabled = false }
    else {myform.validation.disabled = true }
    }
</script>
<form action="/index.jsp" method="post" name="ordermyform"><input type="hidden" name="page" value="shopnow" /> <input type="hidden" name="doshop" value="yes" />
<table width="100%">
  <tr>
    <td colspan="5" class="cartHead" align="center"><b>Order Acceptance</b></td>
  </tr>
  <tr>
    <td colspan="5" class="cartValue0">
    <jsp:include page="/disclaimer.jsp" flush="true"/>
    <!-- br/>
    <br/>
    <center><b>Please read the carefully the <a href="/index.jsp?page=disclaimer">disclaimer</a> before accepting.</b></center>
    <br />
    <br /-->
    <b>Items selected to order </b><br />
    </td>
  </tr>
  <tr>
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
    <td class="cartValue<%=cv%>"><%=accForm.getAccenumb()%></td>
    <td class="cartValue<%=cv%>"><%=accForm.getCollname()%></td>
    <td class="cartValue<%=cv%>"><i><%=AccessionConstants.makeProper(accForm.getGenusname())%></i></td>
    <td class="cartValue<%=cv%>"><i><%=accForm.getSpeciesname().toLowerCase()%></i></td>
    <td class="cartValue<%=cv%>"><%=accForm.getOrigname()%></td>
  </tr>
  <%
    posstart++;
        }
      }
      }
  %>
  <tr>
    <td colspan="5" class="cartValue0">&nbsp;</td>
  </tr>
  <tr>
  	<td colspan="5"  class="cartValue1">
  		What is the purpose of your request? Do you have any special comments for the provider(s)?
  	</td>
  </tr>
  <tr>
    <td colspan="5" class="cartValue0">
    	<textarea name="specialComments" cols="50" rows="5">Not filled</textarea>
    </td>
  </tr>
  <tr>
	<td colspan="5" class="cartHead">
	  		SMTA Prior Notification (You can read SMTA <a href="/index.jsp?page=smta" target="smta">here</a>)
	</td>
  </tr>
  <tr>
	<td colspan="5" class="cartValue1">
	  		<input type="radio" value="no" name="smta" checked="true" onClick="AuthorizeValidation(this.form)"/>
	  		I will not accept the SMTA
	</td>
  </tr>
  <tr>
	<td colspan="5" class="cartValue0">
	  		<input type="radio" value="shrinkwrapped" name="smta" onClick="AuthorizeValidation(this.form)"/> 
	  		I hereby give advance notification that I have read the terms and conditions of the SMTA and that I intend to accept these terms and conditions on receipt of the seed with shrink-wrap SMTA.
	</td>
  </tr>
  <tr>
	<td colspan="5" class="cartValue1">
	  		<input type="radio" value="signedcopy" name="smta" onClick="AuthorizeValidation(this.form)"/> 
	  		I or my organization requires a signed version of the SMTA. I understand that a signed printed copy of the SMTA will be sent to me and must be returned to the Provider before the material can be shipped to me.
	</td>
  </tr>
  <tr>
    <td colspan="5" class="cartValue0">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="5" align="center" class="cartValue1"><input type="checkbox" name="iagree" value="yes" onClick="AuthorizeValidation(this.form)" />I agree to terms and
    conditions as per the disclaimer</td>
  </tr>
  <tr>
    <td colspan="5" align="center" class="cartValue0"><input type="button" onclick="javascript:document.location.href='/index.jsp?page=showcart'" name="back"
      value="Back to Shopping cart" />&nbsp; <input type="submit" name="validation" value="Order Now" disabled="disabled" /></td>
  </tr>
</table>
</form>
<%
  }
  else
  {
 %> <b>No items in shopping cart. Please proceed with selection using the options available in the sidebar !!</b> <%} %> <%
  }
  }
  else
  {
 %> <b>User login invalid . Please login to proceed !!</b> <br />
<br />
<a href="/index.jsp?page=showcart">Click here</a> to return back to Shopping Cart. <%}%>
</div>
