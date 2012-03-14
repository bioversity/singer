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
function Bs_FormFieldSelect() {}
Bs_FormFieldSelect.prototype.hasValue = function(val) {
val = val + '';for (var i=0; i<this.length; i++) {
var t = this.options[i].value + '';if (t == val) return true;}
return false;}
Bs_FormFieldSelect.prototype.getValue = function() {
var selIndex = this.selectedIndex;if ((selIndex != 'undefined') && (selIndex > -1)) {
if (typeof(this.options[selIndex].value) != 'undefined') return this.options[selIndex].value;if (typeof(this.options[selIndex].text)  != 'undefined') return this.options[selIndex].text;}
return 'undefined';}
Bs_FormFieldSelect.prototype.getValueOrText = function(selIndex) {
if (typeof(selIndex) == 'undefined') selIndex = this.selectedIndex;if ((selIndex != 'undefined') && (selIndex > -1)) {
if (typeof(this.options[selIndex].value) != 'undefined') {
if (typeof(this.options[selIndex].outerHTML) == 'string') {
if (this.options[selIndex].outerHTML.toLowerCase().indexOf('value=') != -1) {
return this.options[selIndex].value;}
} else {
if (this.options[selIndex].value != '') return this.options[selIndex].value;}
}
if (typeof(this.options[selIndex].text)  != 'undefined') return this.options[selIndex].text;}
return false;}
Bs_FormFieldSelect.prototype.getTextForValue = function(value) {
for (var i=0; i<this.options.length; i++) {
if (this.options[i].value == value) {
return this.options[i].text;}
}
return false;}
Bs_FormFieldSelect.prototype.setTo = function(compare, type) {
if (typeof(type) == 'undefined') type = 'text';for (var i=0; i<this.length; i++) {
if (this.options[i][type] == compare) {
this.selectedIndex = i;return true;}
}
return false;}
Bs_FormFieldSelect.prototype.selectAll = function() {
for (var i=0; i<this.length; i++) {
this.options[i].selected = true;}
}
Bs_FormFieldSelect.prototype.moveSelectedTo = function(toField, keepSelected) {
if (typeof(toField) == 'string') toField = document.getElementById(toField);if (bs_isNull(toField)) return false;var unsetArray = new Array();for (var i=0; i<this.length; i++) {
if (this.options[i].selected) {
var newOpt = new Option(this.options[i].text, this.options[i].value, false, false);toField.options[toField.length] = newOpt;unsetArray[unsetArray.length] = i;}
}
unsetArray.reverse();for (var i=0; i<unsetArray.length; i++) {
this.options[unsetArray[i]] = null;}
return true;}
Bs_FormFieldSelect.prototype.moveAllTo = function(toField) {
if (typeof(toField) == 'string') toField = document.getElementById(toField);if (bs_isNull(toField)) return false;var unsetArray = new Array();for (var i=0; i<this.length; i++) {
var newOpt = new Option(this.options[i].text, this.options[i].value, false, false);toField.options[toField.length] = newOpt;unsetArray[unsetArray.length] = i;}
unsetArray.reverse();for (var i=0; i<unsetArray.length; i++) {
this.options[unsetArray[i]] = null;}
return true;}
Bs_FormFieldSelect.prototype.moveTo = function(toField, optionValue) {
if (typeof(toField) == 'string') toField = document.getElementById(toField);if (bs_isNull(toField)) return false;var unsetArray = new Array();for (var i=0; i<this.options.length; i++) {
if (this.options[i].value == optionValue) {
var newOpt = new Option(this.options[i].text, this.options[i].value, false, false);toField.options[toField.length] = newOpt;unsetArray[unsetArray.length] = i;break;}
}
unsetArray.reverse();for (var i=0; i<unsetArray.length; i++) {
this.options[unsetArray[i]] = null;}
return true;}
Bs_FormFieldSelect.prototype.moveHashTo = function(toField, hash) {
if (typeof(toField) == 'string') toField = document.getElementById(toField);if (bs_isNull(toField)) return false;var unsetArray = new Array();for (var i=0; i<this.length; i++) {
if (typeof(hash[this.options[i].value]) != 'undefined') {
var newOpt = new Option(this.options[i].text, this.options[i].value, false, false);toField.options[toField.length] = newOpt;unsetArray[unsetArray.length] = i;}
}
unsetArray.reverse();for (var i=0; i<unsetArray.length; i++) {
this.options[unsetArray[i]] = null;}
return true;}
Bs_FormFieldSelect.prototype.getAllKeys = function() {
var ret = new Array();for (var i=0; i<this.options.length; i++) {
ret[i] = this.options[i].value;}
return ret;}
Bs_FormFieldSelect.prototype.getAllOptions = function() {
var ret = new Array();for (var i=0; i<this.options.length; i++) {
var key = this.getValueOrText(i);ret[key] = this.options[i].text;}
return ret;}
Bs_FormFieldSelect.prototype.prune = function() {
this.options.length = 0;}
Bs_FormFieldSelect.prototype.addElement = function() {
}
Bs_FormFieldSelect.prototype.addElementsByHash = function(dataHash) {
var i = 0;for (var key in dataHash) {
var newOpt = new Option(dataHash[key], key, false, false);this.options[this.options.length] = newOpt;i++;}
return i;}
Bs_FormFieldSelect.prototype.sortByText = function(desc, natural) {
if (typeof(desc)    == 'undefined') desc    = this._param1;if (typeof(natural) == 'undefined') natural = this._param2;var sortArr = new Array;for (var i=0; i<this.length; i++) {
if (this.options[i].value == 'undefined') this.options[i].value = this.options[i].text;var bool = (this.options[i].selected) ? '1' : '0';sortArr[i] = this.options[i].text + '__BS_SORT__' + this.options[i].value + '_' + bool;}
sortArr.sort();if (desc) sortArr.reverse();this.prune();var key = '';var txt = '';for (var i=0; i<sortArr.length; i++) {
var pos = sortArr[i].lastIndexOf('__BS_SORT__');txt = sortArr[i].substr(0, pos);key = sortArr[i].substr(pos + '__BS_SORT__'.length);var selected = (key.substr(key.length -1) == '1') ? true : false;key = key.substr(0, key.length -2);var newOpt = new Option(txt, key, selected, selected);this.options[this.options.length] = newOpt;}
}
Bs_FormFieldSelect.prototype.sortByKey = function() {
}
Bs_FormFieldSelect.prototype.setText = function(value, text) {
for (var i=0; i<this.length; i++) {
if (this.options[i].value == value) {
this.options[i].text = text
return true;}
}
return false;}
Bs_FormFieldSelect.prototype.removeElement = function(value) {
if (typeof(value) == 'undefined') value = this._param1;for (var i=0; i<this.options.length; i++) {
if (this.options[i].value == value) {
this.options[i] = null;return true;}
}
return false;}
Bs_FormFieldSelect.prototype.init = function(formField) {
if (formField == null) return;for (var name in this) {
if (name == 'init') continue;formField[name] = this[name];}
}
