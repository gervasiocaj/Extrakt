package com.gervasiocaj.extrakt.utils;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.Context;

import com.gervasiocaj.extrakt.core.Auth;

public class InternetConnection {

	public static boolean isConnected() {
		// ref: http://developer.android.com/training/monitoring-device-state/connectivity-monitoring.html
		// ref: http://stackoverflow.com/questions/16575030/test-internet-connection-android
		boolean isConnected = true;
		/*
		 * try { Log.d("JSONTalker", "connected: " + isConnected); isConnected =
		 * InetAddress.getByName("www.google.com").isReachable(200); } catch
		 * (IOException e1) { e1.printStackTrace(); }
		 */
		return isConnected; //TODO
	}
	
	public static String sendJSONPost(String call, String entity, Context context) {
		String responseText = null;
		try {
			HttpPost post = new HttpPost(call);
			post.setEntity(new StringEntity(entity));
			HttpResponse response = new DefaultHttpClient().execute(post);
			
			// ref: http://stackoverflow.com/questions/12099067/whats-the-recommended-way-to-get-the-http-response-as-a-string-when-using-apach
			if (response.getStatusLine().getStatusCode() == 200)
				responseText = EntityUtils.toString(response.getEntity());
			
		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}
		
		return responseText;
	}
	
	public static String sendJSONPost(String call, Context context) {
		return sendJSONPost(call, Auth.getJSONCredentials(context), context);
	}

}
