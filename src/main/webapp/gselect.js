
var oZoomArea=null;
GSelectControl.prototype = new GControl();

//class globals
GSelectControl.G={
  bDragging:false,
  mct:null,
  mcr:null,
  mcb:null,
  mcl:null,
	oMapPos:null,
	oOutline:null,
	nMapWidth:0,
	nMapHeight:0,
	nMapRatio:0,
	nStartX:0,
	nStartY:0,
	nBorderCorrect:0
};

GSelectControl.prototype.initButton_=function(oMapContainer) {
	var G=GSelectControl.G;
	var oButton = document.createElement('div');
	oButton.innerHTML=G.options.sButtonHTML;
	oButton.id='gzoom-control';
	acl.style([oButton],{cursor:'pointer',zIndex:200});
	acl.style([oButton],G.options.oButtonStartingStyle);
	acl.style([oButton],G.options.oButtonStyle);
	oMapContainer.appendChild(oButton);
	return oButton;
};

GSelectControl.prototype.setButtonMode_=function(sMode){
	var G=GSelectControl.G;
	if (sMode=='zooming') {
		if(oZoomArea!=null)
		{
			G.oMap.removeOverlay(oZoomArea);
		}
		G.oButton.innerHTML=G.options.sButtonZoomingHTML;
		acl.style([G.oButton],G.options.oButtonZoomingStyle);
	} else {
		G.oButton.innerHTML=G.options.sButtonHTML;
		acl.style([G.oButton],G.options.oButtonStyle);
	}
};

// ******************************************************************************************
// Methods required by Google maps -- initialize and getDefaultPosition
// ******************************************************************************************
GSelectControl.prototype.initialize = function(oMap) {
  var G=GSelectControl.G;
	var oMC=oMap.getContainer();
  //DOM:button
	var oButton=this.initButton_(oMC);

	//DOM:map covers
	var o = document.createElement("div");
  	o.id='gzoom-map-cover';
	o.innerHTML='<div id="gzoom-outline" style="position:absolute;display:none;"></div><div id="gzoom-mct" style="position:absolute;display:none;"></div><div id="gzoom-mcl" style="position:absolute;display:none;"></div><div id="gzoom-mcr" style="position:absolute;display:none;"></div><div id="gzoom-mcb" style="position:absolute;display:none;"></div>';
	acl.style([o],{position:'absolute',display:'none',overflow:'hidden',cursor:'crosshair',zIndex:101});
	oMC.appendChild(o);

  // add event listeners
	GEvent.addDomListener(oButton, 'click', GSelectControl.prototype.buttonClick_);
	GEvent.addDomListener(o, 'mousedown', GSelectControl.prototype.coverMousedown_);
	GEvent.addDomListener(document, 'mousemove', GSelectControl.prototype.drag_);
	GEvent.addDomListener(document, 'mouseup', GSelectControl.prototype.mouseup_);

  // get globals
	G.oMapPos=acl.getElementPosition(oMap.getContainer());
	G.oOutline=$id("gzoom-outline");	
	G.oButton=$id("gzoom-control");
	G.mc=$id("gzoom-map-cover");
	G.mct=$id("gzoom-mct");
	G.mcr=$id("gzoom-mcr");
	G.mcb=$id("gzoom-mcb");
	G.mcl=$id("gzoom-mcl");
	G.oMap = oMap;

	G.nBorderCorrect = G.style.nOutlineWidth*2;	
  this.setDimensions_();

  //styles
  this.initStyles_();

  debug("Finished Initializing gzoom control");  
  return oButton;
};

// Default location for the control
GSelectControl.prototype.getDefaultPosition = function() {
  return new GControlPosition(G_ANCHOR_TOP_LEFT, new GSize(3, 120));
};

