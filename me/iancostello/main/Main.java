package me.iancostello.main;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import me.iancostello.util.ByteBuffer;

public class Main {
	
	private HashMap<String, BittCategory> bittCats = new HashMap();
	
	public static void main(String[] args) {
		Main main = new Main();
		main.run();
	}
	
	public void run() {
		/** Init */
		ByteBuffer topIds = new ByteBuffer();
		long initTime = System.currentTimeMillis();
		ArrayList<BittCategory> categoryByIndex = new ArrayList();
		
		/** Get Catagories*/
		try {
			ByteBuffer toRead = new ByteBuffer();
			toRead.readURL(new URL("http://www.infobitt.com/api/category/?count=0&format=json"));
			JSONArray categories = new JSONArray(toRead.toString());
			for (int i = 0; i < categories.length(); i+=1) {
				JSONObject cat = categories.getJSONObject(i);
				BittCategory temp = new BittCategory();
				String slug = cat.getString("slug");
				temp.setSlug(slug);
				temp.setTitle(cat.getString("name"));
				bittCats.put(slug, temp);
				categoryByIndex.add(temp);
			}
		} catch (IOException | JSONException e) {
			e.printStackTrace();
		}
		
		/** Build String To Get All Ids */
		String categoryUrl = "http://www.infobitt.com/api/v2/top-multi/?";
		for (int i = 0; i < categoryByIndex.size(); i+=1) {
			categoryUrl += "categories=" + categoryByIndex.get(i).getSlug() + '&';
		}
		
		JSONObject data = null;
		
		/** Get Bitt Ids */
		try {
			topIds.readURL(new URL(categoryUrl));
			data = new JSONObject(topIds.toString());
		} catch (IOException | JSONException e) {
			e.printStackTrace();
		}
		
		/** Build the URL For Getting Bitts*/
		String contentUrl = "http://www.infobitt.com/api/bitts?";
		ByteBuffer content = new ByteBuffer();
		
		for (int j = 0; j < categoryByIndex.size(); j+=1) {
			try {
				BittCategory category = categoryByIndex.get(j);
				JSONArray results = data.getJSONArray(category.getSlug());
				for (int i = 0; i < results.length(); i+=1) {
					contentUrl = contentUrl + "ids=" + results.getInt(i) + '&';
				}
			} catch (JSONException e) {
				
			}
		}
		
		contentUrl += "format=json";
		JSONArray bittsJson = null;
		
		/** Read Bitts */
		try {
			content.readURL(new URL(contentUrl));
			bittsJson = new JSONArray(content.toString());
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		/** Parse Each Individual Bitt */
		try {
			//Loop Through Each Bitt
			for (int i = 0; i < bittsJson.length(); i+=1) {
				JSONObject jsonData = bittsJson.getJSONObject(i);
				//Grab the Id
				int id = jsonData.getInt("id");
				//Load The Category
				String slug = jsonData.getJSONArray("categorys").getJSONObject(0).getString("slug");
				//Match The Category
				BittCategory category = bittCats.get(slug);
				//Create Temp Bitt
				Bitt bitt = new Bitt(id);
				//Load Each Bitt
				JSONArray facts = jsonData.getJSONArray("facts");
				//Get the Headline
				JSONObject fact = facts.getJSONObject(0);
				bitt.setHeadline(fact.getString("content"));
				bitt.setDateCreated(fact.getString("created"));
				//Get Other Facts
				for (int j = 1; j < facts.length(); j+=1) {
					fact = facts.getJSONObject(j);
					JSONObject author = fact.getJSONObject("author");
					bitt.addFact(new Fact(fact.getString("content"), (author.getString("first_name") + " " + author.getString("last_name"))));
				}
				//Get The Author
				JSONObject author = fact.getJSONObject("author");
				bitt.setAuthorName(author.getString("first_name") + " " + author.getString("last_name"));
				category.addBitt(bitt);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		long endTime = System.currentTimeMillis();
		double timeToLoad = (double)((endTime - initTime) / 1000);
		System.out.println("Loaded in " + timeToLoad);
		
		//Print Out Info In Console
		for (int i=0; i < categoryByIndex.size(); i+=1) {
			BittCategory cat = categoryByIndex.get(i);
			//Get Each Bitt
			for (int j=0; j<cat.size(); j+=1) {
				Bitt bitt = cat.getBitt(j);
				System.out.println(bitt.getAuthorName()+ " on " + cat.getTitle() + " said "+ bitt.getHeadline());
				//Get each fact
				for (int k=0; k < bitt.size(); k+=1) {
					Fact fact = bitt.getFact(k);
					System.out.println('\t' + fact.getAuthor() + " added " + fact.getContent());
				}
			}
		}
	}
}
