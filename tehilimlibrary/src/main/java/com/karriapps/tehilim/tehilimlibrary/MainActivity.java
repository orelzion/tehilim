package com.karriapps.tehilim.tehilimlibrary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SearchView.OnQueryTextListener;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.helpshift.support.Log;
import com.helpshift.support.Support;
import com.helpshift.Core;
import com.helpshift.All;
import com.helpshift.exceptions.InstallException;
import com.karriapps.tehilim.tehilimlibrary.fragments.AboutFragment;
import com.karriapps.tehilim.tehilimlibrary.fragments.MainFragment;
import com.karriapps.tehilim.tehilimlibrary.fragments.MainFragment.VIEW_TYPE;
import com.karriapps.tehilim.tehilimlibrary.fragments.NavigationDrawerFragment;
import com.karriapps.tehilim.tehilimlibrary.fragments.NavigationDrawerFragment.NavigationDrawerCallbacks;
import com.karriapps.tehilim.tehilimlibrary.fragments.dialogs.SilentDialog;
import com.karriapps.tehilim.tehilimlibrary.generators.PsalmsGenerator;
import com.karriapps.tehilim.tehilimlibrary.generators.ShiraGenerator;
import com.karriapps.tehilim.tehilimlibrary.generators.TehilimGenerator;
import com.karriapps.tehilim.tehilimlibrary.generators.TikunKlaliGenerator;
import com.karriapps.tehilim.tehilimlibrary.generators.YahrzeitGenerator;
import com.karriapps.tehilim.tehilimlibrary.model.LastLocation;
import com.karriapps.tehilim.tehilimlibrary.model.callbacks.OnPositionChanged;
import com.karriapps.tehilim.tehilimlibrary.utils.App;
import com.karriapps.tehilim.tehilimlibrary.utils.App.THEME;
import com.karriapps.tehilim.tehilimlibrary.utils.Tools;
import com.karriapps.tehilim.tehilimlibrary.view.ExtendedSpinner;
import com.karriapps.tehilim.tehilimlibrary.view.ExtendedSpinner.OnItemSelectedListener;
import com.karriapps.tehilim.tehilimlibrary.view.ToolbarSpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationDrawerCallbacks, OnPositionChanged {

    public static final String GCM_API_KEY = App.getInstance().getString(R.string.gcm_api_key);

    private static final String TAG = MainActivity.class.getName();

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TITLE_KEY = "title";
    private static final String CURRENT_POSITION_KEY = "position";
    private static final String GENERATOR_KEY = "generator";
    private static final String SILENT_KEY = "silent";
    private static final String OLD_SILENT_KEY = "oldsilent";
    private static final String SCROLLING_KEY = "scrolling";

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private MainFragment mMainFragment;
    private DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;

    private TehilimGenerator mGenerator;

    private ExtendedSpinner mChaptersSpinner;
    private List<String> mChapters = new ArrayList<>();
    private ArrayAdapter<String> mSpinnerAdapter;

    private String mTitle;

    private SilentDialog mSilentDialog;
    private int mOldRingerMode;

    private String mRegistrationID;

    private int mInitialPosition;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (App.getInstance().getAppTheme().equals(THEME.DARK)) {
            setTheme(R.style.AppBaseThemeDark);
        } else {
            setTheme(R.style.AppBaseTheme);
        }

        super.onCreate(savedInstanceState);

        Core.init(All.getInstance());
        try {
            Core.install(getApplication(),
                getString(R.string.help_shift_api_key),
                getString(R.string.help_shift_url),
                getString(R.string.help_shift_app_id));
        } catch (InstallException e) {
            Log.e(TAG, "invalid install credentials : ", e);
        }

        App.getInstance().changeLocale();
        setContentView(R.layout.activity_main);


        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        mMainFragment = (MainFragment)
                getSupportFragmentManager().findFragmentById(R.id.main_fragment);

        //Implement toolbar as a replacement for actionbar
        mToolbar = mMainFragment.getView().findViewById(R.id.app_toolbar);
        setSupportActionBar(mToolbar);

        // Check device for Play Services APK. If check succeeds, proceed with GCM registration.
        if (checkPlayServices()) {
            mRegistrationID = getRegistrationId(getApplicationContext());

            if (mRegistrationID.isEmpty()) {
                registerInBackground();
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }

        if (App.getInstance().isKeepAwake()) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        if (App.getInstance().isPortraitOnly()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }


        mMainFragment.setOnPositionChangedListener(this);


        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                mDrawerLayout,
                mToolbar);

        if (savedInstanceState == null) {
            setGenerator(new PsalmsGenerator(1, 151, 1, 23), -1);
            mTitle = getString(R.string.all_tehilim);
            restoreActionBar();
        }

        AudioManager mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        if (savedInstanceState != null) {
            mTitle = savedInstanceState.getString(TITLE_KEY);
            if (!TextUtils.isEmpty(mTitle)) {
                setTitle(mTitle);
                mMainFragment.updateTitle();
            }

            TehilimGenerator generator = savedInstanceState.getParcelable(GENERATOR_KEY);
            if (generator != null) {
                setGenerator(generator, savedInstanceState.getInt(CURRENT_POSITION_KEY));
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
        if (mMainFragment != null) {
            mMainFragment.updateTitle();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.main, menu);
            //			mSearchView = (SearchView)MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
            //			mSearchView.setOnQueryTextListener(onQuery);

            setSpinner();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (mDrawerToggle != null) {
            mDrawerToggle.syncState();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle != null && mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

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
                    item.setIcon(R.drawable.ic_action_pause);
                } else {
                    item.setTitle(R.string.scroll_play);
                    item.setIcon(R.drawable.ic_action_play);
                }
            }
        } else if (item.getItemId() == R.id.action_help) {
            Support.showFAQs(this);
        }

        return false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (mDrawerToggle != null) {
            mDrawerToggle.onConfigurationChanged(newConfig);
        }
        super.onConfigurationChanged(newConfig);
    }

    OnQueryTextListener onQuery = new OnQueryTextListener() {

        @Override
        public boolean onQueryTextSubmit(String query) {

            query = query.trim();
            int chapter = 0, i;
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
        if (mSilentDialog != null && mSilentDialog.isVisible())
            mSilentDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Setting the ringer mode to normal
        ((AudioManager) getSystemService(Context.AUDIO_SERVICE)).setRingerMode(mOldRingerMode);
    }

    @Override
    public void onBackPressed() {

        if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(Gravity.START | Gravity.LEFT)) {
            mDrawerLayout.closeDrawers();
            return;
        }

        /*if (App.getInstance().isFreeVersion())
            AppBrain.getAds().showInterstitial(this);*/
        finish();
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

        if (mNavigationDrawerFragment != null) {
            mNavigationDrawerFragment.updateQuickListAdapter();
        }
    }

    private void setGenerator(final TehilimGenerator generator, int position) {
        mGenerator = generator;
        if (generator.getList().size() <= 0)
            generator.generate();
        mMainFragment.setTehilimGenerator(generator);
        if (generator instanceof PsalmsGenerator) {
            mMainFragment.setViewType(VIEW_TYPE.TEHILIM_BOOK);
            mMainFragment.changeImage(R.drawable.psalms_mini);
        } else {
            mMainFragment.setViewType(VIEW_TYPE.OTHER);
            if (generator instanceof YahrzeitGenerator) {
                mMainFragment.changeImage(R.drawable.cemetary);
            } else if (generator instanceof TikunKlaliGenerator) {
                mMainFragment.changeImage(R.drawable.forest);
            } else if (generator instanceof ShiraGenerator) {
                mMainFragment.changeImage(R.drawable.squirrel_prayer);
            } else {
                mMainFragment.changeImage(R.drawable.psalms_mini);
            }
        }
        mChapters = generator.getKeys();
        mInitialPosition = position > 0 ? position : mGenerator.getFirstChapterKeyPosition();
        setSpinner();
    }

    private void setSpinner() {

        View view = LayoutInflater.from(this).inflate(R.layout.toolbar_spinner, mToolbar
                , false);
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mToolbar.addView(view, lp);

        ToolbarSpinnerAdapter spinnerAdapter = new ToolbarSpinnerAdapter();
        spinnerAdapter.setItems(mGenerator.getKeys().toArray(new String[mGenerator.getKeys().size()]));

        mChaptersSpinner = (ExtendedSpinner) view.findViewById(R.id.toolbar_spinner);
        mChaptersSpinner.setOnItemSelectedListener(mOnSpinnerItemSelect);
        mChaptersSpinner.setAdapter(spinnerAdapter);
        mMainFragment.getView().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (mGenerator != null)
                    mChaptersSpinner.setSelection(mInitialPosition, true, true);
            }
        }, 150);

    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GoogleApiAvailability.getInstance().isUserResolvableError(resultCode)) {
                GoogleApiAvailability.getInstance().getErrorDialog(this, resultCode,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
            }
            return false;
        }
        return true;
    }

    /**
     * Gets the current registration ID for application on GCM service, if there is one.
     * <p/>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     * registration ID.
     */
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = App.getInstance().getSharedPreferences();
        String registrationId = prefs.getString(getString(R.string.registration_key), "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(getString(R.string.registration_version), Integer.MIN_VALUE);
        int currentVersion = App.getInstance().getAppVersion();
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * Stores the registration ID and the app versionCode in the application's
     * {@code SharedPreferences}.
     */
    private void storeRegistrationId() {
        final SharedPreferences prefs = App.getInstance().getSharedPreferences();
        Log.i(TAG, "Saving regId on app version " + App.getInstance().getAppVersion());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(getString(R.string.registration_key), mRegistrationID);
        editor.putInt(getString(R.string.registration_version), App.getInstance().getAppVersion());
        editor.commit();
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p/>
     * Stores the registration ID and the app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        /*new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (mGcm == null) {
                        mGcm = GoogleCloudMessaging.getInstance(MainActivity.this);
                    }
                    mRegistrationID = mGcm.register(GCM_API_KEY);
                    msg = "Device registered, registration ID=" + mRegistrationID;

                    // You should send the registration ID to your server over HTTP, so it
                    // can use GCM/HTTP or CCS to send messages to your app.
                    sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device will send
                    // upstream messages to a server that echo back the message using the
                    // 'from' address in the message.

                    // Persist the regID - no need to register again.
                    storeRegistrationId();
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.i(TAG, msg);
            }
        }.execute(null, null, null);*/
    }

    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP or CCS to send
     * messages to your app. Not needed for this demo since the device sends upstream messages
     * to a server that echoes back the message using the 'from' address in the message.
     */
    private void sendRegistrationIdToBackend() {
        // Your implementation here.
        // Send registrationId to Helpshift
        Core.registerDeviceToken(this, mRegistrationID);
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
    public void onNavigationDrawerItemSelected(TehilimGenerator generator, String title) {
        onNavigationDrawerItemSelected(generator, title, -1);
    }

    @Override
    public void onNavigationDrawerItemSelected(TehilimGenerator generator, String title, int position) {
        mNavigationDrawerFragment.closeDrawer();
        saveLastLocation();
        mTitle = title;
        restoreActionBar();
        setGenerator(generator, position);
    }

    @Override
    public void onPositionChanged(int position) {
        if (mChaptersSpinner != null) {
            mChaptersSpinner.setSelection(position, true, false);
        }
    }
}
