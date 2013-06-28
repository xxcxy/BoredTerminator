package com.custom.boredterminator.bean;

public class Joke {
	
	private String jokeText;
	private String imagePath;
	public Joke(){
		super();
	}
	public Joke(String jokeText,String imagePath){
		this.jokeText = jokeText;
		this.imagePath = imagePath;
	}
	public String getJokeText() {
		return jokeText;
	}
	public void setJokeText(String jokeText) {
		this.jokeText = jokeText;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
	
}
