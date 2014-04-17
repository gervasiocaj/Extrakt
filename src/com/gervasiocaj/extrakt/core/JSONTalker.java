package com.gervasiocaj.extrakt.core;

import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.gervasiocaj.extrakt.DatabaseManager;
import com.gervasiocaj.extrakt.utils.InternetConnection;

import android.content.Context;
import android.util.Log;

public class JSONTalker {

	public static final String apiKey = "9f5079d18f0651c2ad6906d0fd0b498d";
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
		String call = "http://api.trakt.tv/recommendations/" + (isMovie ? "movies/" : "shows/") + apiKey, responseText = null;
		DatabaseManager db = new DatabaseManager(context);
		JSONArray result = new JSONArray();
		
		if (InternetConnection.isConnected()) {
			responseText = InternetConnection.sendJSONPost(call, context);
			db.insertOrUpdateStoredCall(call, responseText);
			Log.d("Jsontalker", "response from web: "+ responseText);
		} else {
			responseText = db.getStoredCall(call);
			Log.d("Jsontalker", "response from db: "+ responseText);
		}

		try {
			if (responseText != null)
				result = (JSONArray) parser.parse(responseText);
		} catch (ParseException e) {
		}
		db.close();
		return result;
	}

	public static JSONArray fillTVShowProgress(Context context) {
		String username = Auth.getSharedPrefs(context).getString("username", "");
		String call = "http://api.trakt.tv/user/progress/watched.json/" + apiKey + "/" + username + "/all/activity/full", responseText = null;
		DatabaseManager db = new DatabaseManager(context);
		
		if (InternetConnection.isConnected()) {
			responseText = InternetConnection.sendJSONPost(call, context);
			db.insertOrUpdateStoredCall(call, responseText);
		} else {
			responseText = db.getStoredCall(call);
		}

		JSONArray result = null;
		try {
			if (responseText != null)
				result = (JSONArray) parser.parse(responseText);
		} catch (ParseException e) {
		}
		db.close();
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static JSONObject fillContent(Context context, String imdb_id, boolean isMovie) {
		String call = "http://api.trakt.tv/" + (isMovie ? "movie" : "show") + "/summary.json/"+ apiKey +"/" + imdb_id + "/full",
				extra_call = "http://api.trakt.tv/user/progress/watched.json/" + apiKey + "/" + Auth.getSharedPrefs(context).getString("username", "") + "/" + imdb_id + "/full";
		String responseText, extraText = "";
		DatabaseManager db = new DatabaseManager(context);
		
		if (InternetConnection.isConnected()) {
			responseText = InternetConnection.sendJSONPost(call, context);
			db.insertOrUpdateStoredCall(call, responseText);
			if (!isMovie){
				extraText = InternetConnection.sendJSONPost(extra_call, context);
				//db.insertOrUpdateStoredCall(extra_call, extraText);
			}
		} else {
			responseText = db.getStoredCall(call);
			//if (!isMovie)
			//	extraText = db.getStoredCall(extra_call);
		}
		
		Log.d("talker", responseText);
		JSONObject result = new JSONObject(); 
		Map<?, ?> extra;
		try {
			if (!isMovie) {
				Log.d("talker", "arr" + responseText);
				if (extraText.length() > 5)
					result = (JSONObject) ((JSONArray) parser.parse(extraText)).get(0);
					//result = (JSONObject) ((JSONArray) parser.parse(responseText)).get(0);
				extra = (JSONObject) parser.parse(responseText);
				
				result.put("extra_info", extra);
				Log.d("talker", "show " + result.toJSONString());
			} else {
				if (responseText != null)
					result = (JSONObject) parser.parse(responseText);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return result;
	}
}
