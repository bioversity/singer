<table width="100%">
<tr>
<td width="99%">
<table width="100%">
<tr>
<td width="2%">
<img src="/img/login.gif"/>
</td>
<td class="bhead" width="100%">
Login
</td>
</tr>
<tr>
<td width="2%">
</td>
<td class="bdata" width="100%">
<jsp:include flush="true" page="/loginpid.jsp" />
</td>
</tr>
<tr>
<td width="2%">
<img src="/img/pointer.gif"/>
</td>
<td class="bhead" width="100%">
Search
</td>
</tr>
<tr>
<td width="2%">
</td>
<td class="bdata" width="100%">
<div><a href="/index.jsp?page=collections">Institutes / Collections</a></div> 
<div><a href="/index.jsp?page=taxatoz">Taxon</a></div>
<div><a href="/index.jsp?page=atoz">Genus / Species</a></div>
<div><a href="/index.jsp?page=source">Collecting source</a></div>
<div><a href="/index.jsp?page=status">Sample status</a></div> 
<div><a href="/index.jsp?page=conatoz">Country source</a></div>
<% //HTML Commented by Gautier to disabled the separation between Search and Advanced Search %>
<!--<br/>
</td>
</tr>


<tr>
<td width="2%">
<img src="/img/search.png"/>
</td>
<td class="bhead" width="100%">
Advanced Search
</td>
</tr> 
<tr>
<td width="2%">
</td>
<td class="bdata" width="100%">-->
<div><a href="/index.jsp?page=keywords">Cross-keywords</a></div>
<div><a href="/index.jsp?page=prepareSearchForm.do">Multi-selection</a></div>
<div><a href="/index.jsp?page=selectionmap">Map selection</a></div>
<div><a href="/index.jsp?page=queryMaker">Build your query</a></div>
<!-- div><a href="/index.jsp?page=new_selectionmap">New map selection *</a></div-->
<br/>
</td>
</tr>

<tr>
<td width="2%">
<img src="/img/distribution.gif"/>
</td>
<td class="bhead" width="100%">
Distribution
</td>
</tr>
<tr>
<td width="2%"></td>
<td class="bdata" width="100%">
Disabled. Waiting for update.
</td>
<!-- Distribution disabled by Gautier. Waiting for update
<tr>
<td width="2%">
</td>
<td class="bdata" width="100%">
<div><a href="/index.jsp?page=fulldistribution&search=all">System-wide</a></div> 
<div><a href="/index.jsp?page=distribution">Collection x Yearly</a></div>
<div><a href="/index.jsp?page=showinstdistribution">Institute specific</a></div>
<br/>
</td>
</tr>
-->
<tr>
<td width="2%">
<img src="/img/leaf.gif"/>
</td>
<td class="bhead" width="100%">
Collecting Missions
</td>
</tr>
<tr>
<td width="2%">
</td>
<td class="bdata" width="100%">
<div><a href="/index.jsp?page=missiontimeline">Timeline View</a></div> 
<div><a href="/index.jsp?page=showinstmission">By Institute</a></div>
<div><a href="/index.jsp?page=conmissionatoz">In Country</a></div>
<br/>
</td>
</tr>

<tr class="biomissions">
    <td width="2%">
    <img src="/img/leaf.gif"/>
    </td>
    <td class="bhead" width="100%">
    Collected Samples Data (NEW)
    </td>
</tr>
<tr class="biomissions">
<td width="2%">
</td>
<td class="bdata" width="100%">
<div><a href="/index.jsp?page=coll-sample-data">IBPGR/IPGRI Supported Missions Database</a></div> 
<div><a target="_blank" href="http://www.central-repository.cgiar.org/crop_collecting_missions.html">Collecting Missions File Repository</a></div> 
<br/>
<% if(request.getParameter("page") != null && request.getParameter("page").equals("biomissions")) {%>
<p>
<b>The collecting missions section of SINGER now makes sample-level data available from the IBPGR- and IPGRI-supported collecting missions.</b>
Thanks to initial support received from the World Bank through the GPG2 Project, the trip reports and original collecting forms from 558 IBPGR- and IPGRI-supported collecting missions were digitally scanned and saved as PDF files - totaling nearly 60 000 pages of information! Passport data was then extracted from the reports and fed into a sample-level database. The original information recovered from the scanned reports has enabled enhanced passport data for about 130 000 samples to be uploaded to the collecting mission section of SINGER.
</p>
<p>
 
<b>This new database also provides easy access to the full content of the original collecting mission reports</b>, including text, charts, maps and photographs, through links to the corresponding PDF files. These reports contain extremely valuable information that will prove useful, not only for current and future users of the germplasm collected, but also for genebank managers, germplasm collectors, and anyone interested in the history of plant exploration and the distribution.  The reports contain keen, first-hand observations made in the field by the plant collectors regarding the habitat, local names, uses, and unique characteristics of these plant genetic resources at the moment they were first collected.  We encourage you to delve into this treasure chest of new information that is now made available here, information that in many cases was never before published or otherwise disseminated. 
  
<p>Please send any comments and suggestions to <a title="send comments" href="mailto:singer@cgiar.org,m.skofic@cgiar.org,i.thormann@cgiar.org,H.Gaisberger@cgiar.org?subject=My comments are..">singer@cgiar.org</a> so that we may continue improving and enriching this new feature of SINGER. 
</p>
</p>
<%  } %>
</td>
</tr>


</table>
</td>
<td width="1%">&nbsp;</td>
</tr>
</table>

