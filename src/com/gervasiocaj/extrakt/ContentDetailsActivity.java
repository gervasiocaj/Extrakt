package com.gervasiocaj.extrakt;

import java.io.*;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.gervasiocaj.extrakt.R;
import com.gervasiocaj.extrakt.core.*;
import com.gervasiocaj.extrakt.utils.DownloadImageTask;
import com.gervasiocaj.extrakt.utils.InternetConnection;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.*;
import android.net.Uri;
import android.os.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

public class ContentDetailsActivity extends Activity {
	
	public static final String IMDB_ID = "extra.imdb.id";
	public static final String TYPE = "extra.media.type",
			TYPE_MOVIE = "media.movie",
			TYPE_SHOW = "media.show";
	private JSONObject content, extraInfo;
	private ShareActionProvider mShareActionProvider;
	private Object episode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// After struggling a bit with the object parsing, which is kind of useless, I decided to use the json objects directly.

		View imgView = null;
		Button seen;
		final String imdb_id = getIntent().getStringExtra(IMDB_ID);
		AsyncTask<Void, Void, JSONObject> myTask = new AsyncTask<Void, Void, JSONObject>() {

			@Override
			protected JSONObject doInBackground(Void... params) {
				boolean isMovie = getIntent().getStringExtra(TYPE).equals(TYPE_MOVIE);
				return JSONTalker.fillContent(getApplicationContext(), imdb_id, isMovie);
			}
		};
		try {
			content = myTask.execute().get();
		} catch (InterruptedException | ExecutionException e1) {
			e1.printStackTrace();
		}
		
		new DownloadImageTask(getApplicationContext(), content).execute();
		
		if (getIntent().getStringExtra(TYPE).equals(TYPE_MOVIE)) {
			setContentView(R.layout.movie_details);
			
			TextView movie_title = (TextView) findViewById(R.id.movie_title);
			movie_title.setText((String) content.get("title"));
			
			// ref: http://stackoverflow.com/questions/974973/java-timestamp-how-can-i-create-a-timestamp-with-the-date-23-09-2007
			TextView movie_data = (TextView) findViewById(R.id.movie_data);
			String data = ""; 
			data += "Released: " + new Timestamp(1000 * (long) content.get("released")).toString() + "\n";
			data += "General Rating: " + (long) ((JSONObject) content.get("ratings")).get("percentage") + "%";
			movie_data.setText(data);
			
			imgView = findViewById(R.id.movie_details);
			
			seen  = (Button) findViewById(R.id.movie_seen);
			seen.setVisibility((boolean) content.get("watched") ? Button.INVISIBLE : Button.VISIBLE);
			seen.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					new AsyncTask<Void, Void, Void>() {
						@Override
						protected Void doInBackground(Void... params) {
							String creds = Auth.getJSONCredentials(getApplicationContext());
							creds = creds.replace('}', ',');
							creds += "\"movies\":[{\"imdb_id\":\"" +  imdb_id + "\"}]}";
							InternetConnection.sendJSONPost("http://api.trakt.tv/movie/seen/"+JSONTalker.apiKey, creds, getApplicationContext());
							return null;
						}
					}.execute();
				}
			});
			
			Button watchTrailer = (Button) findViewById(R.id.movie_trailer);
			watchTrailer.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// ref: http://stackoverflow.com/a/7334252/2282532
					String[] link = ((String) content.get("trailer")).split("/");
					String videoId = link[link.length-1];
					Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoId)); 
					i.putExtra("VIDEO_ID", videoId); 
					startActivity(i);
				}
			});
			
		} else if (getIntent().getStringExtra(TYPE).equals(TYPE_SHOW)) {
			setContentView(R.layout.tvshow_details);
			
			extraInfo = (JSONObject) content.get("extra_info");
			
			TextView show_title = (TextView) findViewById(R.id.show_title);
			show_title.setText((String) extraInfo.get("title"));
			
			TextView show_data = (TextView) findViewById(R.id.show_data);
			JSONObject ratingsObj = (JSONObject) extraInfo.get("ratings");
			
			String data = ""; 
			data += "General Rating: " + (long) ratingsObj.get("percentage") + "% out of " + (long) ratingsObj.get("votes");
			
			if (content.get("genres") != null) {
				Iterator<?> it = ((JSONArray) extraInfo.get("genres")).iterator();
				data += "\nGenres: ";
				while (it.hasNext())
					data += ((String) it.next()) + " ";
			}
			
			data += "\n\nOverview: " + (String) extraInfo.get("overview");
			
			data += "\n\nStatus: " + (String) extraInfo.get("status");
			
			show_data.setText(data);
			
			imgView = findViewById(R.id.show_details);
			
			seen = (Button) findViewById(R.id.show_seen);
			TextView next = (TextView) findViewById(R.id.show_next);
			String text = "";
			text += "Next episode: ";
			
			episode = content.get("next_episode");
			if (episode instanceof Boolean) {
				text += "none";
			} else if (episode instanceof JSONObject) {
				text += (String) ((JSONObject) episode).get("title");
				text += "\nSeason " + (long) ((JSONObject) episode).get("season") + ", Episode " + (long) ((JSONObject) episode).get("number");
				
			}
			
			next.setText(text);
			
			seen = (Button) findViewById(R.id.show_seen);
			if (episode instanceof Boolean)
				seen.setVisibility(Button.GONE);
			seen.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					new AsyncTask<Void, Void, Void>() {
						@Override
						protected Void doInBackground(Void... params) {
							String creds = Auth.getJSONCredentials(getApplicationContext());
							creds = creds.replace('}', ',');
							JSONObject tempEpi = (JSONObject) episode;
							long season = (long) tempEpi.get("season");
							long episode_no =  (long) tempEpi.get("number");
							creds += "\"imdb_id\":\"" + imdb_id + "\",\"episodes\":[{\"season\":" + season + ",\"episode\":" + episode_no + "}]}";
							InternetConnection.sendJSONPost("http://api.trakt.tv/show/episode/seen/"+JSONTalker.apiKey, creds, getApplicationContext());
							return null;
						}
					}.execute();
				}
			});
		}
			
		try {
			// ref: http://stackoverflow.com/a/11286057/2282532
			FileInputStream posterFile = getApplicationContext().openFileInput(imdb_id + "_poster.jpg");
			Drawable background = new BitmapDrawable(imgView.getResources(), posterFile);
			background.setAlpha(60);
			imgView.setBackground(background); // uses api 16
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
			
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// ref: http://developer.android.com/training/sharing/shareaction.html
		getMenuInflater().inflate(R.menu.share, menu);
		
		MenuItem item = menu.findItem(R.id.menu_item_share);
		mShareActionProvider = (ShareActionProvider) item.getActionProvider();
		mShareActionProvider.setShareIntent(getDefaultShareIntent());

	    return true;
	}
	
	private Intent getDefaultShareIntent() {
		Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        boolean ismovie = getIntent().getStringExtra(TYPE).equals(TYPE_MOVIE);
        intent.putExtra(Intent.EXTRA_TEXT, (String) (ismovie ? content.get("url") : extraInfo.get("url")));
        return intent;
	}

}
