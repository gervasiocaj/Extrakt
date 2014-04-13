package com.gervasiocaj.extrakt;

import java.util.List;

import com.gervasiocaj.extrakt.core.element.TVShow;

import android.content.Context;
import android.widget.ArrayAdapter;

public class ShowAdapter extends ArrayAdapter<TVShow> {

	public ShowAdapter(Context context, List<TVShow> objects) {
		super(context, R.layout.single_element_view, objects);
	}

}
