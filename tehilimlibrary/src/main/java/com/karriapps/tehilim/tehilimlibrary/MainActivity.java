package com.karriapps.tehilim.tehilimlibrary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.AsyncTask;
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

import com.appbrain.AppBrain;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.helpshift.Helpshift;
import com.helpshift.Log;
import com.karriapps.tehilim.tehilimlibrary.fragments.AboutFragment;
import com.karriapps.tehilim.tehilimlibrary.fragments.MainFragment;
import com.karriapps.tehilim.tehilimlibrary.fragments.MainFragment.VIEW_TYPE;
import com.karriapps.tehilim.tehilimlibrary.fragments.NavigationDrawerFragment;
import com.karriapps.tehilim.tehilimlibrary.fragments.NavigationDrawerFragment.NavigationDrawerCallbacks;
import com.karriapps.tehilim.tehilimlibrary.fragments.dialogs.SilentDialog;
import com.karriapps.tehilim.tehilimlibrary.generators.PsalmsGenerator;
import com.karriapps.tehilim.tehilimlibrary.generators.TehilimGenerator;
import com.karriapps.tehilim.tehilimlibrary.generators.YahrzeitGenerator;
import com.karriapps.tehilim.tehilimlibrary.model.LastLocation;
import com.karriapps.tehilim.tehilimlibrary.model.callbacks.OnPositionChanged;
import com.karriapps.tehilim.tehilimlibrary.utils.App;
import com.karriapps.tehilim.tehilimlibrary.utils.App.THEME;
import com.karriapps.tehilim.tehilimlibrary.utils.Tools;
import com.karriapps.tehilim.tehilimlibrary.view.ExtendedSpinner;
import com.karriapps.tehilim.tehilimlibrary.view.ExtendedSpinner.OnItemSelectedListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity implements NavigationDrawerCallbacks, OnPositionChanged {

    public static final String GCM_API_KEY  = App.getInstance().getString(R.string.gcm_api_key);

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

    private TehilimGenerator mGenerator;

    private ExtendedSpinner mChaptersSpinner;
    private List<String> mChapters = new ArrayList<>();
    private ArrayAdapter<String> mSpinnerAdapter;

    private String mTitle;

    private SilentDialog mSilentDialog;
    private int mOldRingerMode;

    private GoogleCloudMessaging mGcm;
    private String mRegisterationID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (App.getInstance().getAppTheme().equals(THEME.DARK)) {
            setTheme(R.style.AppBaseThemeDark);
        } else {
            setTheme(R.style.AppBaseTheme);
        }

        super.onCreate(savedInstanceState);

        if(App.getInstance().isFreeVersion()) {
            AppBrain.init(this);
        }

        Helpshift.install(getApplication(),
                getString(R.string.help_shift_api_key),
                getString(R.string.help_shift_url),
                getString(R.string.help_shift_app_id));

        App.getInstance().changeLocale();
        setContentView(R.layout.activity_main);

        // Check device for Play Services APK. If check succeeds, proceed with GCM registration.
        if (checkPlayServices()) {
            mGcm = GoogleCloudMessaging.getInstance(this);
            mRegisterationID = getRegistrationId();

            if (mRegisterationID.isEmpty()) {
                registerInBackground();
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }

        if (App.getInstance().isKeepAwake()) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        if(App.getInstance().isPortraitOnly()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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

        if (savedInstanceState == null) {
            setGenerator(new PsalmsGenerator(1, 151, 1, 23));
            mTitle = getString(R.string.all_tehilim);
            restoreActionBar();
            saveLastLocation();
        }

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
                    item.setIcon(R.drawable.ic_action_pause);
                } else {
                    item.setTitle(R.string.scroll_play);
                    item.setIcon(R.drawable.ic_action_play);
                }
            }
        } else if (item.getItemId() == R.id.action_help) {
            Helpshift.showFAQs(this);
        }

        return false;
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
        if (mSilentDialog != null)
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
        if(App.getInstance().isFreeVersion())
            AppBrain.getAds().showInterstitial(this);
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
            mSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mChapters);
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

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
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
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     * registration ID.
     */
    private String getRegistrationId() {
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
        editor.putString(getString(R.string.registration_key), mRegisterationID);
        editor.putInt(getString(R.string.registration_version), App.getInstance().getAppVersion());
        editor.commit();
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and the app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (mGcm == null) {
                        mGcm = GoogleCloudMessaging.getInstance(MainActivity.this);
                    }
                    mRegisterationID = mGcm.register(GCM_API_KEY);
                    msg = "Device registered, registration ID=" + mRegisterationID;

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
        }.execute(null, null, null);
    }

    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP or CCS to send
     * messages to your app. Not needed for this demo since the device sends upstream messages
     * to a server that echoes back the message using the 'from' address in the message.
     */
    private void sendRegistrationIdToBackend() {
        // Your implementation here.
        // Send registrationId to Helpshift
        Helpshift.registerDeviceToken(this , mRegisterationID);
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
