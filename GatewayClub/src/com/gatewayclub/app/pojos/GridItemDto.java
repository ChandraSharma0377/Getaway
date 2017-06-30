package com.gatewayclub.app.pojos;

import java.util.Arrays;

/**
 * Created by C0678642 on 11/28/2016.
 */
public class GridItemDto {
    private String title;
    private String image;
    private String[] facility;
    private String imageID;
    public GridItemDto() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String[] getFacility() {
        return facility;
    }

    public void setFacility(String[] facility) {
        this.facility = facility;
    }

    public String getImageID() {
        return imageID;
    }

    public void setImageID(String imageID) {
        this.imageID = imageID;
    }

    @Override
    public String toString() {
        return "GridItemDto{" +
                "title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", facility=" + Arrays.toString(facility) +
                ", imageID='" + imageID + '\'' +
                '}';
    }
}
