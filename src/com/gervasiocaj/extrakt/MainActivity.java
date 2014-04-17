package com.gervasiocaj.extrakt;


import com.gervasiocaj.extrakt.fragment.NavigationDrawerFragment;
import com.gervasiocaj.extrakt.fragment.ProgressFragment;
import com.gervasiocaj.extrakt.fragment.RecommendedContentFragment;

import android.content.*;
import android.support.v7.app.*;
import android.support.v4.app.*;
import android.os.*;
import android.view.*;
import android.support.v4.widget.DrawerLayout;

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
		// TODO replace remaining fragments
		Fragment temp = new Fragment();
		
		if (position == 0)
			temp = RecommendedContentFragment.newInstance(true);
		else if (position == 1)
			temp = RecommendedContentFragment.newInstance(false);
		else if (position == 2)
			temp = ProgressFragment.newInstance();

		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction()
			.replace(R.id.container, temp).commit();
	}

	public void onSectionAttached(int number) {
		mTitle = mNavigationDrawerFragment.getItemLabel(number);
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
			// ref: http://developer.android.com/guide/topics/search/search-dialog.html
			
			//SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
			//SearchView searchView = (SearchView) menu.findItem(R.id.action_example).getActionView();
			
			//searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		    //searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

			
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
	
}
