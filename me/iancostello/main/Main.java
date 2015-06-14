package me.iancostello.main;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import me.iancostello.util.ByteBuffer;

public class Main {
	public static void main(String[] args) {
		/** Init */
		ArrayList<Integer> topIdsString = new ArrayList<>();
		ArrayList<Bitt> bitts = new ArrayList<>();
		ByteBuffer topIds = new ByteBuffer();
		JSONObject data = null;
		
		/** Get Bitt Ids */
		try {
			topIds.readURL(new URL("http://www.infobitt.com/api/v2/top-multi/?categories=top"));
			//http://www.infobitt.com/api/top-edition/?count=5
			//http://www.infobitt.com/api/bitt/?count=4&order_by=-last_activity&format=json
			data = new JSONObject(topIds.toString());
		} catch (IOException | JSONException e) {
			e.printStackTrace();
		}
		
		/** Parse The Ids*/
		try {
			JSONArray results = data.getJSONArray("top");
			for (int i = 0; i < results.length(); i+=1) {
				topIdsString.add(results.getInt(i));
			}
		} catch (JSONException e) {
			
		}
		
		/** Build the URL For Getting Bitts*/
		String contentUrl = "http://www.infobitt.com/api/bitts?";
		ByteBuffer content = new ByteBuffer();
		for (int i = 0; i < topIdsString.size(); i+=1) {
			contentUrl = contentUrl + "ids=" + topIdsString.get(i) + '&';
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
			for (int i = 0; i<bittsJson.length(); i+=1) {
				Bitt bitt = new Bitt();
				//Load Each Bitt
				JSONArray facts = bittsJson.getJSONObject(i).getJSONArray("facts");
				//Get the Headline
				JSONObject fact = facts.getJSONObject(0);
				bitt.setHeadline(fact.getString("content"));
				//Get Other Facts
				for (int j = 1; j < facts.length(); j+=1) {
					fact = facts.getJSONObject(j);
					JSONObject author = fact.getJSONObject("author");
					bitt.addFact(new Fact(fact.getString("content"), (author.getString("first_name") + " " + author.getString("last_name"))));
				}
				//Get The Author
				JSONObject author = fact.getJSONObject("author");
				bitt.setAuthorName(author.getString("first_name") + " " + author.getString("last_name"));
				bitts.add(bitt);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		System.out.println("Loaded");
	}
}
