package com.gatewayclub.app.pojos;

public class PropertyImageDto {

	private String propertyImageID;
	private String propertyImageUrl;

	public PropertyImageDto(String propertyImageID, String propertyImageUrl) {
		super();
		this.propertyImageID = propertyImageID;
		this.propertyImageUrl = propertyImageUrl;
	}

	public String getPropertyImageID() {
		return propertyImageID;
	}

	public void setPropertyImageID(String propertyImageID) {
		this.propertyImageID = propertyImageID;
	}

	public String getPropertyImageUrl() {
		return propertyImageUrl;
	}

	public void setPropertyImageUrl(String propertyImageUrl) {
		this.propertyImageUrl = propertyImageUrl;
	}

	@Override
	public String toString() {
		return "PropertyImageDto [propertyImageID=" + propertyImageID + ", propertyImageUrl=" + propertyImageUrl + "]";
	}

}
