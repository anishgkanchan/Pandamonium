package com.enigma.pandamonium;

public class CellState {
	private int score;
	private int imageId;

	CellState() {
	}

	CellState(int source, int imageId) {
		this.score = source;
		this.imageId = imageId;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getImage() {
		return imageId;
	}

	public void setImage(int image) {
		this.imageId = image;
	}

}