// ******************************************************************************************
// Private methods
// ******************************************************************************************
GSelectControl.prototype.coverMousedown_ = function(e){
  var G=GSelectControl.G;
  var oPos = GSelectControl.prototype.getRelPos_(e);
  debug("Mouse down at "+oPos.left+", "+oPos.top);
  G.nStartX=oPos.left;
  G.nStartY=oPos.top;
  
	acl.style([G.mc],{background:'transparent',opacity:1,filter:'alpha(opacity=100)'});
  acl.style([G.oOutline],{left:G.nStartX+'px',top:G.nStartY+'px',display:'block',width:'1px',height:'1px'});
  G.bDragging=true;

  G.mct.style.top=(G.nStartY-G.nMapHeight)+'px';
  G.mct.style.display='block';
  G.mcl.style.left=(G.nStartX-G.nMapWidth)+'px';
  G.mcl.style.top=(G.nStartY)+'px';
  G.mcl.style.display='block';

  G.mcr.style.left=(G.nStartX)+'px';
  G.mcr.style.top=(G.nStartY)+'px';
  G.mcr.style.display='block';
  G.mcb.style.left=(G.nStartX)+'px';
  G.mcb.style.top=(G.nStartY)+'px';
  G.mcb.style.width='0px';
  G.mcb.style.display='block';

	// invoke the callback if provided
	if (G.callbacks.dragStart !=null){G.callbacks.dragStart(G.nStartX,G.nStartY)};

  debug("mouse down done");
  return false;
};

GSelectControl.prototype.drag_=function(e){
  var G=GSelectControl.G;
  if(G.bDragging) {
    var oPos=GSelectControl.prototype.getRelPos_(e);
    oRec = GSelectControl.prototype.getRectangle_(G.nStartX,G.nStartY,oPos,G.nMapRatio);
    G.oOutline.style.width=oRec.nWidth+"px";
    G.oOutline.style.height=oRec.nHeight+"px";
    
    G.mcr.style.left=(oRec.nEndX+G.nBorderCorrect)+'px';
    G.mcb.style.top=(oRec.nEndY+G.nBorderCorrect)+'px';
    G.mcb.style.width=(oRec.nWidth+G.nBorderCorrect)+'px';
		
		// invoke callback if provided
		if (G.callbacks.dragging !=null){G.callbacks.dragging(G.nStartX,G.nStartY,oRec.nEndX,oRec.nEndY)};
		
    return false;
  }  
};
GSelectControl.prototype.mouseup_=function(e){
  var G=GSelectControl.G;
  if (G.bDragging) {
    var oPos = GSelectControl.prototype.getRelPos_(e);
    G.bDragging=false;
    
    var oRec = GSelectControl.prototype.getRectangle_(G.nStartX,G.nStartY,oPos,G.nMapRatio);
    debug("mouse up at "+oRec.nEndX+", "+oRec.nEndY+". Height/width="+oRec.nWidth+","+oRec.nHeight); 

    GSelectControl.prototype.resetDragZoom_();

		var nwpx=new GPoint(oRec.nStartX,oRec.nStartY);
		var nepx=new GPoint(oRec.nEndX,oRec.nStartY);
		var sepx=new GPoint(oRec.nEndX,oRec.nEndY);
		var swpx=new GPoint(oRec.nStartX,oRec.nEndY);
		var nw = G.oMap.fromContainerPixelToLatLng(nwpx); 
	    var ne = G.oMap.fromContainerPixelToLatLng(nepx); 
    	var se = G.oMap.fromContainerPixelToLatLng(sepx); 
    	var sw = G.oMap.fromContainerPixelToLatLng(swpx); 

		var outputlatlng ="latlng = nw,ne,se,sw = "+nw+","+ne+","+se+","+sw;
		var lat = G.oMap.fromContainerPixelToLatLng(nwpx).lat();
		var lng = G.oMap.fromContainerPixelToLatLng(nwpx).lng();
		//writeoutput(outputlatlng);
		showdata(nw,ne,se,sw);
		
    oZoomArea = new GPolyline([nw,ne,se,sw,nw],G.style.sOutlineColor,G.style.nOutlineWidth+1,.4);

    try{
      G.oMap.addOverlay(oZoomArea);
      //setTimeout (function(){G.oMap.removeOverlay(oZoomArea)},G.options.nOverlayRemoveMS);  
    }catch(e){
      jslog.error("error adding zoomarea overlay:"+e.message);
    }

   // oBounds=new GLatLngBounds(sw,ne);
   // nZoom=G.oMap.getBoundsZoomLevel(oBounds);
   // oCenter=oBounds.getCenter();
   // G.oMap.setCenter(oCenter, nZoom);

		// invoke callback if provided
	//	if (G.callbacks.dragEnd !=null){G.callbacks.dragEnd(nw,ne,se,sw,nwpx,nepx,sepx,swpx)};
		
		//re-init if sticky
		//if (G.options.bStickyZoom) {GSelectControl.prototype.initCover_()};		
  }
};

