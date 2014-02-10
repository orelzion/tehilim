package com.karriapps.tehilimlibrary;

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
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity implements NavigationDrawerCallbacks{
	
	private String TITLE_KEY = "title";
	private String CURRENT_POSITION_KEY = "position";
	private String GENERATOR_KEY = "generator";
	
	private NavigationDrawerFragment mNavigationDrawerFragment;
	private MainFragment mMainFragment;
    
    private SearchView mSearchView;
    
    private String mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		App.getInstance().changeLocale();
		
		setContentView(R.layout.activity_main);
		
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        
        mMainFragment = (MainFragment)
        		getSupportFragmentManager().findFragmentById(R.id.main_fragment);
        
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
			getMenuInflater().inflate(R.menu.global, menu);
			mSearchView = (SearchView)MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
			mSearchView.setOnQueryTextListener(onQuery);
			
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
    public boolean onOptionsItemSelected(MenuItem item) {
    	
        // Handle action buttons
        switch(item.getItemId()) {
	        default:
	            return super.onOptionsItemSelected(item);
        }
    }

	@Override
	protected void onStop() {
		super.onStop();
		
		App.getInstance().save();
	}
	
	private void setGenerator(TehilimGenerator generator) {
		generator.generate();
		mMainFragment.setTehilimGenerator(generator);
		if(generator instanceof PsalmsGenerator)
			mMainFragment.setPosition(((PsalmsGenerator)generator).getFirstChapterKeyPosition());
	}

	@Override
	public void onNavigationDrawerItemSelected(TehilimGenerator generator,
			String title) {
		mNavigationDrawerFragment.closeDrawer();
		mTitle = title;
		restoreActionBar();
		setGenerator(generator);
	}
}
