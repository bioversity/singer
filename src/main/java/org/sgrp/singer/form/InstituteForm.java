package org.sgrp.singer.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.sgrp.singer.AccessionConstants;

@SuppressWarnings("serial")
public class InstituteForm extends GenericForm {

	public static void setSearchAttributes(HttpServletRequest request) {
	}

	public String	address;
	public String	caddress;
	public String	cemail;
	public String	contact;
	public String	ctycode;
	public String	ctyname;
	public String	email;
	public String	faocode;
	public String	fax;
	public String	fullname;
	public String	instid;
	public String	logo;
	public String	name;
	public String	phone;
	public String	url;

	public InstituteForm() {
		super();
	}

	public InstituteForm(String instid) {
		super();
		this.instid = instid;
	}

	public String getAddress() {
		return address;
	}

	public String getCaddress() {
		return caddress;
	}

	public String getCemail() {
		return cemail;
	}

	public String getContact() {
		return contact;
	}

	public String getCtycode() {
		return ctycode;
	}

	public String getCtyname() {
		return ctyname;
	}

	public String getEmail() {
		return email;
	}

	public String getFaocode() {
		return faocode;
	}

	public String getFax() {
		return fax;
	}

	public String getFullname() {
		return fullname;
	}

	public String getInstid() {
		return instid;
	}

	public String getLogo() {
		return logo;
	}

	public String getName() {
		return name;
	}

	public String getPhone() {
		return phone;
	}

	public String getUrl() {
		return url;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setCaddress(String caddress) {
		this.caddress = caddress;
	}

	public void setCemail(String cemail) {
		this.cemail = cemail;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public void setCtycode(String ctycode) {
		this.ctycode = ctycode;
	}

	public void setCtyname(String ctyname) {
		this.ctyname = ctyname;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setFaocode(String faocode) {
		this.faocode = faocode;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public void setInstid(String instid) {
		this.instid = instid;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest request) {
		System.out.println("In Institute Form");
		ActionErrors errors = new ActionErrors();
		setSearchAttributes(request);
		if (errors.size() > 0) {
			status = AccessionConstants.FAILURE;
		} else {
			status = AccessionConstants.SUCCESS;
		}
		return errors;
	}
}
