<%@ include file="/functions.jsp"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@page import="org.sgrp.singer.AccessionConstants"%>
<%@page import="org.sgrp.singer.PropertiesManager"%>

<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js" type="text/javascript"></script>

<style>
.box {
background: #FFFFCC; 
position:absolute;display: none;
}
.box li {
    height:23px;
    line-height:22px;
    margin-left:20px;
    margin-right:20px;
}
.box li a {
    margin: auto;
}
</style>
<script>

$(function() {

var box = (function(){
    var html = "<div class='box'>"
    +"<li><a target='_blank' href='http://www.bioversityinternational.org/nc/publications/publication/issue/faoipgri_multi_crop_passport_descriptors.html'>FAO/IPGRI Multi-crop passport descriptors</a></li>"
    +"<li><a target='_blank' href='http://www.bioversityinternational.org/publications/search/results.html?t=11&pageTitle=Yes'>Crop descriptors</a></li>"
    +"<li><a target='_blank' href='http://www.bioversityinternational.org/publications/search/results.html?q=Key%20access%20and%20utilization%20descriptors&pageTitle=Yes'>Key access and utilization descriptors for crop genetic resources</a></li>"
    +"</div>";
    var jhtml = $(html);
    jhtml.hover(function(){
        $(this).show();
    }, function(){
        $(this).hide();
    });
    $(document.body).append(jhtml);
    return jhtml;
})();

$("#standards").hover(function(e) {
    $this = $(this);
    var p = $this.position(); 

    box.css("top", p.top + 10);
    box.css("left", p.left);
    box.show();    
}, function(){
    box.hide();
});

});

</script>
<table width="100%">
<tr>
<td align="left" valign="middle">
<b>|</b><a class="menubarlinks" title="Home" href="/index.jsp">Home</a>
<b>|</b><a class="menubarlinks" title="Information about Members of SINGER" href="/index.jsp?page=members">Members</a>
<b>|</b><a class="menubarlinks" title="Standard Material Transfer Agreement" href="/index.jsp?page=smta">SMTA</a>
<b>|</b><a id="standards" class="menubarlinks" title="Information about Standards" href="#">Standards</a>
<b>|</b><a class="menubarlinks" title="Information about SINGER" href="/index.jsp?page=what">What we do</a>
<b>|</b><a class="menubarlinks" title="SINGER Terms of use and License" href="/index.jsp?page=terms">Terms of use</a>
<b>|</b><a class="menubarlinks" title="SINGER Disclaimer" href="/index.jsp?page=disclaimer">Disclaimer</a><b>|</b>
<% // Add by Gautier %>
<a class="menubarlinks" title="CGIAR Crop Genebank Knowledge Base" href="/index.jsp?page=tutorial"> Tutorials</a><b>|</b>
<a class="menubarlinks" title="CGIAR Crop Genebank Knowledge Base" href="http://cropgenebank.sgrp.cgiar.org/" target= "_blank"> Knowledge Bank</a><b>|</b>
<a class="menubarlinks" title="SINGER Data-Dictionary" href="http://singer.cgiar.org/data-dictionary" target= "_blank">SINGER Data Dictionary</a><b>|</b>
<% //End Add by Gautier %>
<!-- b>|</b><a class="menubarlinks" title="Internal Use" href="/index.jsp?page=statuspage">Status Page</a-->
</td>
</tr>
</table>
