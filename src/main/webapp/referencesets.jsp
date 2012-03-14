<%@page trimDirectiveWhitespaces="true"%>
<%@ page import="org.sgrp.singer.referencesets.*"%>
<%@ page import="org.sgrp.singer.SqlDataInterface"%>
<%@ page import="org.sgrp.singer.scaffolding.*"%>
<%@ page import="org.sgrp.singer.AccessionServlet"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.ResultSet"%>
<%@ page import="java.sql.SQLException"%>
<%@ page import="java.util.ArrayList"%>
<%@page import="org.sgrp.singer.MyShopCart"%>
<%

// create db connection
Connection conn = AccessionServlet.openConnection();

// which reference set?
String refSetPar = request.getParameter("refSet");
if(refSetPar == null) {
    refSetPar = "chickpea";
%>
    <h2>Reference Sets</h2>
    <ul>
        <li><a href="/index.jsp?page=referencesets&refSet=chickpea">Chickpea</a></li>
        <li><a href="/index.jsp?page=referencesets&refSet=musa">Musa</a></li>
        <li><a href="/index.jsp?page=referencesets&refSet=sorghum">Sorghum</a></li>
        <!-- <li><a href="/index.jsp?page=referencesets&refSet=pearl_millet">Pearl Millet</a></li> -->
    </ul>
<%
        
    return;
}

// reference set
SqlDataInterface refSet = null;
if(refSetPar.equals("chickpea")) {
    refSet = new ChickPeaPurified();
} else if(refSetPar.equals("chickpea_unpurified")) {
    refSet = new ChickPeaUnpurified();
} else if(refSetPar.equals("musa")) {
    refSet = new MusaPurified();
} else if(refSetPar.equals("musa_unpurified")) {
    refSet = new MusaUnpurified();
} else if(refSetPar.equals("sorghum")) {
    refSet = new SorghumPurified();
}

// shopping cart
MyShopCart shopCart = (MyShopCart) request.getSession(true).getAttribute("mycart");

String pageNum = request.getParameter("pageNum");
String orderColumn = request.getParameter("orderColumn");
String link = "index.jsp?page=referencesets&refSet="+refSetPar;

Scaffolding s = new Scaffolding(refSet, conn, pageNum, orderColumn, link);

// CSV download
String output = request.getParameter("output");
if(output != null && output.equals("csv")) {
    String csv = s.downloadAsCsv(refSetPar, s.getAll(), response);
    out.println(csv);

    conn.close();
    return;
}

// order all
String orderall = request.getParameter("orderall");
if(orderall != null) {
    ResultSet allset = s.getAll();

    if(orderall.equals("true")) { // add all
        while(allset.next()) { 
            shopCart.addToCart(allset.getString("accenumb_"), null);
        }
    } else if (orderall.equals("false")) { // remove all
        while(allset.next()) { 
			shopCart.removeFromCart(allset.getString("accenumb_"));
        }
    } 

    conn.close();
    return;
}

ResultSet curr = s.getCurrentPage();

%>
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script>
<script type="text/javascript" src="http://vadikom.com/demos/poshytip/src/jquery.poshytip.js"></script> 
<link href="http://vadikom.com/demos/poshytip/src/tip-twitter/tip-twitter.css" media="screen" rel="stylesheet" type="text/css" /> 
<script>
// some js for the shopping cart
function shop(elem) {
    var jelem = $(elem);
    $.ajax({
      url: jelem.attr("href"),
      success: function(data) {
            // reload so user sees the added item
            window.location.href=window.location.href
      }
    });

}

$(function(){

   $('.poshytip').poshytip({
        className: 'tip-twitter',
        showTimeout: 1,
        alignTo: 'target',
        alignX: 'center',
        offsetY: 5,
        allowTipHover: false,
        fade: false,
        slide: false
    });

});

</script>

<% if(refSetPar.equals("chickpea")) { //purified %>
    <h2>Core Reference Set from Chickpea (purified)</h2>
    <b><a href="index.jsp?page=referencesets&refSet=chickpea_unpurified">Core Sample Collection (unpurified)</a></b>

<% } else if(refSetPar.equals("chickpea_unpurified")) {  %>
    <h2>Core Sample Collection from Chickpea (unpurified)</h2>
    <b><a href="index.jsp?page=referencesets&refSet=chickpea">Core Reference Set (purified)</a></b>
    <br>
    <br>
    <b><%=s.getPagination()%></b>

<% } else if(refSetPar.equals("musa")) {  %>
    <h2>Core Reference Set from Musa (purified)</h2>
    <b><a href="index.jsp?page=referencesets&refSet=musa_unpurified">Core Sample Collection (unpurified)</a></b>
    <br>
    <br>
<% } else if(refSetPar.equals("musa_unpurified")) {  %>
    <h2>Core Sample Collection from Musa (unpurified)</h2>
    <b><a href="index.jsp?page=referencesets&refSet=musa">Core Reference Set (purified)</a></b>
    <br>
    <br>
<% } else if(refSetPar.equals("sorghum")) {  %>
    <h2>Core Reference Set from Sorghum</h2>
    <br>
    <br>
    <b><%=s.getPagination()%></b>
<% } else { %>
    <b><%=s.getPagination()%></b>
<% } %>

    <div style="float: right;">
    <% 
    String wall = "";
    if(refSetPar.startsWith("musa"))
        wall = "musa";
    else if (refSetPar.startsWith("chickpea"))
        wall = "chickpea";
    %>
        <a style="font-size: 20px;" href="index.jsp?page=wall&refSet=<%=wall%>"><img src="http://enperublog.com/wp-content/themes/enperutwo/images/comment_icon.gif" /> View interactive Wall</a>
    </div>

    <br>
    <br>

          <table width="100%">
            <tbody>
              <tr>
                  <td align="right" width="100%" colspan="8" class="dispHead">Download all results in 
                        <a href="/referencesets.jsp?refSet=<%=refSetPar%>&output=csv" target="output">csv</a> format(s)
                  </td>
              </tr>
              <tr>
                <td class="collHead" style="width: 50px;">Accession Number <%=s.createSortArrow("anumb")%></td>
