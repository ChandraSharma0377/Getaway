package com.gatewayclub.app.pojos;

public class PropertyDto {

	private String propertyID;
	private String propertyName;
	private String propertyImage;
	private String propertymin_capacity;
	private String propertymax_capacity;
	
	
	public PropertyDto(String propertyID, String propertyName, String propertyImage, String propertymin_capacity,
			String propertymax_capacity) {
		super();
		this.propertyID = propertyID;
		this.propertyName = propertyName;
		this.propertyImage = propertyImage;
		this.propertymin_capacity = propertymin_capacity;
		this.propertymax_capacity = propertymax_capacity;
	}
	public String getPropertyID() {
		return propertyID;
	}
	public void setPropertyID(String propertyID) {
		this.propertyID = propertyID;
	}
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	public String getPropertyImage() {
		return propertyImage;
	}
	public void setPropertyImage(String propertyImage) {
		this.propertyImage = propertyImage;
	}
	public String getPropertymin_capacity() {
		return propertymin_capacity;
	}
	public void setPropertymin_capacity(String propertymin_capacity) {
		this.propertymin_capacity = propertymin_capacity;
	}
	public String getPropertymax_capacity() {
		return propertymax_capacity;
	}
	public void setPropertymax_capacity(String propertymax_capacity) {
		this.propertymax_capacity = propertymax_capacity;
	}
	@Override
	public String toString() {
		return "PropertyDto [propertyID=" + propertyID + ", propertyName=" + propertyName + ", propertyImage="
				+ propertyImage + ", propertymin_capacity=" + propertymin_capacity + ", propertymax_capacity="
				+ propertymax_capacity + "]";
	}

	
}
