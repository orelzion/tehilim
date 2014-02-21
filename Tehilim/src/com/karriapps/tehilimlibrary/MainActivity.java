package com.karriapps.tehilimlibrary;

import java.util.ArrayList;
import java.util.List;

import com.karriapps.tehilimlibrary.ExtendedSpinner.OnItemSelectedListener;
import com.karriapps.tehilimlibrary.MainFragment.VIEW_TYPE;
import com.karriapps.tehilimlibrary.NavigationDrawerFragment.NavigationDrawerCallbacks;
import com.karriapps.tehilimlibrary.R;
import com.karriapps.tehilimlibrary.generators.PsalmsGenerator;
import com.karriapps.tehilimlibrary.generators.TehilimGenerator;
import com.karriapps.tehilimlibrary.utils.App;
import com.karriapps.tehilimlibrary.utils.Tools;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

public class MainActivity extends ActionBarActivity implements NavigationDrawerCallbacks, OnPositionChanged {

	private String TITLE_KEY = "title";
	private String CURRENT_POSITION_KEY = "position";
	private String GENERATOR_KEY = "generator";

	private NavigationDrawerFragment mNavigationDrawerFragment;
	private MainFragment mMainFragment;
	
	private TehilimGenerator mGenerator;

	private ExtendedSpinner mChaptersSpinner;
	private List<String> mChapters = new ArrayList<String>();
	ArrayAdapter<String> mSpinnerAdapter;

	private String mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		App.getInstance().changeLocale();

		setContentView(R.layout.activity_main);

		mNavigationDrawerFragment = (NavigationDrawerFragment)
				getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
		
		mMainFragment = (MainFragment)
				getSupportFragmentManager().findFragmentById(R.id.main_fragment);
		
		mMainFragment.setOnPositionChangedListener(this);

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(
				R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));

		if(savedInstanceState != null) {
			mTitle = savedInstanceState.getString(TITLE_KEY);
			if(!TextUtils.isEmpty(mTitle))
				setTitle(mTitle);
			TehilimGenerator generator = savedInstanceState.getParcelable(GENERATOR_KEY);
			if(generator != null) {
				setGenerator(generator);
				mMainFragment.setPosition(savedInstanceState.getInt(CURRENT_POSITION_KEY));
			}
		}
	}

	protected void onSaveInstanceState(Bundle outState) {
		if(mMainFragment != null) {
			outState.putInt(CURRENT_POSITION_KEY, mMainFragment.getPosition());
			outState.putString(TITLE_KEY, mTitle);
			outState.putParcelable(GENERATOR_KEY, mMainFragment.getTehilimGenerator());
		}
	};

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if(!mNavigationDrawerFragment.isDrawerOpen()) {
			getMenuInflater().inflate(R.menu.main, menu);
			//			mSearchView = (SearchView)MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
			//			mSearchView.setOnQueryTextListener(onQuery);

			mChaptersSpinner = (ExtendedSpinner) MenuItemCompat.getActionView(menu.findItem(R.id.choose_chapter));
			mChaptersSpinner.setOnItemSelectedListener(mOnSpinnerItemSelect);
			setSpinner();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}
	
	OnQueryTextListener onQuery = new OnQueryTextListener() {

		@Override
		public boolean onQueryTextSubmit(String query) {

			query = query.trim();
			int chapter = 0, i = 0;
			if(Tools.isIntNumeric(query))
				chapter = Integer.parseInt(query);
			else{
				for(char c : query.toCharArray()){
					i = Tools.getCharVal(c);
					chapter += i;
				}
			}

			mMainFragment.setPosition(chapter);

			return false;
		}

		@Override
		public boolean onQueryTextChange(String newText) {

			return false;
		}
	};

	@Override
	protected void onStop() {
		super.onStop();

		App.getInstance().save();
	}

	private void setGenerator(final TehilimGenerator generator) {
		mGenerator = generator;
		generator.generate();
		mMainFragment.setTehilimGenerator(generator);
		if(generator instanceof PsalmsGenerator)
			mMainFragment.setViewType(VIEW_TYPE.TEHILIM_BOOK);
		else
			mMainFragment.setViewType(VIEW_TYPE.OTHER);
		mChapters = generator.getKeys();
		setSpinner();
	}
	
	private void setSpinner() {
		if(mChaptersSpinner != null) {
			mSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mChapters);
			mChaptersSpinner.setAdapter(mSpinnerAdapter);
			mMainFragment.getView().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					if(mGenerator != null)
						mChaptersSpinner.setSelection((mGenerator).getFirstChapterKeyPosition(), true, true);
				}
			}, 150);
		}
	}
	
	OnItemSelectedListener mOnSpinnerItemSelect = new OnItemSelectedListener() {
		
		@Override
		public void onNothingSelected(AdapterView<?> adapter) {}
		
		@Override
		public void OnItemSelected(AdapterView<?> adapter, View view, int position,
				long itemID, boolean byUser) {
			if(byUser) {
				String titleToSelect = (String)adapter.getItemAtPosition(position);
				mMainFragment.setPosition(titleToSelect);
			}
		}
	};

	@Override
	public void onNavigationDrawerItemSelected(TehilimGenerator generator,
			String title) {
		mNavigationDrawerFragment.closeDrawer();
		mTitle = title;
		restoreActionBar();
		setGenerator(generator);
	}

	@Override
	public void onPositionChanged(int position) {
		if(mChaptersSpinner != null) {
			mChaptersSpinner.setSelection(position, true, false);
		}
	}
}
