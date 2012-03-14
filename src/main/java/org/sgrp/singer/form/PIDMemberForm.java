package org.sgrp.singer.form;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.sgrp.singer.AccessionConstants;
import org.sgrp.singer.AccessionServlet;
import org.sgrp.singer.SearchResults;
import org.sgrp.singer.db.UserManager;

@SuppressWarnings("serial")
public class PIDMemberForm extends GenericForm {

	public String	nuserpid				= null;

	public String	nfname				= null;

	public String	nlname				= null;

	public String	niname				= null;

	public String	nShippingAddress	= null;
	
	public String	nZip				= null;
	
	public String	nCity				= "";

	public String	nemail				= null;
	
	public String	nCountry			= null;
	
	public String	nType				= null;
	
	public String	nCompany			= null;
	
	private boolean	validated			= false;
	
	public String getNCompany() {
		return nCompany;
	}

	public void setNCompany(String company) {
		nCompany = company;
	}

	public String getNShippingAddress() {
		return nShippingAddress;
	}

	public void setNShippingAddress(String nShippingAddress) {
		this.nShippingAddress = nShippingAddress;
	}

	public String getNZip() {
		return nZip;
	}

	public void setNZip(String nZip) {
		this.nZip = nZip;
	}

	public String getNCity() {
		return nCity;
	}

	public void setNCity(String nCity) {
		this.nCity = nCity;
	}

	public boolean isValidated() {
		return validated;
	}

	public void setValidated(boolean validated) {
		this.validated = validated;
	}

	public PIDMemberForm() {
		super();
	}

	public PIDMemberForm(String nuserpid) {
		super();
		this.nuserpid = nuserpid;
	}

	@Override
	public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest httpServletRequest) {
		return new ActionErrors();
	}

	public static void setSearchAttributes(HttpServletRequest request) {
		request.setAttribute("countries", AccessionServlet.getKeywords().getAllOrigMap());		
		request.setAttribute("users", AccessionServlet.getKeywords().getAllUserMap());
	}

	public String getNemail() {
		return nemail;
	}

	public void setNemail(String nemail) {
		this.nemail = nemail;
	}

	public String getNfname() {
		return nfname;
	}

	public void setNfname(String nfname) {
		this.nfname = nfname;
	}

	public String getNlname() {
		return nlname;
	}

	public void setNlname(String nlname) {
		this.nlname = nlname;
	}

	public String getNuserpid() {
		return nuserpid;
	}

	public void setNuserpid(String nuserpid) {
		this.nuserpid = nuserpid;
	}

	public String getNiname() {
		return niname;
	}

	public void setNiname(String niname) {
		this.niname = niname;
	}

	public String getNCountry() {
		return nCountry;
	}

	public void setNCountry(String nCountry) {
		this.nCountry = nCountry;
	}

	public String getNType(){
		return this.nType;
	}
	
	public void setNType(String nType){
		this.nType=nType;
	}
	
}
