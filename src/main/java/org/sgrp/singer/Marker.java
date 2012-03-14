/**
 * 
 */
package org.sgrp.singer;

/**
 * @author kviparthi
 */
public class Marker {

	public int		freq;
	public String	lat;
	public String	latlongd;
	public String	lng;

	public Marker(String latlongd, int freq) {
		this.latlongd = latlongd;
		this.freq = freq;
		this.lat = latlongd.substring(0, latlongd.indexOf("#"));
		this.lng = latlongd.substring(latlongd.indexOf("#") + 1);
	}

	public int getFreq() {
		return freq;
	}

	public String getLat() {
		return lat;
	}

	public String getLatlongd() {
		return latlongd;
	}

	public String getLng() {
		return lng;
	}

	public void setFreq(int freq) {
		this.freq = freq;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public void setLatlongd(String latlongd) {
		this.latlongd = latlongd;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}
}