// set the cover sizes according to the size of the map
GSelectControl.prototype.setDimensions_=function() {
  var G=GSelectControl.G;
	if (G.options.bForceCheckResize){G.oMap.checkResize()};
  var oSize = G.oMap.getSize();
  G.nMapWidth  = oSize.width;
  G.nMapHeight = oSize.height;
  G.nMapRatio  = G.nMapHeight/G.nMapWidth;
	acl.style([G.mc,G.mct,G.mcr,G.mcb,G.mcl],{width:G.nMapWidth+'px', height:G.nMapHeight+'px'});
};

GSelectControl.prototype.initStyles_=function(){
  var G=GSelectControl.G;
	acl.style([G.mc,G.mct,G.mcr,G.mcb,G.mcl],{filter:G.style.sIEAlpha,opacity:G.style.nOpacity,background:G.style.sColor});
  G.oOutline.style.border=G.style.sBorder;  
  debug("done initStyles_");	
};

// The zoom button's click handler.
GSelectControl.prototype.buttonClick_=function(){
  if (GSelectControl.G.mc.style.display=='block'){ // reset if clicked before dragging
    GSelectControl.prototype.resetDragZoom_();
  } else {
		GSelectControl.prototype.initCover_();
	}
};

// Shows the cover over the map
GSelectControl.prototype.initCover_=function(){
  var G=GSelectControl.G;
	G.oMapPos=acl.getElementPosition(G.oMap.getContainer());
	GSelectControl.prototype.setDimensions_();
	GSelectControl.prototype.setButtonMode_('zooming');
	acl.style([G.mc],{display:'block',background:G.style.sColor});
	acl.style([G.oOutline],{width:'0px',height:'0px'});
	//invoke callback if provided
	if(GSelectControl.G.callbacks['buttonClick'] !=null){GSelectControl.G.callbacks.buttonClick()};
	debug("done initCover_");
};

GSelectControl.prototype.getRelPos_=function(e) {
  var oPos=acl.getMousePosition (e);
  var G=GSelectControl.G;
  return {top:(oPos.top-G.oMapPos.top),left:(oPos.left-G.oMapPos.left)};
};

GSelectControl.prototype.getRectangle_=function(nStartX,nStartY,oPos,nRatio){
	var dX=oPos.left-nStartX;
	var dY=oPos.top-nStartY;
	if (dX <0) dX =dX*-1;
	if (dY <0) dY =dY*-1;
	delta = dX > dY ? dX : dY;

  return {
    nStartX:nStartX,
    nStartY:nStartY,
    nEndX:nStartX+delta,
    nEndY:nStartY+parseInt(delta*nRatio),
    nWidth:delta,
    nHeight:parseInt(delta*nRatio)
  }
};

GSelectControl.prototype.resetDragZoom_=function() {
	var G=GSelectControl.G;
	//var output = "G.mc,G.mct,G.mcr,G.mcb,G.mcl = "+G.mc+","+G.mct+","+G.mcr+","+G.mcb+","+G.mcl;
	//writeoutput(output);
	acl.style([G.mc,G.mct,G.mcr,G.mcb,G.mcl],{display:'none',opacity:G.style.nOpacity,filter:G.style.sIEAlpha});
	G.oOutline.style.display='none';	
	GSelectControl.prototype.setButtonMode_('normal');
  debug("done with reset drag zoom");
};

