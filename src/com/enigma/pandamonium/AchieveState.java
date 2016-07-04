package com.enigma.pandamonium;

public class AchieveState {
	private String title;
	private int imageId;

	AchieveState() {
	}

	AchieveState(String title, int imageId) {
		this.title = title;
		this.imageId = imageId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getImage() {
		return imageId;
	}

	public void setImage(int image) {
		this.imageId = image;
	}
}
