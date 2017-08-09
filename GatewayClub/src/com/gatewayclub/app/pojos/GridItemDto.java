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
    private int imageDrawable;

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

    public int getImageDrawable() {
        return imageDrawable;
    }

    public void setImageDrawable(int imageDrawable) {
        this.imageDrawable = imageDrawable;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("GridItemDto{");
        sb.append("title='").append(title).append('\'');
        sb.append(", image='").append(image).append('\'');
        sb.append(", facility=").append(facility == null ? "null" : Arrays.asList(facility).toString());
        sb.append(", imageID='").append(imageID).append('\'');
        sb.append(", imageDrawable=").append(imageDrawable);
        sb.append('}');
        return sb.toString();
    }
}
