<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"                                                                            
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">                                                                              

<html lang="en" xmlns="http://www.w3.org/1999/xhtml" xml:lang="en"><head>

<title>SINGER Warehouse Data Dictionary</title>

<link type="text/css" rel="stylesheet" media="screen" href="https://assets0.github.com/stylesheets/bundle_common.css?677c14c4c0026f1b894c1fe68b5a6549bff32c5a">
<link type="text/css" rel="stylesheet" media="screen" href="https://assets3.github.com/stylesheets/bundle_github.css?677c14c4c0026f1b894c1fe68b5a6549bff32c5a">

<style>
.description ul{
    padding-left:40px;
}
.bar {
    width: 420px;
    float: left;
    margin-left: 20px;
    margin-right: 20px;
}

div.comment_post {
    margin-top:5px;
    padding:0 1px;
    width:500px;
}
div.comment_post textarea {
    height:100px;
    width:500px;
}
.comment_buttons button {
    margin:5px 5px 10px 0;
}

#logo {
    float: right;
}
#user {
    padding-top: 20px;
}

h2 span {
    font-weight:normal;
    margin-left:10px;
}

button {
    padding: 5px 10px;
}

.commentarea form {
    margin-bottom: 10px;
}

.comment {
    margin-bottom: 20px;
}

.comment p {
    margin:5px 0;
}
.comment_info a {
    color: #336699;
    font-weight: bold;
}
.comment_date {
    color: #888888;
    font-size: x-small;
}

.comment_reply a {
    color:#888888;
    font-weight:bold;
    font-size: 12px;
}
.vote {
    background:none repeat scroll 0 0 #715EB9;
    color:#ddd;
    display:block;
    font-size:16px;
    padding:10px 20px;
    text-align:center;
    text-decoration:none;
    text-shadow:1px 1px rgba(0, 0, 0, 0.5);
}
vote:hover {
    color:#fff;
}
.vote.voted {
    background-color: #DC4040; 
    background-color: #66CC66; 
    color:#fff;
}
.vote.voted:hover {
    color:#fff;
    cursor: default;
}

.vote_value {
    margin-left: 20px;
    margin-right: 20px;
    cursor: pointer;
}
.vote_value:hover {
    color: #fff;
}

</style>

<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js" type="text/javascript"></script>

<script>
var URL_DATA = "/js/data-dictionary.json";
var disqus_shortname = 'singerdatadictionary'; // required: replace example with your forum shortname

function add_disqus_embed() {
    // The following are highly recommended additional parameters. Remove the slashes in front to use.
    // var disqus_identifier = 'referenceset_chickpea';
    // var disqus_url = 'http://example.com/permalink-to-page.html';

    /* * * DON'T EDIT BELOW THIS LINE * * */
    (function() {
        var dsq = document.createElement('script'); dsq.type = 'text/javascript'; dsq.async = true;
        dsq.src = 'http://' + disqus_shortname + '.disqus.com/embed.js';
        (document.getElementsByTagName('head')[0] || document.getElementsByTagName('body')[0]).appendChild(dsq);
    })();

}
function add_disqus_count() {
    /* * * DON'T EDIT BELOW THIS LINE * * */
    (function () {
        var s = document.createElement('script'); s.async = true;
        s.type = 'text/javascript';
        s.src = 'http://' + disqus_shortname + '.disqus.com/count.js';
        (document.getElementsByTagName('HEAD')[0] || document.getElementsByTagName('BODY')[0]).appendChild(s);
    }());

}

function show_loader() {
    $("#loader").show();
}

function hide_loader() {
    $("#loader").hide();
}

function createCookie(name,value,days) {
    if (days) {
        var date = new Date();
        date.setTime(date.getTime()+(days*24*60*60*1000));
        var expires = "; expires="+date.toGMTString();
    }
    else var expires = "";
    document.cookie = name+"="+value+expires+"; path=/";
}

