<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.ResultSet"%>
<%@ page import="java.sql.SQLException"%>
<%@ page import="java.sql.Statement"%>
<%@ page import="java.sql.PreparedStatement"%>
<%@ page import="org.sgrp.singer.AccessionServlet"%>

<%
// create db connection
Connection conn = AccessionServlet.openConnection();

// which reference set?
String refSetPar = request.getParameter("refSet");
if(refSetPar == null)  {
    out.println("No media for this Reference Set");

    conn.close();
    return;
}

String sqlBiblio = "select * from wall where id = ? and type = 'biblio'";
PreparedStatement pstmt = conn.prepareStatement(sqlBiblio);
pstmt.setString(1, refSetPar);
ResultSet biblio = pstmt.executeQuery();

%>
<style>
.content {
    margin: 10px;
    width: 980px;
}
.wall {
    width: 500px;
    float: left;
}
.biblio {
    float: right;
    border : 1px solid #ddd;
    width: 400px;
    padding:20px;
}
.biblio h3 {
    text-align: center;
}

</style>

<div class="content">

<h2>Comments and Suggestions for <a href="index.jsp?page=referencesets&refSet=<%=refSetPar%>" style="font-size: 16px;color: #FA7231;"><%=refSetPar.replace("_"," ")%></a></h2>

    <div class="wall">
        <div id="disqus_thread"></div>
        <script type="text/javascript">
            /* * * CONFIGURATION VARIABLES: EDIT BEFORE PASTING INTO YOUR WEBPAGE * * */
            var disqus_shortname = 'singer'; // required: replace example with your forum shortname

            // The following are highly recommended additional parameters. Remove the slashes in front to use.
            // var disqus_identifier = 'referenceset_<%=refSetPar%>';
            // var disqus_url = 'http://example.com/permalink-to-page.html';

            /* * * DON'T EDIT BELOW THIS LINE * * */
            (function() {
                var dsq = document.createElement('script'); dsq.type = 'text/javascript'; dsq.async = true;
                dsq.src = 'http://' + disqus_shortname + '.disqus.com/embed.js';
                (document.getElementsByTagName('head')[0] || document.getElementsByTagName('body')[0]).appendChild(dsq);
            })();
        </script>
        <noscript>Please enable JavaScript to view the <a href="http://disqus.com/?ref_noscript">comments powered by Disqus.</a></noscript>
    </div><!-- /wall -->
    <div class="biblio">
    <h3>Bibliography</h3>

<% while(biblio.next()) { %>

    <%=biblio.getString("content")%>

<% } %>
    </div>
</div><!-- /content -->
<%
// close connection
conn.close();
%>
