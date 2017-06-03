package com.gatewayclub.app.pojos;



public class LocationDto {

    String locationID;
    String locationName;

    public LocationDto(String locationID, String locationName) {
        this.locationID = locationID;
        this.locationName = locationName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationID() {
        return locationID;
    }

    public void setLocationID(String locationID) {
        this.locationID = locationID;
    }

    @Override
    public String toString() {
        return "LocationDto{" +
                "locationID='" + locationID + '\'' +
                ", locationName='" + locationName + '\'' +
                '}';
    }
}
