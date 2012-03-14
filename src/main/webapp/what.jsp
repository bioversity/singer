<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<script type="text/javascript" src="/tabs/tabber.js"></script>
<link rel="stylesheet" href="/tabs/tab.css" TYPE="text/css" MEDIA="screen">
<div>
<div class="tabber">
<div class="tabbertab" title="What we do"><jsp:include flush="true" page="whatwedo.htm" /></div>
<div class="tabbertab" title="In trust"><jsp:include flush="true" page="intrust.htm" /></div>
<div class="tabbertab" title="Needs"><jsp:include flush="true" page="needs.htm" /></div>
<div class="tabbertab" title="Standards"><jsp:include flush="true" page="standards.htm" /></div>
<div class="tabbertab" title="Knowledge"><jsp:include flush="true" page="knowledge.htm" /></div>
<div class="tabbertab" title="Technology"><jsp:include flush="true" page="technology.htm" /></div>
<div class="tabbertab" title="Impact"><jsp:include flush="true" page="impact.htm" /></div>
</div>
</div>