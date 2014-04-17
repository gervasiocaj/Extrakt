package com.gervasiocaj.extrakt.utils;

import java.io.*;
import java.net.URL;

import org.json.simple.JSONObject;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.gervasiocaj.extrakt.core.element.Content;

public class DownloadImageTask extends AsyncTask<ImageView, Void, Drawable> {
	
	// ref: http://stackoverflow.com/questions/3090650/android-loading-an-image-from-the-web-with-asynctask
	private ImageView imgView = null;
	private Context context;
	private Content content;

	public DownloadImageTask(Context context, Content content) {
		this.context = context;
		this.content = content;
	}

	public DownloadImageTask(Context applicationContext, JSONObject content2) {
		this.context = applicationContext;
		this.content = new Content();
		content.posterImg = null;
		content.title = (String) content2.get("title");
		content.imdb_id = (String) content2.get("imdb_id");
		content.poster = (String) content2.get("poster");
	}

	@Override
	protected Drawable doInBackground(ImageView... params) {
		// ref: http://stackoverflow.com/questions/2471935/how-to-load-an-imageview-by-url-in-android
		// ref: http://stackoverflow.com/questions/3375166/android-drawable-images-from-url
		if (params.length > 0)
			imgView = params[0];
		Content current = content;
		
		if (current.posterImg != null)
			return current.posterImg;
		
		InputStream in;
		FileOutputStream out;
		String name = current.imdb_id + "_poster.jpg";
		FileInputStream posterFile = null;
		
		try {
			posterFile = context.openFileInput(name);
			Log.i(getClass().getSimpleName(), "found image for: " + current.title);
		} catch (FileNotFoundException e1) {
			try {
				Log.i(getClass().getSimpleName(), "file not found, downloading image for: " + current.title);
				in = new URL(current.poster).openStream();
				out = context.openFileOutput(name, Context.MODE_PRIVATE);
				
				while (in.available()>0)
					out.write(in.read());
				
				in.close();
				out.close();
			} catch (IOException e) {
				Log.e(getClass().getSimpleName(), "failed to download image for " + current.title);
			}
		}
		
		try {
			posterFile = context.openFileInput(name);
			if (imgView != null)
				current.posterImg = new BitmapDrawable(imgView.getResources(), posterFile);
		} catch (FileNotFoundException e) {
		}
		
		if (imgView != null && current.posterImg.getIntrinsicWidth() < 10) {
			Log.d(getClass().getSimpleName(), "image probably corrupted");
			current.posterImg = null;
			context.deleteFile(name);
		}
		
		return current.posterImg;
	}
	
	@Override
	protected void onPostExecute(Drawable result) {
		super.onPostExecute(result);
		if (imgView != null)
			imgView.setImageDrawable(result);
	}
	
}
