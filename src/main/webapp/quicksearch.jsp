<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<script language="JavaScript" type="text/javascript">
	function doSearch()
	{
	var search = document.getElementById('search').value;
	if(search!=null && search.length>0)
	{
		var sel = document.getElementById('like').checked;
		if(sel)
		{
			if(search.indexOf('~')<0)
			{
			search = search+"~";
			}
		}
		window.location = '/index.jsp?page=disppage&search='+search;
	}
    else
    {
      alert('Please enter query.');
    }
	}
	</script>

<form action="/index.jsp" method="get" onsubmit="doSearch(); return false;" style="display: inline;">
<% 
String pagePar = request.getParameter("page");
boolean is_bio = false;
if(pagePar != null && (pagePar.equals("coll-sample-data") ||pagePar.equals("biomissions") || pagePar.equals("showbiomission") || pagePar.equals("showsample") || pagePar.equals("submissionsamples"))) { // show different search
    is_bio = true;
%>
  <input type="hidden" name="page" value="biomissions" />
<% } else { %>
  <input type="hidden" name="page" value="disppage" />

<% } %>
  <%
String search= request.getParameter("search");
String cpage = request.getParameter("page");
if(cpage!=null && cpage.equals("showkeycount"))
{
  search="";
}
%>
<table align="right" border="0" cellpadding="0" cellspacing="0" height="1">
      <tbody><tr class="<%=(is_bio ? "biomissions" : "")%>">
        <td style="color: rgb(86, 102, 77); font-family: Verdana,Geneva,Arial,Helvetica,sans-serif; font-weight: bold; font-size: 8pt; text-decoration: none;" valign="middle"><img src="/img/page.png" border="0" width="15" height="15"/> 
<%if(is_bio) { // show different search
%>
        SEARCH IN SAMPLE <br>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;LEVEL DATABASE:&nbsp;
<% }  else { %>
        SEARCH:&nbsp;
<% } %>
        </td>
        <td valign="middle" nowrap="nowrap"><input name="search" />&nbsp;<input type="checkbox" id="like" name="like" /> Like &nbsp;<input type="submit" value=">>" /> &nbsp;
        <td valign="middle" nowrap="nowrap">
        <a title="Search Help" href="javascript:void(0);" onclick="javascript:openWin('/searchhelp.htm');"><img src="/img/question.gif" border="0"/></a></td>
        <td>
        &nbsp;&nbsp;&nbsp;&nbsp;
        </td>
      </tr>
    </tbody></table>
    </form>
  
  

