package org.sgrp.singer.form;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.sgrp.singer.AccessionConstants;
import org.sgrp.singer.AccessionServlet;
import org.sgrp.singer.SearchResults;

@SuppressWarnings("serial")
public class AccessionForm extends GenericForm {

	public static void setSearchAttributes(HttpServletRequest request) {
		// request.setAttribute("divisions",
		// AccessionServlet.getKeywords().getAllDivisionMap());
		request.setAttribute("collections", AccessionServlet.getKeywords().getAllColMap());
		request.setAttribute("statuses", AccessionServlet.getKeywords().getAllStatusMap());
		request.setAttribute("sources", AccessionServlet.getKeywords().getAllSrcMap());
		request.setAttribute("countries", AccessionServlet.getKeywords().getAllOrigMap());
		/* Disabled by Gautier in order to clean the taxon List 
		request.setAttribute("taxons", AccessionServlet.getKeywords().getAllTaxMap());*/
		/* Added by Gautier in order to clean the taxon List */
		
		Map<String, String> taxons = new LinkedHashMap<String, String>();
		Map<String, String> allGenusSpecies = AccessionServlet.getKeywords().getAllGenusSpeciesMap();
		Iterator<String> iteGenus = allGenusSpecies.keySet().iterator();
		while(iteGenus.hasNext())
		{
			String genusSpeciesId = iteGenus.next();
			String genusSpeciesName = allGenusSpecies.get(genusSpeciesId);
			
			String key = genusSpeciesId;
			String value = AccessionConstants.makeProper(genusSpeciesName);
			taxons.put(key, value);
		}
		request.setAttribute("taxons", taxons);
		request.setAttribute("trusts", AccessionServlet.getKeywords().getAllTrustMap());
	}

	public String	acceid;

	public String	accename;

	public String	accenumb;

	public String	acqdate;

	public String	availability;

	public String	collcode;

	public String	colldate;

	public String	collname;

	public String	collnumb;

	public String	collsite;

	public String	donorcode;

	public String	elevation;

	public String	genuscode;

	public String	genusname;

	public String	instcode;

	public String	instname;

	public String	insvalbard;

	public String	latitude;

	public String	latituded;

	public String	latres;

	public String	longitude;

	public String	longituded;

	public String	lonres;

	public String	misscode;

	public String	origacceid;

	public String	origcode;

	public String	origname;

	public String	othernumb;

	public String	parentfemale;

	public String	parentmale;

	public String	pedigree;

	public String	speciescode;

	public String	speciesname;

	public String	srccode;

	public String	srcname;

	public String	statcode;

	public String	statname;

	public String	taxcode;

	public String	taxname;

	public String	trustcode;

	public String	trustname;

	public AccessionForm() {
		super();
	}

	public AccessionForm(String acceid) {
		super();
		this.acceid = acceid;
	}

	public String getAcceid() {
		return acceid;
	}

	public String getAccename() {
		return accename;
	}

	public String getAccenumb() {
		return accenumb;
	}

	public String getAcqdate() {
		return acqdate;
	}

	public String getAvailability() {
		return availability;
	}

	public String getCollcode() {
		return collcode;
	}

	public String getColldate() {
		return colldate;
	}

	public String getCollname() {
		return collname;
	}

	public String getCollnumb() {
		return collnumb;
	}

	public String getCollsite() {
		return collsite;
	}

	public String getDonorcode() {
		return donorcode;
	}

	public String getElevation() {
		return elevation;
	}

	public String getGenuscode() {
		return genuscode;
	}

	public String getGenusname() {
		return genusname;
	}

	public String getInstcode() {
		return instcode;
	}

	public String getInstname() {
		return instname;
	}

	public String getInsvalbard() {
		return insvalbard;
	}

	public String getLatitude() {
		return latitude;
	}

	public String getLatituded() {
		return latituded;
	}

	public String getLatres() {
		return latres;
	}

	public String getLongitude() {
		return longitude;
	}

	public String getLongituded() {
		return longituded;
	}

	public String getLonres() {
		return lonres;
	}

	public String getMisscode() {
		return misscode;
	}

	public String getOrigacceid() {
		return origacceid;
	}

	public String getOrigcode() {
		return origcode;
	}

	public String getOrigname() {
		return origname;
	}

	public String getOthernumb() {
		return othernumb;
	}

	public String getParentfemale() {
		return parentfemale;
	}

	public String getParentmale() {
		return parentmale;
	}

	public String getPedigree() {
		return pedigree;
	}

	public String getSpeciescode() {
		return speciescode;
	}

	public String getSpeciesname() {
		return speciesname;
	}

	public String getSrccode() {
		return srccode;
	}

	public String getSrcname() {
		return srcname;
	}

	public String getStatcode() {
		return statcode;
	}

	public String getStatname() {
		return statname;
	}

	public String getTaxcode() {
		return taxcode;
	}

	public String getTaxname() {
		return taxname;
	}

	public String getTrustcode() {
		return trustcode;
	}

	public String getTrustname() {
		return trustname;
	}

	public void setAcceid(String acceid) {
		this.acceid = acceid;
	}