<% if (refSetPar.startsWith("musa")) {%>
                <td class="collHead" style="width: 50px;">SSR Data</td>
<% } %>

<% if(refSetPar.equals("chickpea") || refSetPar.equals("musa")) { //purified %>
                <td class="collHead">Nucleus  <%=s.createSortArrow("isnucleus")%></td>
<% } else if (refSetPar.equals("chickpea_unpurified") || refSetPar.equals("musa_unpurified")) {%>
                <td class="collHead">Minicore  <%=s.createSortArrow("isminicore")%></td>
<% } %>
                <td class="collHead">Institute Name  <%=s.createSortArrow("instname")%></td>

                <td class="collHead">Collection Name <%=s.createSortArrow("collname")%> </td>

                <td class="collHead">Taxon  <%=s.createSortArrow("taxname")%></td>

                <td class="collHead">Country Source <%=s.createSortArrow("origname")%> </td>


				<td class="collHead poshytip" width="1%" align="center" title="Adding the entire set to shopping cart creates some performance issue. It has been disabled for the prototype version">
                    <!--<a href="/referencesets.jsp?refSet=<%=refSetPar%>&orderall=true"><img src="/img/add_icon.gif" /></a>-->
                    <img src="/img/add_icon.gif" />
                </td>

              </tr>

<%
    while(curr.next()) {
%>

              <tr>
                <td class="collValue">
                    <a href="/index.jsp?page=showaccession&acc=<%=curr.getString("accenumb_").replace("_","").replace(" ","").toLowerCase()%>"><%=curr.getString("anumb")%></a>
                </td>
<% if (refSetPar.startsWith("musa")) {%>
                <td class="collValue">
                    <a target="_blank" href="http://gohelle.cirad.fr:8080/SagacityWS/accessionData.jsp?crop=Musa&acc=<%=curr.getString("anumb")%>">Link</a>
                </td>
<% } %>
<% if(refSetPar.equals("chickpea") || refSetPar.equals("musa")) { //purified %>
                <td class="collValue">
                    <%=(curr.getBoolean("isnucleus") ? "Yes" : "")%>
                </td>
<% } else if (refSetPar.equals("chickpea_unpurified") || refSetPar.equals("musa_unpurified")) {%>
                <td class="collValue">
                    <%=(curr.getBoolean("isminicore") || refSetPar.equals("musa_unpurified") ? "Yes" : "")%>
                </td>
<% } %>

                <td class="collValue">
                    <%=curr.getString("instname")%>
                </td>

                <td class="collValue">
                    <%=curr.getString("collname")%>

                </td>
                <td class="collValue">
                    <%=curr.getString("taxname")%>
                </td>

                <td class="collValue">
                    <%=curr.getString("origname")%>

                </td>
                <td class="collValue">
<%
boolean inCart = false;
String okeyid = curr.getString("accenumb_");
if(shopCart!=null) {
    inCart =shopCart.inCart(okeyid);
}
if (inCart) {
%> 
            <a href="/index.jsp?page=disppage&caction=remove&caccId=<%=okeyid%>" onclick="shop(this);return false;"><img src="/img/icon_tick.gif" /></a> 
<%
} else if(shopCart.canAdd()){
%> 

    <% if(refSetPar.equals("chickpea")) { %>
            <a class="poshytip" title="WARNING: We strongly suggest to order the entire purified set" href="/index.jsp?page=disppage&caction=add&caccId=<%=okeyid%>" onclick="shop(this); return false;"><img src="/img/add_icon.gif" /></a> 
    <% } else { %>
            <a  href="/index.jsp?page=disppage&caction=add&caccId=<%=okeyid%>" onclick="shop(this); return false;"><img src="/img/add_icon.gif" /></a> 
    <% } %>

<%
} else {
%> 
            <img src="/img/full_icon.gif" title="Cart already full"/>
<% } %>

                </td>

              </tr>
<%
    }
%>
            </tbody>
          </table>
<%
conn.close();
%>
