package com.gervasiocaj.extrakt.fragment;

import java.util.List;

import com.gervasiocaj.extrakt.*;
import com.gervasiocaj.extrakt.core.element.Content;
import com.gervasiocaj.extrakt.core.element.Movie;
import com.gervasiocaj.extrakt.utils.DownloadImageTask;

import android.app.Activity;
import android.content.*;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

public class RecommendedContentFragment extends Fragment implements OnItemClickListener {
	
	private static final String IS_MOVIE = "recommended_content";

	public static RecommendedContentFragment newInstance(boolean isMovie) {
		RecommendedContentFragment fragment = new RecommendedContentFragment();
		Bundle args = new Bundle();
		args.putBoolean(IS_MOVIE, isMovie);
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_content, container, false);
		
		final GridView gridView = (GridView) rootView.findViewById(R.id.gridView1);
		final Context con = container.getContext();

		gridView.setOnItemClickListener(this);

		AsyncTask<Boolean,Void,ListAdapter> myTask = new AsyncTask<Boolean, Void, ListAdapter>() {

			@Override
			protected ListAdapter doInBackground(Boolean... params) {
				boolean isMovie = params[0];
				
				Log.d(getClass().getSimpleName(), "finishing fill screen with " + (isMovie ? "movies" : "shows"));
				return new ContentGridAdapter(con, com.gervasiocaj.extrakt.core.JSONParser.getContentRecomendations(con, isMovie));
			}
			
			@Override
			protected void onPostExecute(ListAdapter result) {
				gridView.setAdapter(result);
			}

		};
		
		myTask.execute(getArguments().getBoolean(IS_MOVIE));
		return rootView;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		boolean isMovie = getArguments().getBoolean(IS_MOVIE);
		((MainActivity) activity).onSectionAttached(isMovie ? 1 : 2);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Content content = (Content) view.getTag();
		Intent i = new Intent(view.getContext(), ContentDetailsActivity.class);
		i.putExtra(ContentDetailsActivity.IMDB_ID, content.imdb_id);
		i.putExtra(ContentDetailsActivity.TYPE, content instanceof Movie ? ContentDetailsActivity.TYPE_MOVIE : ContentDetailsActivity.TYPE_SHOW);
		startActivity(i);
	}
	
	public class ContentGridAdapter extends ArrayAdapter<Content> {

		private Context context;

		public ContentGridAdapter(Context context, List<Content> objects) {
			super(context, R.layout.single_element_view, objects);
			this.context = context; 
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// ref: http://www.mkyong.com/android/android-listview-example/
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			View rowView = inflater.inflate(R.layout.single_element_view, parent, false);

			ImageView image = (ImageView) rowView.findViewById(R.id.movie_image);
			TextView text = (TextView) rowView.findViewById(R.id.movie_title);
			
			Content current = (Content) getItem(position);
			
			text.setText(current.title);
			new DownloadImageTask(context, current).execute(image);
			
			rowView.setTag(current); //important to onclickListener
			
			return rowView;
		}
		
	}

	
}
