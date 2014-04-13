package com.gervasiocaj.extrakt.core;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.simple.JSONValue;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class Auth {

	public static boolean login(Context con, String username, String pass) {
		Log.d("auth", "start login");

		boolean hashed = false, logged = false, saved = false; 
		
		String hash = hashPass(pass);
		hashed = !hash.isEmpty();
		
		saved = saveCredentials(con, username, hash);
		
		if (hashed)
			logged = JSONTalker.login(con);
		
		Log.d("auth", "hash: " + hash);
		Log.d("auth", "login: " + (logged ? "sucessful" : "fail"));
		Log.d("auth", "save: " + (saved ? "sucessful" : "fail"));
		
		return hashed && logged && saved;
	}

	private static String hashPass(String pass) {
		// ref: http://nuin.blogspot.co.uk/2011/03/quick-tip-sha1-or-md5-checksum-strings.html
		String result = "";
		try {
			MessageDigest md = MessageDigest.getInstance("SHA1");
			md.update(pass.getBytes("UTF-8"));
			result = toHexString(md.digest());
		} catch (NoSuchAlgorithmException e) {
		} catch (UnsupportedEncodingException e) {
		}
		
		return result;
	}
	
	public static SharedPreferences getSharedPrefs(Context context) {
		return context.getSharedPreferences("ExtraktPrefs", 0);
	}

	private static boolean saveCredentials(Context con, String username, String hashedPass) {
		// ref: http://developer.android.com/guide/topics/data/data-storage.html
		Editor editor = getSharedPrefs(con).edit();
		editor.putString("username", username);
		editor.putString("pass", hashedPass);

		return editor.commit();
	}

	public static String getJSONCredentials(Context context) {
		// ref: https://code.google.com/p/json-simple/wiki/EncodingExamples
		Map<String, String> requestObject = new LinkedHashMap<String, String>();
		requestObject.put("username", getSharedPrefs(context).getString("username", ""));
		requestObject.put("password", getSharedPrefs(context).getString("pass", ""));
		return JSONValue.toJSONString(requestObject);
	}
	
	public static String toHexString(byte[] array) {
		StringBuilder builder = new StringBuilder();
		String temp;
		int last;
		
		for (byte b : array) {
			// added zero to assure the size is greater than 2
			temp = "0" + Integer.toHexString(b);
			
			last = temp.length() - 1;
			builder.append(temp.charAt(last-1));
			builder.append(temp.charAt(last));
			// gets two last digits
		}
		return builder.toString();
	}

}
