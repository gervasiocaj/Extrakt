package com.gervasiocaj.extrakt.fragment;

import java.util.concurrent.ExecutionException;

import com.gervasiocaj.extrakt.*;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.*;
import android.widget.*;

public class RecommendedContentFragment extends Fragment {
	
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
		
		GridView gridView = (GridView) rootView.findViewById(R.id.gridView1);
		final Context con = container.getContext();


		AsyncTask<Boolean,Void,ListAdapter> myTask = new AsyncTask<Boolean, Void, ListAdapter>() {

			@Override
			protected ListAdapter doInBackground(Boolean... params) {
				boolean isMovie = params[0];
				
				Log.d(getClass().getSimpleName(), "finishing fill screen with " + (isMovie ? "movies" : "shows"));
				return new ContentAdapter(con, com.gervasiocaj.extrakt.core.JSONParser.getContentRecomendations(con, isMovie));
			}

		};
		
		ListAdapter adapter = null;
		
		try {
			Log.d(getClass().getSimpleName(), "before fill screen");
			adapter = myTask.execute(getArguments().getBoolean(IS_MOVIE)).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		
		Log.d(getClass().getSimpleName(), "set screen adapter: " + (adapter == null ? "null" : adapter));
		gridView.setAdapter(adapter);
		
		//TextView textView = (TextView) rootView.findViewById(R.id.section_label);
		//textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
		return rootView;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		boolean isMovie = getArguments().getBoolean(IS_MOVIE);
		((MainActivity) activity).onSectionAttached(isMovie ? 1 : 2);
	}
	
}