function readCookie(name) {
    var nameEQ = name + "=";
    var ca = document.cookie.split(';');
    for(var i=0;i < ca.length;i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1,c.length);
        if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
    }
    return null;
}

function eraseCookie(name) {
    createCookie(name,"",-1);
}

/******* HOMEPAGE *******/
var homepage = function(){

function add_accession(node, accession_names) {

    $.each(accession_names, function(data){
        var acc_info = accession_names[data];
        var mcpd = acc_info["Token (MCPD)"] || "";

        var li = $('<li><ul class="repo-stats"><li class="watchers"><a href="http://singer.cgiar.org/data-dictionary/'+data+'#disqus_thread" title="Comments"></a>    </li></ul><h3><a href="/data-dictionary/'+data+'" class="term" title="'+data+'">'+data+'</a></h3><p class="description">'+acc_info.Label+'</p></li>');

        if(mcpd)
            li.find("a.term").append(' (<b>'+mcpd+'</b>)');
        node.append(li);
    });
}

function sort_alpha(obj) {
    var arr = [],
        o = {};

    for(var i in obj) {
        arr.push(i);
    }
    arr.sort();

    for(i=0; i<arr.length; i++) {
        o[arr[i]] = obj[arr[i]]; 
    }

    var halph = Math.floor(arr.length/2);

    return {
        obj: o,
        /* the key of the object in the middle */
        middle: arr[halph] 
    };
}

/* display cats with its accessions */
function start(el, obj) {
    var sorter = sort_alpha(obj);
    var obj = sorter.obj;

    var cont = ".bar.left";
    $.each(obj, function(data) {
        var accession_names = obj[data];
        var h2 = $('<h2 class="featured-heading"><a href="http://singerdatadictionary.disqus.com/latest.rss" class="feed">Subscribe</a>'+data+'</h2>');

        // add accession names inside this node
        var node = $('<ol class="ranked-repositories"></ol>');
        add_accession(node, accession_names);

        if(sorter.middle == data) {// we reached middle object
            cont = ".bar.right";
        }

        $(el+' '+cont).append(h2).append(node);
    });

}

show_loader();
$.getJSON(URL_DATA, function(data) {
    hide_loader();
    $("#homepage").show();
    start("#first", data["Passport Data Dictionary"]);
    start("#second", data["Other Units Data Dictionary"]);

    add_disqus_count();

});

};

/******* TERMPAGE *******/
var termpage = function(acc){

function lookup(cats) {
    for(var i in cats) {
        if(cats[i][acc])
            return cats[i][acc];
    }
    return false;
}

function display(curr) {
    var mcpd = curr["Token (MCPD)"] || "";

    if(mcpd)
        $("#.pagehead h1").append(" <b>("+mcpd+")</b>");

    $(".description").html(curr.Description);
    $(".human").html(curr.Examples);
    $(".scope").append("<span>"+curr.Scope+"</span>");
    $(".data-type").append("<span>"+curr["Data type"]+"</span>");
}
function get_vote_info(elem, descriptor, question) {
    var jelem = $(elem);
    $.get("vote?descriptor="+descriptor+"&voteQuestion="+question, function(data){
        jelem.addClass("voted");
        jelem.html(data);
    });
}
function voted(descriptor, question) {
    if(readCookie(descriptor+":"+question)) {
        return true;
    }
    return false;
}

$("#.pagehead h1").text(acc);

show_loader();
$.getJSON(URL_DATA, function(data) {
    hide_loader();
    $("#termpage").show();
    var curr_accession = lookup(data["Passport Data Dictionary"]);
    if(!curr_accession)
        curr_accession = lookup(data["Other Units Data Dictionary"]);

    display(curr_accession);
    add_disqus_embed();

});

// check if we already voted
$(".vote").each(function(){
    var question = $(this).children(":first").text();
    var descriptor = acc; // dunno why it's called "acc"
    if(voted(descriptor, question)) { // question was voted
        get_vote_info(this, descriptor, question);
    } 
});

// assign vote click
// run vote
$(".vote_value").click(function(e) {
    $this = $(this);
    var vote_value = $this.text();
    var vote_question = $this.parent().find(".question").text();

    var cont = $this.parent();
    cont.text("Loading votes...");
    $.post("vote", {
        "descriptor": acc,
        "vote_question": vote_question,
        "vote_value": vote_value
    }, function(data){
        get_vote_info(cont, acc, vote_question);

        // TODO set cookie
        createCookie(acc+":"+vote_question, "true", 100); // 100 days

    });

    e.preventDefault();
    e.stopPropagation();
    
});

};

