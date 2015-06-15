package me.iancostello.main;

import java.util.ArrayList;

public class Bitt {
	private String headline;
	private String authorName;
	private ArrayList<Fact> facts = new ArrayList<>();
	private int id;

	public Bitt(int id) {
		this.id = id;
	}
	
	public String getHeadline() {
		return headline;
	}

	public void setHeadline(String headline) {
		this.headline = headline;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public void addFact(Fact fact) {
		facts.add(fact);
	}

	public Fact getFact(int index) {
		return facts.get(index);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
}