	public void setAccename(String accename) {
		this.accename = accename;
	}

	public void setAccenumb(String accenumb) {
		this.accenumb = accenumb;
	}

	public void setAcqdate(String acqdate) {
		this.acqdate = acqdate;
	}

	public void setAvailability(String availability) {
		this.availability = availability;
	}

	public void setCollcode(String collcode) {
		this.collcode = collcode;
	}

	public void setColldate(String colldate) {
		this.colldate = colldate;
	}

	public void setCollname(String collname) {
		this.collname = collname;
	}

	public void setCollnumb(String collnumb) {
		this.collnumb = collnumb;
	}

	public void setCollsite(String collsite) {
		this.collsite = collsite;
	}

	public void setDonorcode(String donorcode) {
		this.donorcode = donorcode;
	}

	public void setElevation(String elevation) {
		this.elevation = elevation;
	}

	public void setGenuscode(String genuscode) {
		this.genuscode = genuscode;
	}

	public void setGenusname(String genusname) {
		this.genusname = genusname;
	}

	public void setInstcode(String instcode) {
		this.instcode = instcode;
	}

	public void setInstname(String instname) {
		this.instname = instname;
	}

	public void setInsvalbard(String insvalbard) {
		this.insvalbard = insvalbard;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public void setLatituded(String latituded) {
		this.latituded = latituded;
		if ((latituded == null) || (latituded.trim().length() == 0) || latituded.toLowerCase().equals("null")) {
			this.latituded = "";
		}
	}

	public void setLatres(String latres) {
		this.latres = latres;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public void setLongituded(String longituded) {
		this.longituded = longituded;
		if ((longituded == null) || (longituded.trim().length() == 0) || longituded.toLowerCase().equals("null")) {
			this.longituded = "";
		}
	}

	public void setLonres(String lonres) {
		this.lonres = lonres;
	}

	public void setMisscode(String misscode) {
		this.misscode = misscode;
	}

	public void setOrigacceid(String origacceid) {
		this.origacceid = origacceid;
	}

	public void setOrigcode(String origcode) {
		this.origcode = origcode;
	}

	public void setOrigname(String origname) {
		this.origname = origname;
	}

	public void setOthernumb(String othernumb) {
		this.othernumb = othernumb;
	}

	public void setParentfemale(String parentfemale) {
		this.parentfemale = parentfemale;
	}

	public void setParentmale(String parentmale) {
		this.parentmale = parentmale;
	}

	public void setPedigree(String pedigree) {
		this.pedigree = pedigree;
	}

	public void setSpeciescode(String speciescode) {
		this.speciescode = speciescode;
	}

	public void setSpeciesname(String speciesname) {
		this.speciesname = speciesname;
	}

	public void setSrccode(String srccode) {
		this.srccode = srccode;
	}

	public void setSrcname(String srcname) {
		this.srcname = srcname;
	}

	public void setStatcode(String statcode) {
		this.statcode = statcode;
	}

	public void setStatname(String statname) {
		this.statname = statname;
	}

	public void setTaxcode(String taxcode) {
		this.taxcode = taxcode;
	}

	public void setTaxname(String taxname) {
		this.taxname = taxname;
	}

	public void setTrustcode(String trustcode) {
		this.trustcode = trustcode;
	}

	public void setTrustname(String trustname) {
		this.trustname = trustname;
	}

	@Override
	public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest request) {
		System.out.println("In Accession Form");
		ActionErrors errors = new ActionErrors();
		/*
		 * if (isNull(getSdate())) { //System.out.println("Start Date found null"); errors.add("esdate", new ActionMessage("null.check", "Start Date")); } else { if(getDate(getSdate())==null) {
		 * //System.out.println("Start Date found invalid"); errors.add("esdate", new ActionMessage("invalid.date", "Start Date")); } } if (isNull(getEdate())) { //System.out.println("End Date found
		 * null"); errors.add("eedate", new ActionMessage("null.check", "End Date")); } else { if(getDate(getEdate())==null) { //System.out.println("Start Date found invalid"); errors.add("eedate",
		 * new ActionMessage("invalid.date", "End Date")); } } if(!isNull(getTrainingdays())) { try { Integer.parseInt(getTrainingdays()); } catch(Exception e) { errors.add("etrainingdays", new
		 * ActionMessage("Not a valid number format")); } } if(!isNull(getPeopledays())) { try { Integer.parseInt(getPeopledays()); } catch(Exception e) { errors.add("epeopledays", new
		 * ActionMessage("Not a valid number format")); } } if (isNull(getTitle())) { //System.out.println("Title found null"); errors.add("etitle", new ActionMessage("null.check", "Title")); } else
		 * if (getAction().equals(AccessionConstants.ACTION_ADD)) { System.out.println("Here in title check"); if (AccessionManager.getInstance().capacityBuildingExists(getTitle())) {
		 * System.out.println("In title already exists"); errors.add("etitle", new ActionMessage("errors.title.exists")); } }
		 */
		setSearchAttributes(request);
		if (errors.size() > 0) {
			status = AccessionConstants.FAILURE;
		} else {
			status = AccessionConstants.SUCCESS;
		}
		return errors;
	}

}
