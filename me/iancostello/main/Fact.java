package me.iancostello.main;

public class Fact {
	private String content;
	private String author;
	
	public Fact() {
		
	}

	public Fact(String content, String author) {
		this.content = content;
		this.author = author;
	}
	
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
