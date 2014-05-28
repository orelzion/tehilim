package com.karriapps.easytehilim.tehilimlibrary;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.karriapps.easytehilim.tehilimlibrary.fragments.AboutFragment;
import com.karriapps.easytehilim.tehilimlibrary.fragments.MainFragment;
import com.karriapps.easytehilim.tehilimlibrary.fragments.MainFragment.VIEW_TYPE;
import com.karriapps.easytehilim.tehilimlibrary.fragments.NavigationDrawerFragment;
import com.karriapps.easytehilim.tehilimlibrary.fragments.NavigationDrawerFragment.NavigationDrawerCallbacks;
import com.karriapps.easytehilim.tehilimlibrary.fragments.dialogs.SilentDialog;
import com.karriapps.easytehilim.tehilimlibrary.generators.PsalmsGenerator;
import com.karriapps.easytehilim.tehilimlibrary.generators.TehilimGenerator;
import com.karriapps.easytehilim.tehilimlibrary.generators.YahrzeitGenerator;
import com.karriapps.easytehilim.tehilimlibrary.model.LastLocation;
import com.karriapps.easytehilim.tehilimlibrary.model.callbacks.OnPositionChanged;
import com.karriapps.easytehilim.tehilimlibrary.utils.App;
import com.karriapps.easytehilim.tehilimlibrary.utils.App.THEME;
import com.karriapps.easytehilim.tehilimlibrary.utils.Tools;
import com.karriapps.easytehilim.tehilimlibrary.view.ExtendedSpinner;
import com.karriapps.easytehilim.tehilimlibrary.view.ExtendedSpinner.OnItemSelectedListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity implements NavigationDrawerCallbacks, OnPositionChanged {

    private String TITLE_KEY = "title";
    private String CURRENT_POSITION_KEY = "position";
    private String GENERATOR_KEY = "generator";
    private String SILENT_KEY = "silent";
    private String OLD_SILENT_KEY = "oldsilent";
    private String SCROLLING_KEY = "scrolling";

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private MainFragment mMainFragment;

    private TehilimGenerator mGenerator;

    private ExtendedSpinner mChaptersSpinner;
    private List<String> mChapters = new ArrayList<String>();
    private ArrayAdapter<String> mSpinnerAdapter;

    private String mTitle;

    private SilentDialog mSilentDialog;
    private int mOldRingerMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (App.getInstance().getAppTheme().equals(THEME.DARK)) {
            setTheme(R.style.AppBaseThemeDark);
        } else {
            setTheme(R.style.AppBaseTheme);
        }

        super.onCreate(savedInstanceState);

        App.getInstance().changeLocale();
        setContentView(R.layout.activity_main);

        if (App.getInstance().isKeepAwake()) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        mMainFragment = (MainFragment)
                getSupportFragmentManager().findFragmentById(R.id.main_fragment);

        mMainFragment.setOnPositionChangedListener(this);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        AudioManager mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        if (savedInstanceState != null) {
            mTitle = savedInstanceState.getString(TITLE_KEY);
            if (!TextUtils.isEmpty(mTitle))
                setTitle(mTitle);
            TehilimGenerator generator = savedInstanceState.getParcelable(GENERATOR_KEY);
            if (generator != null) {
                setGenerator(generator);
                mMainFragment.setPosition(savedInstanceState.getInt(CURRENT_POSITION_KEY));
            }
            mAudioManager.setRingerMode(savedInstanceState.getInt(SILENT_KEY));
            mOldRingerMode = savedInstanceState.getInt(OLD_SILENT_KEY);
        } else {
            mOldRingerMode = mAudioManager.getRingerMode();
            if (mOldRingerMode == AudioManager.RINGER_MODE_NORMAL) {
                mSilentDialog = Tools.showSilentDialog(mAudioManager, this);
                if (mSilentDialog != null)
                    mSilentDialog.show(getSupportFragmentManager(), "silent");
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (App.getInstance().isSettingsChanged()) {
            App.getInstance().setSettingsChanged(false);
            if (Build.VERSION.SDK_INT > 10)
                recreate();
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        if (mMainFragment != null) {
            outState.putInt(CURRENT_POSITION_KEY, mMainFragment.getPosition());
            outState.putString(TITLE_KEY, mTitle);
            outState.putParcelable(GENERATOR_KEY, mMainFragment.getTehilimGenerator());
            outState.putInt(SILENT_KEY, ((AudioManager) getSystemService(Context.AUDIO_SERVICE)).getRingerMode());
            outState.putInt(OLD_SILENT_KEY, mOldRingerMode);
        }
    }

    ;

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
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

    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getTitle().equals(getString(R.string.action_about))) {
            new AboutFragment().show(getSupportFragmentManager(), "about");
        } else if (item.getTitle().equals(getString(R.string.action_settings))) {
            Intent preference = new Intent(MainActivity.this, PreferencesActivity.class);
            preference.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(preference);
        } else if (item.getItemId() == R.id.action_scroll) {
            if (mMainFragment != null) {
                item.setChecked(!item.isChecked());
                mMainFragment.toggleScroll(item.isChecked());
                if (item.isChecked()) {
                    item.setTitle(R.string.scroll_pause);
                } else {
                    item.setTitle(R.string.scroll_play);
                }
            }
        }

        return false;
    }

    ;

    OnQueryTextListener onQuery = new OnQueryTextListener() {

        @Override
        public boolean onQueryTextSubmit(String query) {

            query = query.trim();
            int chapter = 0, i = 0;
            if (Tools.isIntNumeric(query))
                chapter = Integer.parseInt(query);
            else {
                for (char c : query.toCharArray()) {
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

        saveLastLocation();
        if (mSilentDialog != null)
            mSilentDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Setting the ringer mode to normal
        ((AudioManager) getSystemService(Context.AUDIO_SERVICE)).setRingerMode(mOldRingerMode);
    }

    private void saveLastLocation() {
        int[] values = new int[4];
        LastLocation.GENERATOR_TYPE type;
        if (mGenerator instanceof PsalmsGenerator) {
            values[0] = ((PsalmsGenerator) mGenerator).getFirstVisibleChapter();
            values[1] = ((PsalmsGenerator) mGenerator).getLastVisibleChapter();
            values[2] = ((PsalmsGenerator) mGenerator).getFirstVisibleKufYudPart();
            values[3] = ((PsalmsGenerator) mGenerator).getLastVisibleKufYud();
            type = LastLocation.GENERATOR_TYPE.CHAPTERS;
        } else if (mGenerator instanceof YahrzeitGenerator) {
            values = Tools.convertIntegers(((YahrzeitGenerator) mGenerator).getNameChapters());
            type = LastLocation.GENERATOR_TYPE.YAHRZEIT;
        } else {
            type = LastLocation.GENERATOR_TYPE.NAME;
        }
        LastLocation loc = new LastLocation(mTitle, values, type, mMainFragment.getPosition());
        App.getInstance().saveLastLocation(loc);
    }

    private void setGenerator(final TehilimGenerator generator) {
        mGenerator = generator;
        if (generator.getList().size() <= 0)
            generator.generate();
        mMainFragment.setTehilimGenerator(generator);
        if (generator instanceof PsalmsGenerator)
            mMainFragment.setViewType(VIEW_TYPE.TEHILIM_BOOK);
        else
            mMainFragment.setViewType(VIEW_TYPE.OTHER);
        mChapters = generator.getKeys();
        setSpinner();
    }

    private void setSpinner() {
        if (mChaptersSpinner != null) {
            mSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mChapters);
            mChaptersSpinner.setAdapter(mSpinnerAdapter);
            mMainFragment.getView().postDelayed(new Runnable() {

                @Override
                public void run() {
                    if (mGenerator != null)
                        mChaptersSpinner.setSelection((mGenerator).getFirstChapterKeyPosition(), true, true);
                }
            }, 150);
        }
    }

    OnItemSelectedListener mOnSpinnerItemSelect = new OnItemSelectedListener() {

        @Override
        public void onNothingSelected(AdapterView<?> adapter) {
        }

        @Override
        public void OnItemSelected(AdapterView<?> adapter, View view, int position,
                                   long itemID, boolean byUser) {
            if (byUser) {
                String titleToSelect = (String) adapter.getItemAtPosition(position);
                mMainFragment.setPosition(titleToSelect);
            }
        }
    };

    @Override
    public void onNavigationDrawerItemSelected(TehilimGenerator generator,
                                               String title) {
        mNavigationDrawerFragment.closeDrawer();
        saveLastLocation();
        mTitle = title;
        restoreActionBar();
        setGenerator(generator);
    }

    @Override
    public void onNavigationDrawerItemSelected(TehilimGenerator generator, String title, int position) {
        onNavigationDrawerItemSelected(generator, title);
        mMainFragment.setPosition(position);
    }

    @Override
    public void onPositionChanged(int position) {
        if (mChaptersSpinner != null) {
            mChaptersSpinner.setSelection(position, true, false);
        }
    }
}
