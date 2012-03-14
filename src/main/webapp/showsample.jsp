<%@ page import="org.sgrp.singer.biomissions.SubMissions"%>
<%@ page import="org.sgrp.singer.AccessionServlet"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.ResultSet"%>
<%@ page import="java.sql.SQLException"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.text.DecimalFormat"%>
<%
Connection conn = AccessionServlet.openConnection();
SubMissions s = new SubMissions(conn);

String parSampleId = request.getParameter("id");

if(parSampleId == null || parSampleId.equals("")) {
    out.println("You must specify a sample id");
    return;
}

int sampleId = Integer.parseInt(parSampleId);
ResultSet rs = s.getSampleId(sampleId);

if(!rs.next()) {
    out.println("No Sample found");
    return;
} 

String[] taxons = rs.getString("ScientificName").split(" ");

String genus = "";
String species = "";

if(taxons.length == 2) {
    genus = taxons[0];
    species = taxons[1];
} else if(taxons.length == 1) {
    genus = taxons[0];
}

// get sub mission information to get countr etc
Map data = s.getSingleSubMissionData(rs.getString("ID_SUB_MISSION"));
%>
<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script> 
<script type="text/javascript"> 

function initialize() {
    var myLatlng = new google.maps.LatLng(<%=rs.getString("LATITUDEdecimal")%>, <%=rs.getString("LONGITUDEdecimal")%>);
    var myOptions = {
      zoom: 4,
      center: myLatlng,
      mapTypeId: google.maps.MapTypeId.ROADMAP
    }
    var map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);


    var marker = new google.maps.Marker({
        position: myLatlng, 
        map: map,
        title:""
    });   

    /*
    var infowindow = new google.maps.InfoWindow({
        content: "BOOM"
    });
    google.maps.event.addListener(marker, "mouseover", function() {
        infowindow.open(map, marker);
    });
    */
}

window.onload = function() {
<% if(rs.getString("LATITUDEdecimal") != null &&
        rs.getString("LONGITUDEdecimal") != null) { %>
    initialize();
<% } %>
}

</script>


<span class="accTitle">Sample information

</span>
<style>
.accHead  {
background-color: #FFFFCC;
}
</style>
<table width="100%" style="background-color: #FFFFCC;">
    <tbody>
      <tr>
        <td width="30%" nowrap="nowrap" class="accHead">Collecting number</td>

        <td width="70%" class="accValue"><%=rs.getString("CollectingNumber")%></td>
      </tr>

      <tr>
        <td class="accHead">Taxon</td>

        <td class="accValue"><%=rs.getString("ScientificName")%></td>
      </tr>

      <tr>
        <td class="accHead">Genus</td>

        <td class="accValue"><%=genus%></td>
      </tr>

      <tr>
        <td class="accHead">Species</td>

        <td class="accValue"><%=species%></td>
      </tr>

      <tr>
        <td class="accHead">Institute</td>

        <td class="accValue"><%=data.get("institutes")%></td>
      </tr>


      <tr>
        <td class="accHead">Collecting Source</td>

        <td class="accValue"><%=s.convertCollSrc(rs.getInt("COLLSRC"))%></td>
      </tr>

      <tr>
        <td class="accHead">Sample Status</td>

        <td class="accValue"><%=s.convertSampleStatus(rs.getInt("SAMPSTAT"))%></td>
      </tr>
      <tr>
        <td class="accHead">Collection Date</td>

        <td class="accValue"><%=data.get("startDate")%></td>
      </tr>

      <tr>
        <td class="accHead">Other numbers</td>

        <td class="accValue"><%=rs.getString("OtherNumbers")%></td>
      </tr>

      <tr>
        <td class="accHead">Collection site</td>

        <td class="accValue"><%=rs.getString("COLLSITE")%></td>
      </tr>

      <tr>
        <td class="accHead">Region</td>

        <td class="accValue"><%=rs.getString("Admin1")%></td>
      </tr>
      <tr>
        <td class="accHead">Province</td>

        <td class="accValue"><%=rs.getString("Admin2")%></td>
      </tr>
      <tr>
        <td class="accHead">Municipality</td>

        <td class="accValue"><%=rs.getString("Admin3")%></td>
      </tr>
      <tr>
        <td class="accHead">Country</td>

        <td class="accValue"><%=data.get("country")%></td>
      </tr>
      <tr>
        <td class="accHead">Latitude<sup>o</sup></td>
        <%
        DecimalFormat myFormatter = new DecimalFormat("###.##");
        %>

        <td class="accValue"><i><%=myFormatter.format(rs.getDouble("LATITUDEdecimal"))%></i></td>
      </tr>

      <tr>
        <td class="accHead">Longitude<sup>o</sup></td>

        <td class="accValue"><i><%=myFormatter.format(rs.getDouble("LONGITUDEdecimal"))%></i></td>
      </tr>
      <tr>
        <td class="accHead">Elevation</td>

        <td class="accValue"><i><%=rs.getString("ELEVATION")%></i></td>
      </tr>
      <tr>
        <td class="accHead">Admin 1</td>

        <td class="accValue"><i><%=rs.getString("Admin1")%></i></td>
      </tr>
      <tr>
        <td class="accHead">Admin 2</td>

        <td class="accValue"><i><%=rs.getString("Admin2")%></i></td>
      </tr>
      <tr>
        <td class="accHead">Admin 3</td>

        <td class="accValue"><i><%=rs.getString("Admin3")%></i></td>
      </tr>
<% if(rs.getString("LATITUDEdecimal") != null &&
        rs.getString("LONGITUDEdecimal") != null) { %>
      <tr>
        <td class="accHead">Map</td>

        <td class="accValue"><div style="width:500px;height:500px;" id="map_canvas"></div></td>
      </tr>
<% } %>
    </tbody>
  </table>
<%
conn.close();
%>
