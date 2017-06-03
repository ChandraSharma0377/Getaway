package com.gatewayclub.app.pojos;

/**
 * Created by C0678642 on 11/28/2016.
 */
public class GridItemDto {
    private String title;
    private String image;
    public GridItemDto() {

    }

    public GridItemDto(String title, String image) {
        this.title = title;
        this.image = image;
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

    @Override
    public String toString() {
        return "GridItem{" +
                "title='" + title + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
