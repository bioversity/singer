<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<table width="100%" border="0" cellspacing="0" cellpadding="4">
  <tr>
    <td valign="top" width="70%">
    <br/>
    <span class="keyTitle"><b>Welcome to SINGER</b></span>
    <br/>
    <br/>
    <p class="BodyGray"><span class="BodyGray"><span class="MediumTitleBlue"><span>The System-wide Information Network for Genetic Resources (SINGER)</span></span><span>
    is the germplasm information exchange network of the <a target="_new" href="http://cgiar.org">Consultative Group on International Agricultural Research</a> (CGIAR) and its partners. </span></span></p>
    <p class="BodyGray"><span class="BodyGray"><span>Together, the members of SINGER hold more than half a million
    samples of crop, forage and tree diversity in their germplasm collections. This diversity is vital for food security and agricultural development; SINGER provides easy access
    to information about this diversity </span></span></p>
    <p class="BodyGray"><span class="BodyGray"><span>SINGER is an initiative of the CGIAR <a target="_new" href="http://sgrp.cgiar.org">System-wide Genetic Resources Programme</a> (SGRP).</span></span></p>
    <p class="BodyGray"></p>
    <span class="BodyGray"> </span><span class="BodyGray">This website allows you to:</span>
    <ul>
      <li type="square"><span class="BodyGray">Search for information about the samples of crop, forage, and tree germplasm <a href="/index.jsp?page=intrustcollections">held in trust</a> for the world</span>
      <li type="square"><span class="BodyGray">Order samples directly from our web-interface</span>
      <li type="square"><span class="BodyGray">Learn more about using SINGER to access plant genetic diversity quickly and easily; and </span>
      <li type="square"><span class="BodyGray">Find out about SINGER&rsquo;s background, vision and impacts.<br>
      <br>
      </span>
    </ul>
    <br/>
    <hr/>
    <br/>
    <jsp:include page="/otherinfo.jsp" flush="true"/>
    <br/>
    <hr/>
    <br/>
    </td>
    <td valign="top" width="30%" class="lborder">
    <jsp:include page="/logos.jsp" flush="true"/>  
    </td>
  </tr>
</table>
