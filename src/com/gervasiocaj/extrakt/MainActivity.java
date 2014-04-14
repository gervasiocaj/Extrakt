package com.gervasiocaj.extrakt;

import java.util.concurrent.ExecutionException;

import com.gervasiocaj.extrakt.core.element.*;

import android.app.Activity;
import android.content.*;
import android.support.v7.app.*;
import android.support.v4.app.*;
import android.os.*;
import android.util.Log;
import android.view.*;
import android.support.v4.widget.DrawerLayout;
import android.widget.*;

public class MainActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager
				.beginTransaction()
				.replace(R.id.container,
						PlaceholderFragment.newInstance(position + 1)).commit();
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.title_movies);
			break;
		case 2:
			mTitle = getString(R.string.title_tvshows);
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			// TODO goto settings
			return true;
		} else if (id == R.id.action_login) {
			startActivity(new Intent(this, LoginActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false); // TODO change textview to list
			
			GridView gridView = (GridView) rootView.findViewById(R.id.gridView1);
			final Context con = container.getContext();


			AsyncTask<Class,Void,ListAdapter> myTask = new AsyncTask<Class, Void, ListAdapter>() {

				@Override
				protected ListAdapter doInBackground(Class... params) {
					boolean isMovie = false;
					
					if (params[0] == Movie.class)
						isMovie = true;
					
					Log.d("mainActivity", "finishing fill screen with " + (isMovie ? "movies" : "shows"));
					return new ContentAdapter(con, com.gervasiocaj.extrakt.core.JSONParser.getContentRecomendations(con, isMovie));
				}

			};

			Class classType = null;
			switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
			case 1:
				classType = Movie.class;
				break;
			case 2:
				classType = TVShow.class;
				break;
			default:
				classType = TVShow.class;
				break;
			} // TODO add more views
			
			ListAdapter adapter = null;
			
			try {
				Log.d("mainActivity", "before fill screen");
				adapter = myTask.execute(classType).get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			
			Log.d("mainActivity", "set screen adapter: " + (adapter == null ? "null" : adapter));
			gridView.setAdapter(adapter);
			
			//TextView textView = (TextView) rootView.findViewById(R.id.section_label);
			//textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}
	}

}
