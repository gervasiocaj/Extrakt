package com.gervasiocaj.extrakt.core;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import android.content.Context;
import android.util.Log;

public class JSONTalker {

	private static final String apiKey = "9f5079d18f0651c2ad6906d0fd0b498d";
	private static JSONParser parser = new JSONParser();
	
	public static boolean login(Context context) {
		boolean logged = false;
		
		try {
			HttpPost post = new HttpPost("http://api.trakt.tv/account/test/" + apiKey);
			post.setEntity(new StringEntity(Auth.getJSONCredentials(context)));
			
			HttpResponse response = new DefaultHttpClient().execute(post);
			int responseCode = response.getStatusLine().getStatusCode();
			switch (responseCode) {
			case 200:
				logged = true;
				break;
			case 401:
				logged = false;
				break;
			default:
				throw new Exception("unidentified response, failed authentication");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Log.d("jsontalker", "status: " + (logged ? "logged" : "not logged"));
		
		return logged;
	}
	
	public static JSONArray fillContentRecomendations(Context context, boolean isMovie) {
		
		//TODO
		try {
			HttpPost post = new HttpPost("http://api.trakt.tv/recommendations/" + (isMovie ? "movies/" : "shows/") + apiKey);
			post.setEntity(new StringEntity(Auth.getJSONCredentials(context)));
			HttpResponse response = new DefaultHttpClient().execute(post);
			
			// ref: http://stackoverflow.com/questions/12099067/whats-the-recommended-way-to-get-the-http-response-as-a-string-when-using-apach
			String text = EntityUtils.toString(response.getEntity()); 
			if (response.getStatusLine().getStatusCode() != 200)
				return new JSONArray();
			
			return (JSONArray) parser.parse(text);
		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}
		return new JSONArray();
	}
	
	
}