/* alias get element by id */
function $id(sId) { return document.getElementById(sId); }
/* utility functions in acl namespace */
if (!window['acldefined']) {var acl={};window['acldefined']=true;}//only set the acl namespace once, then set a flag

/* A general-purpose function to get the absolute position of
the mouse */
acl.getMousePosition=function(e) {
	var posx = 0;
	var posy = 0;
	if (!e) var e = window.event;
	if (e.pageX || e.pageY) {
		posx = e.pageX;
		posy = e.pageY;
	} else if (e.clientX || e.clientY){
		posx = e.clientX + (document.documentElement.scrollLeft?document.documentElement.scrollLeft:document.body.scrollLeft);
		posy = e.clientY + (document.documentElement.scrollTop?document.documentElement.scrollTop:document.body.scrollTop);
	}	
	return {left:posx, top:posy};  
};

/*
To Use: 
	var pos = acl.getElementPosition(element);
	var left = pos.left;
	var top = pos.top;
*/
acl.getElementPosition=function(eElement) {
  var nLeftPos = eElement.offsetLeft;          // initialize var to store calculations
	var nTopPos = eElement.offsetTop;            // initialize var to store calculations
	var eParElement = eElement.offsetParent;     // identify first offset parent element  
	while (eParElement != null ) {                // move up through element hierarchy
		nLeftPos += eParElement.offsetLeft;      // appending left offset of each parent
		nTopPos += eParElement.offsetTop;  
		eParElement = eParElement.offsetParent;  // until no more offset parents exist
	}
	return {left:nLeftPos, top:nTopPos};
};
//elements is either a coma-delimited list of ids or an array of DOM objects. o is a hash of styles to be applied
//example: style('d1,d2',{color:'yellow'});  
acl.style=function(a,o){
	if (typeof(a)=='string') {a=acl.getManyElements(a);}
	for (var i=0;i<a.length;i++){
		for (var s in o) { a[i].style[s]=o[s];}
	}
};
acl.getManyElements=function(s){		
	t=s.split(',');
	a=[];
	for (var i=0;i<t.length;i++){a[a.length]=$id(t[i])};
	return a;
};
	
var jslog = {debug:function(){},info:function(){}, 
	warning:function(){}, error:function(){},
	text:function(){}}; var debug=function(){};
if (location.href.match(/enablejslog/)){
		document.write('<script type="text/javascript" src="http://earthcode.com/includes/scripts/jslog.js"></script>');};	

		
		function GSelectControl(oBoxStyle,oOptions,oCallbacks) {
	//box style options
  GSelectControl.G.style = {
  	position:'absolute',top:'0',right:'0',
    nOpacity:.2,
    sColor:"#000",
    sBorder:"2px solid blue"
  };
  var style=GSelectControl.G.style;
  for (var s in oBoxStyle) {style[s]=oBoxStyle[s]};
  var aStyle=style.sBorder.split(' ');
  style.nOutlineWidth=parseInt(aStyle[0].replace(/\D/g,''));
  style.sOutlineColor=aStyle[2];
  style.sIEAlpha='alpha(opacity='+(style.nOpacity*100)+')';
	
	// Other options
	GSelectControl.G.options={
		bForceCheckResize:false,
		sButtonHTML:'<b>Select</b>',
		oButtonStartingStyle:{width:'40px',border:'1px solid black',padding:'0px 5px 1px 5px'},
		oButtonStyle:{background:'#FFF'},
		sButtonZoomingHTML:'Select a region on the map',
		oButtonZoomingStyle:{background:'#EEE'},
		nOverlayRemoveMS:6000,
		bStickyZoom:false
	};
	
	for (var s in oOptions) {GSelectControl.G.options[s]=oOptions[s]};
	
	// callbacks: buttonClick, dragStart,dragging, dragEnd
	if (oCallbacks == null) {oCallbacks={}};
	GSelectControl.G.callbacks=oCallbacks;
}
		