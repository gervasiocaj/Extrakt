package com.gervasiocaj.extrakt.fragment;

import java.util.List;

import com.gervasiocaj.extrakt.ContentDetailsActivity;
import com.gervasiocaj.extrakt.MainActivity;
import com.gervasiocaj.extrakt.R;
import com.gervasiocaj.extrakt.core.JSONParser;
import com.gervasiocaj.extrakt.core.element.TVShow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

public class ProgressFragment extends Fragment implements OnItemClickListener {
	
	public static ProgressFragment newInstance() {
		ProgressFragment fragment = new ProgressFragment();
		// TODO Auto-generated constructor stub
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_progress, container, false);
		
		final ListView listView = (ListView) rootView.findViewById(R.id.content_progress_list);
		final Context context = container.getContext();
		
		AsyncTask<Void,Void,ListAdapter> myTask = new AsyncTask<Void, Void, ListAdapter>() {

			@Override
			protected ListAdapter doInBackground(Void... params) {
				return new ContentProgressListAdapter(context, JSONParser.getTVShowProgress(context));
			}
			
			@Override
			protected void onPostExecute(ListAdapter result) {
				listView.setAdapter(result);
			}
			
		};
		
		myTask.execute();
		listView.setOnItemClickListener(this);
		
		return rootView;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(3);
	}
	
	
	public class ContentProgressListAdapter extends ArrayAdapter<TVShow> {

		private Context context;

		public ContentProgressListAdapter(Context context, List<TVShow> objects) {
			super(context, R.layout.single_progress_view, objects);
			this.context = context;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			View rowView = inflater.inflate(R.layout.single_progress_view, parent, false);
			TVShow show = (TVShow) getItem(position);
			
			TextView text = (TextView) rowView.findViewById(R.id.textView5);
			ProgressBar progress = (ProgressBar) rowView.findViewById(R.id.progress_bar);
			
			text.setText(show.title);
			progress.setProgress((int) show.completed);
			// ref: http://developer.android.com/reference/android/widget/ProgressBar.html
			
			rowView.setTag(show);
			return rowView;
		}

	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		TVShow show = (TVShow) view.getTag();
		Intent i = new Intent(view.getContext(), ContentDetailsActivity.class);
		i.putExtra(ContentDetailsActivity.IMDB_ID, show.imdb_id);
		i.putExtra(ContentDetailsActivity.TYPE, ContentDetailsActivity.TYPE_SHOW);
		startActivity(i);
	}

}
