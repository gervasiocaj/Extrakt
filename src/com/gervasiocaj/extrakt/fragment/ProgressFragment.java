package com.gervasiocaj.extrakt.fragment;

import java.util.List;

import com.gervasiocaj.extrakt.MainActivity;
import com.gervasiocaj.extrakt.R;
import com.gervasiocaj.extrakt.core.element.Content;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ProgressFragment extends Fragment {
	
	public static ProgressFragment newInstance	() {
		ProgressFragment fragment = new ProgressFragment();
		// TODO Auto-generated constructor stub
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_progress, container, false);
		
		ListView listView = (ListView) rootView.findViewById(R.id.content_progress_list);
		listView.setAdapter(new ContentListAdapter(rootView.getContext(), 0, null));
		
		return rootView;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(3);
	}
	
	public class ContentListAdapter extends ArrayAdapter<Content> {

		public ContentListAdapter(Context context, int resource,
				List<Content> objects) {
			super(context, resource, objects);
			// TODO Auto-generated constructor stub
		}

	}

}
