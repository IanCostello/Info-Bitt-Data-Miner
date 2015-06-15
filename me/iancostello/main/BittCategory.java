package me.iancostello.main;

import java.util.ArrayList;

public class BittCategory {
	private String title;
	private String slug;
	private ArrayList<Bitt> bitts = new ArrayList<>();
	
	public BittCategory() {
		
	}
	
	public int size() {
		return bitts.size();
	}
	
	public Bitt getBitt(int index) {
		return bitts.get(index);
	}

	public void addBitt(Bitt bitt) {
		bitts.add(bitt);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}
}
