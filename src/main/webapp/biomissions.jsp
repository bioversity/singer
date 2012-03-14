<%@ page import="org.sgrp.singer.biomissions.SubMissions"%>
<%@ page import="org.sgrp.singer.AccessionServlet"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.ResultSet"%>
<%@ page import="java.sql.SQLException"%>
<%@ page import="java.util.ArrayList"%>
<%
// we need to get all the sub_missions, however, the UI needs
// to indent the id_mission value.

// create db connection
Connection conn = AccessionServlet.openConnection();

SubMissions subMiss = new SubMissions(conn);

// search
String searchTerm = request.getParameter("search");


// ugly fix, pagination sucks - should use scaffolding
int pageNum = 1;

int totalSubMissions = 0;
int totalMissions = 0;
int totalPages = 0;

if(searchTerm == null) {
    // we only want 10 items per page
    subMiss.setItemsPerPage(10);
    totalSubMissions = subMiss.getTotalSubMissions();
    totalMissions = subMiss.getTotalMissions();
    totalPages = subMiss.getAmountOfPages();

    // get the sub_missions for this page.
    // convert the pageNum parameter to an integer
    String pageNumParameter = request.getParameter("pageNum");
    if(pageNumParameter != null) {
        pageNum = Integer.parseInt(pageNumParameter.trim());

        if(pageNum <= 0) pageNum = 1;
        if(pageNum >= totalPages) pageNum = totalPages;
    }
} else {
    subMiss.setItemsPerPage(100);
    // trim searchTerm
    searchTerm = searchTerm.trim();
}

// get the data
ResultSet rs = subMiss.getSubMissionsForPage(pageNum, searchTerm);

%>

<table width="100%" style="background-color: #FFFFCC;">
    <tbody>
      <tr>
        <td width="2%"></td>

        <td width="95%">
        <h2>Welcome to the sample level database!</h2>
          <b>IBPGR/IPGRI-supported missions</b>

          <table width="100%">
            <tbody>
              <tr>
                <% if(searchTerm == null) { %>
                <td align="left"><b>Total missions: <%=totalMissions%>. Total sub-missions: <%=totalSubMissions%>. Viewing page <%=pageNum%> of <%=totalPages%></b></td>

                <td align="right"><b>Pages : 
                <% // show only the 10 pages from this pageNum, so if pageNum is 31
                  // only show pages from 31 to 40
                ArrayList<Integer> pageLinks = subMiss.getPageLinks(pageNum);
                if(pageLinks.get(0) > subMiss.getItemsPerPage()) {
                %>
                <a class="menubarlinks" href="/index.jsp?page=biomissions&pageNum=<%=pageNum-10%>">&lt; Previous 10</a>
                <%
                }
                for(Integer link : pageLinks) {
                    if(link == pageNum) {
                %>
                    <%=link%>
                <%

                    } else {
                %>
                    <a href="/index.jsp?page=biomissions&pageNum=<%=link%>"><%=link%></a>

                <%
                    }
                }
                %>
                <%
                if(pageLinks.get(pageLinks.size() - 1) < totalPages) {
                %>
                <a class="menubarlinks" href="/index.jsp?page=biomissions&pageNum=<%=pageNum+10%>">Next 10 &gt;</a>
                <%
                }
                %>
                </b></td>

                <% } else {%>

                <br><br>
                Search result for <b><%=searchTerm%></b>

                <% } %>
              </tr>
            </tbody>
          </table><br />

<style>
tr.submissions td {
   border-bottom:0; 
   background-color: #ddd;
}
</style>
          <table width="100%">
            <tbody>
              <tr>
                <td class="collHead">Mission code</td>

                <td class="collHead" width="30%">Title</td>
                <td class="collHead" width="30%">Institute(s)</td>

                <td class="collHead">Country</td>

                <td class="collHead">Start date</td>

                <td class="collHead">End date</td>

                <td class="collHead">Samples Count Linked</td>
              </tr>
<%
    while(rs.next()) {
        // these are all SQL fields
        String subMissionId = rs.getString("ID_SUB_MISSION");
        String missionId = rs.getString("ID_MISSION");
        String missionTitle = rs.getString("Title");
        String startDate = rs.getString("StartDate");
        String endDate = rs.getString("EndDate");

        ResultSet subRes = subMiss.getSubMissionFromMissionId(missionId);


        boolean hasSubmissions = false;

        subRes.next(); // this is only to check whether submissions exist (the 2nd result)

        // check whether submissions exist, 
        // if they exist, we don't need to show a link and samples numbers
        if(subRes.next()) {
            hasSubmissions = true;
        }
%>

              <tr>
                <td class="collValue">
        <%
        // check whether submissions exist, 
        // if they exist, we don't need to show a link and samples numbers
        if(hasSubmissions) {
        %>
                <%=missionId%>
        <%
        } else {
        %>
                <a href="/index.jsp?page=showbiomission&subid=<%=subMissionId%>"><%=missionId%></a>
        
        <% } %>
                
                </td>

                <td class="collValue"><%=missionTitle%></td>

                <td class="collValue"><%=subMiss.parseInstCode(rs.getString("instcodes"))%></td>

                <td class="collValue">
        <%
        if(!hasSubmissions) {
        %>

                <%=rs.getString("countryName")%>

        <% }  %>
                </td>

                <td class="collValue">
        <%
        if(!hasSubmissions) {
        %>
                <%=startDate%>
        <% }  %>

                </td>
                <td class="collValue">
        <%
        if(!hasSubmissions) {
        %>

                <%=endDate%>

        <% }  %>
                </td>

                <td class="collValue">

        <%
        if(!hasSubmissions) {
        %>
                <a href="/index.jsp?page=submissionsamples&subid=<%=subMissionId%>">
                View the <%=rs.getString("samplesCount")%> collected sample(s)</a>

        <% }  %>
                </td>
              </tr>
                <%
                subRes.beforeFirst();
                // Looping through the sub-missions
                while(subRes.next()) {
                    // this is for when there's no sub-missions
                    if(subRes.getString("ID_SUB_MISSION").equals(missionId)) continue;
                %>
                    
              <tr class="submissions">
                <td class="collValue" style="padding-left: 20px;">
                <a href="/index.jsp?page=showbiomission&subid=<%=subRes.getString("ID_SUB_MISSION")%>"><%=subRes.getString("ID_SUB_MISSION")%></a>
                </td>

                <td class="collValue"></td>

                <td class="collValue"></td>

                <td class="collValue"><%=subRes.getString("countryName")%></td>

                <td class="collValue"><%=subRes.getString("StartDate")%></td>

                <td class="collValue"><%=subRes.getString("EndDate")%></td>

                <td class="collValue">
          <a href="/index.jsp?page=submissionsamples&subid=<%=subRes.getString("ID_SUB_MISSION")%>">
                View the <%=subRes.getString("samplesCount")%> collected sample(s)</a></td>
              </tr>
                <%
                }
                %>
<%
    }
%>
            </tbody>
          </table><br />
        </td>
      </tr>
    </tbody>
  </table>

<%
conn.close();
%>