$(function() {


<% 
String acc = (String)request.getAttribute("acc");
if(acc == null || acc.equals("")) { // homepage %>

    homepage();

<% } else { %>

    termpage("<%=acc%>");
<% }  %>

    $.ajaxSetup({ cache: false });
});
</script>

</head><body class="explore">
    <div class="" id="main">
        <div style="height: 100px;" id="header">
            <div id="logo">
                <a href="/data-dictionary"><img src="/img/singer.png"></a>
            </div>
            <div id="user">
                        </div>

            
        </div>
        <div class="site">
            <div id="loader">
                <img src="/img/ajax-loader.gif" />
            </div>

            <div id="homepage" style="display: none">
                <div class="pagehead">
                    <h2 style="margin: 5px; border-bottom: 0pt none ! important; padding: 0pt;"><a href="http://singerdatadictionary.disqus.com/latest.rss" class="feed">Subscribe to all comments</a></h2>
                    <h1>SINGER data dictionary</h1>
                </div>

                <div id="first">
                    <div class="bar left"></div>
                    <div class="bar right"></div>
                </div>

                <div class="pagehead" style="clear: both;">
                    <h2 style="margin: 5px; border-bottom: 0pt none ! important; padding: 0pt;"><a href="http://singerdatadictionary.disqus.com/latest.rss" class="feed">Subscribe to all comments</a></h2>
                    <h1>Other Units Data Dictionary</h1>
                </div>

                <div id="second">
                    <div class="bar left"></div>
                    <div class="bar right"></div>
                </div>
            </div>

            <div id="termpage" style="display: none">
                <div class="pagehead">
                    <h2 style="margin: 5px; border-bottom: 0pt none ! important; padding: 0pt;"><a href="http://singerdatadictionary.disqus.com/latest.rss" class="feed">Subscribe to all comments</a></h2>
                    <h1></h1>
                </div>

                <div class="description"></div>
                <h2 class="scope">Scope</h2>
                <h2 class="data-type">Data type</h2>
                <h2>Examples</h2>
                <div id="commit">
                    <div class="group">
                        <div class="envelope commit">
                            <div class="human">
                                
                            </div>
                        </div>
                    </div>
                </div>
                <h2>Vote</h2>
                <div class="vote">
                    <span class="question">Should this field be part of MCPD?</span>
                    <span class="vote_value">Yes</span>
                    <span class="vote_value">No</span>
                </div>
                <br>
                <div class="vote">
                    <span class="question">Should this field be part of the GENESYS Data dictionary?</span>
                    <span class="vote_value">Yes</span>
                    <span class="vote_value">No</span>
                </div>
                <div class="commentarea">
                    <h2></h2>
        <div id="disqus_thread"></div>
        <noscript>Please enable JavaScript to view the <a href="http://disqus.com/?ref_noscript">comments powered by Disqus.</a></noscript>


                </div>


            </div><!-- /termpage -->

        </div><!-- /site -->
    </div>


<!--[if IE 8]>
<script type="text/javascript" charset="utf-8">
  $(document.body).addClass("ie8")
</script>
<![endif]-->

<!--[if IE 7]>
<script type="text/javascript" charset="utf-8">
  $(document.body).addClass("ie7")
</script>
<![endif]-->

</body>
</html>
