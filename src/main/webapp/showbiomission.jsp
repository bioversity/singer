<%@ page import="org.sgrp.singer.biomissions.SubMissions"%>
<%@ page import="org.sgrp.singer.AccessionServlet"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.ResultSet"%>
<%@ page import="java.util.Map"%>

<%
// create db connection
Connection conn = AccessionServlet.openConnection();

SubMissions s = new SubMissions(conn);

// get the specific data for this submission id
String subMissionId = request.getParameter("subid");

// get general information for this submission
Map data = s.getSingleSubMissionData(subMissionId);

// get batch-level samples data
ResultSet batchRs = s.getBatchSamplesForSubMission(subMissionId);

// get cooperators
ResultSet rs = s.getCooperatorsForSubMission(subMissionId);

// get coordinates of samples
ResultSet coords = s.getCoordsForAllSamplesInSubMission(subMissionId);

// move it to first row
coords.first();

%>
<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script> 
<script type="text/javascript" src="http://google-maps-utility-library-v3.googlecode.com/svn/tags/markerclusterer/1.0/src/markerclusterer_packed.js"></script> 
<script type="text/javascript"> 

function mapInit() {
    
<%
// check whether it's empty
if(coords.first()) {
%>
    var center = new google.maps.LatLng(<%=coords.getString("LATITUDEdecimal")%>, <%=coords.getString("LONGITUDEdecimal")%>);
<%
} else {
%>
    return;

<% } %>

    var myOptions = {
      zoom: 4,
      center: center,
      mapTypeId: google.maps.MapTypeId.ROADMAP
    }
    var map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);


    var markers = [], myLatlng;

    // loop this
<%
while(coords.next()) {
    if(coords.getString("LATITUDEdecimal") != null && coords.getString("LONGITUDEdecimal") != null) {
%>
    myLatlng = new google.maps.LatLng(<%=coords.getString("LATITUDEdecimal")%>, <%=coords.getString("LONGITUDEdecimal")%>);
    markers.push(
        new google.maps.Marker({
            position: myLatlng, 
            map: map
        })
    );   
<% }
} %>

    var markerCluster = new MarkerClusterer(map, markers);

}

// the checkboxes toggling
window.onload = function() {
    mapInit();
    function checkall(c) {
        var checkboxes = document.getElementById("checkcont").getElementsByTagName("input");
        for(var i=0; i<checkboxes.length; i++) {
            checkboxes[i].checked = c;
        }
    }
    document.getElementById("checktoggle").onclick = function() {
        if(this.checked == true) {
            checkall(true);
        } else {
            checkall(false);
        }
    } 
}

</script>
<table width="100%" style="background-color: #FFFFCC;">
    <tbody>
      <tr>
        <td width="100%" align="left">
          <span class="accTitle">Collecting Mission</span><br />
          <a href="/index.jsp?page=submissionsamples&subid=<%=subMissionId%>">
          View the <%=data.get("samplesCount")%> collected sample(s)</a><br />

              <form method="post" action="http://www.central-repository.cgiar.org/index.php?id=2391" target="_blank">
                  <input type="hidden" size="45" value="<%=data.get("missionCode")%>" name="user_alfsearch_pi1[search1][missionID]">
                  <input type="hidden"  value="Search" name="user_alfsearch_pi1[search1][submit]">
                  
                  <div style="margin:auto; text-align: center;margin-top:-18px;">
                  <style>
                    .pdf-link:hover {
                       color: #ff3333; 
                    }
                  </style>
                      <img src="http://www.central-repository.cgiar.org/alfresco/images/filetypes/pdf.gif" style="vertical-align:middle;"/><input class="pdf-link" type="submit" value="View pdf files for this mission" style="background: none repeat scroll 0% 0% transparent; border: 0pt none; cursor: pointer; color: #336699;"/>
                  </div>
              </form>
          </div>
          <br />

          <table width="100%">
            <tbody>
              <tr>
                <td width="50%" valign="top">
                  <br />
                  <span class="accTitle">Information</span><br />

                  <table width="100%">
                    <tbody>
                      <tr>
                        <td class="collHead">Name</td>

                        <td class="collHead">Value</td>
                      </tr>

                      <tr>
                        <td class="collValue">Sub-Mission code</td>

                        <td class="collValue"><%=data.get("subMissionCode")%></td>
                      </tr>
                      <tr>
                        <td class="collValue">Mission code</td>

                        <td class="collValue"><%=data.get("missionCode")%></td>
                      </tr>

                      <tr>
                        <td class="collValue">Institute(s)</td>

                        <td class="collValue"><%=data.get("institutes")%></td>
                      </tr>

                      <tr>
                        <td class="collValue">Country</td>

                        <td class="collValue"><%=data.get("country")%></td>
                      </tr>

                      <tr>
                        <td class="collValue">Start date</td>

                        <td class="collValue"><%=data.get("startDate")%></td>
                      </tr>

                      <tr>
                        <td class="collValue">End date</td>

                        <td class="collValue"><%=data.get("endDate")%></td>
                      </tr>

                      <tr>
                        <td></td>
                      </tr>
                      <% if(coords.first()) { %>
                      <tr>
                        <td class="collValue" colspan=2>
                            <span class="accTitle">Samples locations</span>
                        </td>

                      </tr>
                      <tr>
                        <td class="collValue" colspan=2>
                            <div style="width:500px;height:400px;" id="map_canvas"></div>
                        </td>

                      </tr>
                      <% } %>
                    </tbody>
                  </table>
                </td>

                <td width="50%" valign="top">
                  <br />
                  <span class="accTitle">Batch-level samples</span><br />

                  <form method="get" action="/index.jsp">
                    <input type="hidden" name="page" value="submissionsamples" />
                    <input type="hidden" name="subid" value="<%=subMissionId%>" />
                  <table width="100%" id="checkcont">
                    <tbody>
                      <tr>
                        <td class="collHead">Taxon</td>

                        <td class="collHead">Samples</td>
                        <td class="collHead">
                            <input type="checkbox" id="checktoggle" />
                        </td>

                      </tr>

<% while(batchRs.next()) { %>
                      <tr>
                        <td class="collValue"><%=batchRs.getString("ScientificName")%></td>
                        <td class="collValue"><%=batchRs.getString("groupedSamplesCount")%></td>
                        <td class="collValue">
                            <input type="checkbox" name="taxon" value="<%=batchRs.getString("ScientificName")%>" />
                        </td>
                      </tr>
<% } %>

                      <tr>
                        <td><input type="submit" value="Filter" /></td>
                      </tr>
                      <tr>
                        <td></td>
                      </tr>
                    </tbody>
                  </table>
                  </form>
                </td>
              </tr>
            </tbody>
          </table><br />
        <% if(rs.first()) { 
            rs.beforeFirst(); // what a hacky way, ResultSet sucks
        %>
          <span class="accTitle">Cooperators Information</span><br />

          <table width="100%">
            <tbody>
              <tr>
                <td class="collHead" width="20%">Cooperator Firstname</td>
                <td class="collHead" width="20%">Cooperator Surname</td>

                <td class="collHead" width="40%">Organization</td>

              </tr>


            <% while(rs.next()) { %>
              <tr>
                <td class="collValue"><%=rs.getString("Firstname")%></td>

                <td class="collValue"><%=rs.getString("Surname")%></td>

                <td class="collValue"><%=rs.getString("NAME_NAT")%></td>
              </tr>
            <% } %>

            </tbody>
          </table><br />
          &nbsp;
        <% } %>
        </td>
      </tr>
    </tbody>
  </table>
<%
conn.close();
%>
