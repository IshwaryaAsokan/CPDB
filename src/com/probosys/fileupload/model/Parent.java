package com.probosys.fileupload.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Parent {

	@SerializedName("ancestors")
	private List<PimPojo> pimPojo;

	public List<PimPojo> getPimPojo() {
		return pimPojo;
	}

	public void setPimPojo(List<PimPojo> pimPojo) {
		this.pimPojo = pimPojo;
	}

}
