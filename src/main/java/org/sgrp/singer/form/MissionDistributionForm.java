package org.sgrp.singer.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.sgrp.singer.AccessionConstants;

@SuppressWarnings("serial")
public class MissionDistributionForm extends GenericForm {

	public static void setSearchAttributes(HttpServletRequest request) {
	}

	public String	code;
	public String	misscode;
	public String	sp;
	public String	tinstcode;
	public String	samp;
	public String	faocode;
	public String	acronym;
	public String	name;
	public String	street;
	public String	city;
	public String	zip;
	public String	tlf;
	public String	tlx;
	public String	fax;
	public String	email;

	public MissionDistributionForm() {
		super();
	}

	public MissionDistributionForm(String code) {
		super();
		this.code = code;
	}

	@Override
	public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest request) {
		System.out.println("In  Form");
		ActionErrors errors = new ActionErrors();
		setSearchAttributes(request);
		if (errors.size() > 0) {
			status = AccessionConstants.FAILURE;
		} else {
			status = AccessionConstants.SUCCESS;
		}
		return errors;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSp() {
		return sp;
	}

	public void setSp(String sp) {
		this.sp = sp;
	}

	public String getTinstcode() {
		return tinstcode;
	}

	public void setTinstcode(String tinstcode) {
		this.tinstcode = tinstcode;
	}

	public String getSamp() {
		return samp;
	}

	public void setSamp(String samp) {
		this.samp = samp;
	}

	public String getFaocode() {
		return faocode;
	}

	public void setFaocode(String faocode) {
		this.faocode = faocode;
	}

	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getTlf() {
		return tlf;
	}

	public void setTlf(String tlf) {
		this.tlf = tlf;
	}

	public String getTlx() {
		return tlx;
	}

	public void setTlx(String tlx) {
		this.tlx = tlx;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMisscode() {
		return misscode;
	}

	public void setMisscode(String misscode) {
		this.misscode = misscode;
	}

}
