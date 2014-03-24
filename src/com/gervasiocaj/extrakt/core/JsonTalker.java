package com.gervasiocaj.extrakt.core;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONValue;

import android.util.Log;

public class JsonTalker {

	private static final String apiKey = "9f5079d18f0651c2ad6906d0fd0b498d";
	
	public static boolean login(String username, String pass) {
		boolean logged = false;
		
		// ref: https://code.google.com/p/json-simple/wiki/EncodingExamples
		Map<String, String> requestObject = new LinkedHashMap<String, String>();
		requestObject.put("username",username);
		requestObject.put("password",pass);
		String request = JSONValue.toJSONString(requestObject);
		
		try {
			HttpPost post = new HttpPost("http://api.trakt.tv/account/test/" + apiKey);
			post.setEntity(new StringEntity(request));
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
		
		Log.d("jsontalker", "status: " + logged);
		
		return logged;
	}
}
