package com.gatewayclub.app.pojos;

public class GridItem {
	private String title;
	private int imageID;

	public GridItem() {

	}

	public GridItem(String title, int imageID) {
		super();
		this.title = title;
		this.imageID = imageID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getImageID() {
		return imageID;
	}

	public void setImageID(int imageID) {
		this.imageID = imageID;
	}

	@Override
	public String toString() {
		return "GridItem [title=" + title + ", imageID=" + imageID + "]";
	}

}
