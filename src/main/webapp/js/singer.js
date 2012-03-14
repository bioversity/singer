//this function includes all necessary js files for the application
function include(file)
{
 
	var script  = document.createElement('script');
	script.src  = file;
	script.type = 'text/javascript';
	script.defer = true;

	document.getElementsByTagName('head').item(0).appendChild(script);

}

include('js/AjaxRequest.js');

function autoCompleteTaxon(input, select)
{
	setOptions('autoCompletion.do?auto='+input.value, select);
}

function setOptions(url, select) {
	  AjaxRequest.get(
	    {
	      'url':url
	      ,'onSuccess':function(req){generateOptions(req.responseXML, select); }
	    }
	  );
}

function generateOptions(xml, select)
{
	var genusspecies=xml.getElementsByTagName("genusspecies");
	if(genusspecies.length!=0)
	{
		var options="";
		if(genusspecies.length>1)
		{
			for (var i=0; i < genusspecies.length; ++i)
			{
				options+="<option value='"+genusspecies[i].getElementsByTagName('id')[0].firstChild.nodeValue+"'>";
				options+=genusspecies[i].getElementsByTagName('name')[0].firstChild.nodeValue;
				options+="</option>";
			}
		}
		else
		{
			options+="<option value='"+genusspecies[0].getElementsByTagName('id')[0].firstChild.nodeValue+"' selected='true'>";
			options+=genusspecies[0].getElementsByTagName('name')[0].firstChild.nodeValue;
			options+="</option>";
		}
	}

	select.innerHTML=options;
}

var fieldNumber =1;
var fieldPosition=1;

function addField(select, fieldtable)
{
	var newField = document.getElementById('fieldToAdd').cloneNode(true);
	var text = select.options[select.selectedIndex].text;
	var fieldtr=document.getElementById('fieldToAdd');
	AjaxRequest.get(
		    {
		      'url':'addField.do?field='+select.value+'&n='+fieldNumber+'&text='+text
		      ,'onSuccess':function(req){
		    	addTr(req, fieldtable); 
		    		}
		    }
		  );
	fieldNumber++;	
}

function removeField (field)
{
	field.parentNode.removeChild(field);
	fieldPosition--;
}

function addTr(req, table)
{
	var xml = req.responseXML;
	var head = xml.getElementsByTagName("head")[0];
	var newRow = table.insertRow(fieldPosition);
	newRow.id="field"+(fieldNumber-1);
	var cell = newRow.insertCell(0);
	cell.className="dispHead";
	cell.innerHTML=head.firstChild.nodeValue;
	cell=newRow.insertCell(1)
	var value = xml.getElementsByTagName("value")[0];
	cell.className=value.getAttribute("class");
	var cellInnerHTML ="";
	var name = value.firstChild.getAttribute("name");
	if(value.firstChild.tagName=="select")
	{
		
		if(name=="gecspc")
		{
			var autoCompleteRow = table.insertRow(fieldPosition);
			autoCompleteRow.id="auto"+(fieldNumber-1);
			var newCell = autoCompleteRow.insertCell(0);
			newCell.className="dispHead";
			newCell.innerHTML="Look for taxon";
			newCell=autoCompleteRow.insertCell(1);
			newCell.innerHTML ="<input type='text' onkeyup=\"autoCompleteTaxon(this,document.getElementById('select"+(fieldNumber-1)+"'));\" name='' />";
			fieldPosition++;
		}
		cellInnerHTML+="<select name='"+name+"' multiple='true' rows='3' id='select"+(fieldNumber-1)+"'>"
		var options = value.getElementsByTagName("option");
		for(var i=0; i<options.length;i++)
		{
			var optValue = options[i].getAttribute("value");
			var text = options[i].firstChild.nodeValue;
			cellInnerHTML+="<option value='"+optValue+"'>";
			cellInnerHTML+=text;
			cellInnerHTML+="</option>";
		}
		cellInnerHTML+="</select>";
	}
	else
	{
		var name = value.firstChild.getAttribute("name");
		cellInnerHTML+="Lower bound:&nbsp;&nbsp;";
		cellInnerHTML+="<input type='text' name='"+name+"' maxlength='5' value=''/> ";
		cellInnerHTML+="<br />";
		cellInnerHTML+="Upper bound:&nbsp;&nbsp;";
		cellInnerHTML+="<input type='text' name='"+name+"' maxlength='5' value='' />";
	}
	if(name=="gecspc")
	{
		cellInnerHTML+= "&nbsp;&nbsp;<a ><img title='Click here to remove this field ' src='/img/remove_icon.gif' onclick=\"removeField(document.getElementById('field"+(fieldNumber-1)+"'));removeField(document.getElementById('auto"+(fieldNumber-1)+"'));\" style='position:absolute;' /></a>";
	}
	else
	{
		cellInnerHTML+= "&nbsp;&nbsp;<a ><img title='Click here to remove this field ' src='/img/remove_icon.gif' onclick=\"removeField(document.getElementById('field"+(fieldNumber-1)+"'));\" style='position:absolute;' /></a>";
	}
	
	cell.innerHTML = cellInnerHTML;
	
	fieldPosition++;
	
	
}