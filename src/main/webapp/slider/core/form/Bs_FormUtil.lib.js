/********************************************************************************************
* BlueShoes Framework; This file is part of the php application framework.
* NOTE: This code is stripped (obfuscated). To get the clean documented code goto 
*       www.blueshoes.org and register for the free open source *DEVELOPER* version or 
*       buy the commercial version.
*       
*       In case you've already got the developer version, then this is one of the few 
*       packages/classes that is only available to *PAYING* customers.
*       To get it go to www.blueshoes.org and buy a commercial version.
* 
* @copyright www.blueshoes.org
* @author    sam blum <sam-at-blueshoes>
* @author    Andrej Arn <andrej-at-blueshoes>
*/
function bsFormGetFormForField(fieldElm) {
if (document.forms.length == 1) return document.forms[0];if (fieldElm.tagName == 'form') return fieldElm;if (fieldElm.parentNode) return bsFormGetFormForField(fieldElm.parentNode);return false;}
function bsFormGetNextField(fieldElm) {
var formElm = bsFormGetFormForField(fieldElm);if (!formElm) return false;var useNext = false;for (var i=0; i<formElm.elements.length; i++) {
if (useNext) return formElm.elements[i];if (formElm.elements[i] == fieldElm) useNext = true;}
return false;}
function bsFormToggleCheckbox(formName, fieldName) {
try {
if (document.forms[formName].elements[fieldName].checked) {
document.forms[formName].elements[fieldName].checked = false;} else {
document.forms[formName].elements[fieldName].checked = true;}
} catch (e) {
}
}
function bsFormToggleContainer(containerName) {
if (document.getElementById) {
var elm = document.getElementById(containerName);if (typeof(elm) != 'undefined') {
elm.style.display = (elm.style.display == 'none') ? 'block' : 'none';var img = document.getElementById('contToggleImg' + containerName.substr(9));if (typeof(img) != 'undefined') {
if (elm.style.display == 'none') {
img.src = img.src.substr(0, img.src.length -6) + 'down.gif';} else {
img.src = img.src.substr(0, img.src.length -8) + 'up.gif';}
}
var txtO = document.getElementById('contToggleTextO' + containerName.substr(9));var txtC = document.getElementById('contToggleTextC' + containerName.substr(9));if (typeof(txtO) != 'undefined') {
if (elm.style.display == 'none') {
txtO.style.display = 'inline';txtC.style.display = 'none';} else {
txtO.style.display = 'none';txtC.style.display = 'inline';}
}
return;}
}
if (document.all) {
if (document.all[containerName].style.display == "none") {
document.all[containerName].style.display = "block";} else {
document.all[containerName].style.display = "none";}
}
}
function bsFormCheckOnlyIf(formName, fieldName, fieldElement) {
if (typeof(bsFormVars) == 'undefined') return;if (typeof(bsFormVars[formName]) == 'undefined') return;for (var otherFieldName in bsFormVars[formName]) {
if (typeof(bsFormVars[formName][otherFieldName]['onlyIf']) == 'undefined') continue;var status = _bsForm_anyIfCase(bsFormVars[formName][otherFieldName]['onlyIf']);var elm = document.getElementById(otherFieldName);if ((typeof(elm) != 'undefined') && (elm != null)) {
if (!status) {
elm.disabled = true;} else {
elm.disabled = false;}
}
}
}
function bsFormGetFieldValue(elementID) {
var elm  = document.getElementById(elementID);if ((typeof(elm) == 'undefined') || (elm == null) || (elm.id != elementID)) {
var elm  = document.getElementById(elementID + '0');var elm2 = document[elm.form.name][elm.name];elm = elm2;} else {
var elm2 = document[elm.form.name][elm.name];}
for (var i=0; i<elm2.length; i++) {
if (elm2[i].checked) return elm2[i].value;}
return elm.value;}
function _bsForm_anyIfCase(myIf) {
if ((typeof(myIf) == 'object') && (myIf.length > 0)) {
var stack = new Array();for (i=0; i<myIf.length; i++) {
stack[i] = new Array();stack[i]['operator'] = (typeof(myIf[i]['operator']) != 'undefined') ? myIf[i]['operator'] : '|';stack[i]['compare']  = (typeof(myIf[i]['compare'])  != 'undefined') ? myIf[i]['compare']  : '=';stack[i]['boolean']  = false;do {
if (typeof(myIf[i]['value']) != 'undefined') {
var t = bsFormGetFieldValue(myIf[i]['field']);if (typeof(t) != null) {
switch (stack[i]['compare']) {
case '=':
stack[i]['boolean'] = (t == myIf[i]['value']);break;case '>':
stack[i]['boolean'] = (t > myIf[i]['value']);break;case '<':
stack[i]['boolean'] = (t < myIf[i]['value']);break;case '>=':
stack[i]['boolean'] = (t >= myIf[i]['value']);break;case '<=':
stack[i]['boolean'] = (t <= myIf[i]['value']);break;case '!=':
case '<>':
stack[i]['boolean'] = (t != myIf[i]['value']);break;default:
}
break;}
} else {
break;}
} while (false);}
if ((typeof(stack) == 'object') && (stack.length > 0)) {
var evalStr = '';for (var i=0; i<stack.length; i++) {
if (i > 0) evalStr += (stack[i]['operator'] == '&') ? '&& ' : '|| ';evalStr += (stack[i]['boolean']) ? 'true ' : 'false ';}
evalStr = ' (' + evalStr + ');';if (eval(evalStr)) {
return true;}
}
}
return false;}
function bsFormCheckMail(url, fieldObj, checkType) {
var fieldName = fieldObj.name;var fieldID   = fieldObj.id;var email     = fieldObj.value;var iFrameObj = document.getElementById('bsMailCheck' + fieldName);url += "?email=" + email + "&checkType=" + checkType;var zeit = new Date();url += "&random=" + zeit.getMilliseconds();iFrameObj.src = url;}
function bsFormJumpToFirstError(fieldName, formName, doSelect) {
try {
if (document.forms[formName].elements[fieldName]) {
if (doSelect && (document.forms[formName].elements[fieldName].value != '')) {
if (document.forms[formName].elements[fieldName].select) {
document.forms[formName].elements[fieldName].select();}
}
if (document.forms[formName].elements[fieldName].focus) {
document.forms[formName].elements[fieldName].focus();}
}
} catch (e) {
}
}
function bsFormEnterSubmit(ev, myForm) {
var ev = ('object' == typeof(window.event)) ? window.event : ev;if (ev && ev.keyCode == 13) {
myForm.submit();}
return true;}
function bsFormNoEnter(ev) {
if (typeof(ev) == 'undefined') ev = window.event;if (ev) return (ev.keyCode != 13);return true;}
function bsFormEnterToTab(ev) {
ev = ('object' == typeof(window.event)) ? window.event : ev;if ((ev && (ev.keyCode == 13)) || (ev.which && (ev.which == 13))) {
if ((typeof(ie) == 'undefined') || ie) {
ev.keyCode = 9;} else {
var nextField = bsFormGetNextField(ev.srcElement);if (nextField) {
try {
nextField.focus();} catch (e) {
}
}
return false;}
}
return true;}
function bsFormHandleEnter(ev, functionName) {
var ev = ('object' == typeof(window.event)) ? window.event : ev;if (ev && ev.keyCode == 13) {
return eval(functionName + '();');}
return true;}
function bsFormFieldSetFocusAndSelect(field, force) {
if (typeof(field) == 'string') {
field = document.getElementById(field);}
if (!field) return false;try {
if (force || !field.hasFocus) {
field.focus();field.select();}
} catch (e) {
return false;}
return true;}
function rc4encryptFormValues(formName, passPhraze) {
for(var i=0;i<document[formName].length;++i) {
var elm = document[formName].elements[i];if (typeof(elm.name) == 'undefined') continue;if (elm.name.substr(0, 8) == 'bs_form[') continue;if ((typeof(elm.value) != 'undefined') && (typeof(elm.value) != 'object') && (elm.value != '')) {
switch (elm.type) {
case 'text':
case 'password':
case 'hidden':
elm.value = '_crp_' + base64_encode(rc4crypt(passPhraze, elm.value));break;}
}
}
}
function flowerPower1(formName, passPhraze) {
rc4encryptFormValues(formName, passPhraze);}
function rc4decryptFormValues(formName, passPhraze) {
for(var i=0;i<document[formName].length;++i) {
var elm = document[formName].elements[i];if ((typeof(elm.value) != 'undefined') && (elm.value != '') && (elm.value.substr(0, 5) == '_crp_')) {
elm.value = rc4crypt(passPhraze, base64_decode(elm.value.substr(5)));}
}
}
function flowerPower2(formName, passPhraze) {
rc4decryptFormValues(formName, passPhraze);}
function bsFormDoHiddenSubmit(exitScreen, exitAction, nextScreen, nextAction, dataHash, submitToAction) {
var formOutArray =  new Array();var ii=0;formOutArray[ii++] = '<form name="smSubmitForm" action="' + submitToAction + '" method="post">';formOutArray[ii++] = '<input type="hidden" name="bs_todo[nextScreen]" value="' + nextScreen + '">';formOutArray[ii++] = '<input type="hidden" name="bs_todo[exitScreen]" value="' + exitScreen + '">';switch (typeof(nextAction)) {
case 'string':
formOutArray[ii++] = '<input type="hidden" name="bs_todo[nextAction]" value="' + nextAction + '">';break;case 'object':
for (var key in nextAction) {
formOutArray[ii++] = '<input type="hidden" name="bs_todo[nextAction][' + key + ']" value="' + nextAction[key] + '">';}
default:
}
switch (typeof(exitAction)) {
case 'string':
formOutArray[ii++] = '<input type="hidden" name="bs_todo[exitAction]" value="' + exitAction + '">';break;case 'object':
for (var key in exitAction) {
formOutArray[ii++] = '<input type="hidden" name="bs_todo[exitAction][' + key + ']" value="' + exitAction[key] + '">';}
default:
}
dataHash = _recursiveObj2Hash(dataHash);for (var matrixStr in dataHash) {
if (typeof(dataHash[matrixStr]) == 'function') continue;var valStr = bs_filterForHtml(dataHash[matrixStr] + '');formOutArray[ii++] = '<input type="hidden" name="' + "bs_todo[dataHash]" + matrixStr + '" value="' + valStr +  '">';}
formOutArray[ii++] = '</form>';var body = document.getElementsByTagName('body').item(0);body.innerHTML = formOutArray.join('');var form = document.smSubmitForm;form.submit();}
function _recursiveObj2Hash(aObject, matrixStr, flatObjHash) {
if (!flatObjHash) {
flatObjHash = new Object();matrixStr = '';}
if (typeof(aObject) != 'object') {
flatObjHash[matrixStr] = aObject;} else {
for (var key in aObject) {
var newMatrixStr = matrixStr + '['+key+']';_recursiveObj2Hash(aObject[key], newMatrixStr, flatObjHash);}
}
return flatObjHash;}
