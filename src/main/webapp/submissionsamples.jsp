<%@ page import="org.sgrp.singer.biomissions.SubMissions"%>
<%@ page import="org.sgrp.singer.AccessionServlet"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.ResultSet"%>
<%@ page import="java.sql.SQLException"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Map"%>

<%
Connection conn = AccessionServlet.openConnection();
SubMissions s = new SubMissions(conn);

String subMissionId = request.getParameter("subid");
ResultSet rs = s.getSamplesForSubMission(subMissionId);

Map data = s.getSingleSubMissionData(subMissionId);

// let's do the filtering in the Java code, who cares
String[] taxons = request.getParameterValues("taxon");

%>
<strong>Samples for sub-mission:</strong> <%=subMissionId%><br>
<strong>Institute(s):</strong> <%=data.get("institutes")%><br>
<strong>Total Samples count:</strong> <%=data.get("samplesCount")%><br>
<br><br>

        <table width="100%" style="background-color: #FFFFCC;">
            <tbody>
              <tr>
                <td class="collHead"></td>


                <td class="collHead">Taxon</td>
                <td class="collHead">Other Numbers</td>
                <td class="collHead">Region</td>
                <td class="collHead">Province</td>
                <td class="collHead">Municipality</td>

                <td class="collHead">Samples Count</td>
                <td class="collHead">Notes</td>
              </tr>

<% while(rs.next()) { 
        
    // let's default to not found
    boolean found = false;

    // let's find our taxon in the parameter array
    if(taxons != null && taxons.length != 0) {
        // loop through the array
        for(int i=0; i<taxons.length; i++) {
            if(rs.getString("ScientificName").equals(taxons[i])) {
                // we have a match, let's mark it
                found = true;
            }
        }
    } else {
        // our taxons dont exist, always show all
        found = true;

    }

    // if we didn't find it, let's not show it
    if(!found) continue;

    String notes = (rs.getString("Notes") == null) ? "" : rs.getString("Notes");
%>
              <tr>
                <td class="collValue">
                    <% if(!notes.startsWith("No further")) { %>
                    <a href="/index.jsp?page=showsample&id=<%=rs.getString("ID_SAMPLE")%>">View sample</a>
                    <% } %>
                </td>


                <td class="collValue"><%=rs.getString("ScientificName")%></td>
                <td class="collValue"><%=rs.getString("OtherNumbers")%></td>
                <td class="collValue"><%=rs.getString("region")%></td>
                <td class="collValue"><%=rs.getString("province")%></td>
                <td class="collValue"><%=rs.getString("municipality")%></td>

                <td class="collValue"><%=rs.getString("SamplesCount")%></td>
                <td class="collValue"><%=(notes.startsWith("No further MCPD")) ? "No further Passport data" : notes%></td>
              </tr>
<% } %>

            </tbody>
          </table><br />
<%
conn.close();
%>
