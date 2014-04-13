package com.gervasiocaj.extrakt;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import com.gervasiocaj.extrakt.core.element.Movie;

import android.content.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.*;
import android.widget.*;

public class MovieAdapter extends ArrayAdapter<Movie> {

	private Context context;

	public MovieAdapter(Context context, List<Movie> objects) {
		super(context, R.layout.single_element_view, objects);
		this.context = context; 
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// ref: http://www.mkyong.com/android/android-listview-example/
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View rowView = inflater.inflate(R.layout.single_element_view, parent, false);

		ImageView image = (ImageView) rowView.findViewById(R.id.imageView1);
		TextView text = (TextView) rowView.findViewById(R.id.textView1);
		
		Movie current = (Movie) getItem(position);
		
		text.setText(current.title);
		image.setTag(current);
		new DownloadImageTask().execute(image);
		
		return rowView;
	}
	
	public class DownloadImageTask extends AsyncTask<ImageView, Void, Drawable> {
		
		// ref: http://stackoverflow.com/questions/3090650/android-loading-an-image-from-the-web-with-asynctask
		private ImageView imgView = null;

		@Override
		protected Drawable doInBackground(ImageView... params) {
			// ref: http://stackoverflow.com/questions/2471935/how-to-load-an-imageview-by-url-in-android
			// ref: http://stackoverflow.com/questions/3375166/android-drawable-images-from-url
			imgView = params[0];
			Movie current = (Movie) imgView.getTag();
			
			if (current.posterImg != null)
				return current.posterImg;
			
			InputStream in;

			try {
				Log.i("downloadAsync", "downloading image for: " + current.title);
				in = new URL(current.poster).openStream();
				current.posterImg = new BitmapDrawable(imgView.getResources(), in);
			} catch (IOException e) {
				Log.e("downloadAsync", "failed to download image for " + current.title);
			}
			return current.posterImg;
		}
		
		@Override
		protected void onPostExecute(Drawable result) {
			super.onPostExecute(result);
			imgView.setImageDrawable(result);
		}
		
	}

}
