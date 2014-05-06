package com.karriapps.tehilimlibrary;

import com.karriapps.tehilimlibrary.utils.App;
import com.karriapps.tehilimlibrary.utils.App.THEME;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.widget.Toast;

public class PreferencesActivity extends PreferenceActivity{
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
	    if (getIntent().hasExtra("bundle") && savedInstanceState == null){
	        savedInstanceState = getIntent().getExtras().getBundle("bundle");
	    }
	    
		if(App.getInstance().getAppTheme().equals(THEME.DARK)) {
			setTheme(R.style.AppBaseThemeDark);
		} else {
			setTheme(R.style.AppBaseTheme);
		}
		
		super.onCreate(savedInstanceState);

		reDraw();
	}
	
	@SuppressWarnings("deprecation")
	private void reDraw() {
		addPreferencesFromResource(R.xml.preferences);
		
		ListPreference fontPref = (ListPreference)findPreference(getString(R.string.font_key));
		String[] fonts = new String[App.FONTS.values().length];
		for(int i = 0; i < fonts.length; i++) {
			fonts[i] = App.FONTS.values()[i].name();
		}
		fontPref.setEntries(fonts);
		fontPref.setEntryValues(fonts);
		
		ListPreference themesPref = (ListPreference)findPreference(getString(R.string.theme_key));
		String[] themes = new String[App.THEME.values().length];
		for(int i = 0; i < themes.length; i++) {
			themes[i] = App.THEME.values()[i].name();
		}
		themesPref.setEntryValues(themes);
		themesPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				preference.getSharedPreferences().edit().putString(getString(R.string.theme_key), newValue.toString()).commit();
				settingsChanged();
				return true;
			}
		});
		
		ListPreference langPref = (ListPreference)findPreference(getString(R.string.lang_key));
		String[] lang = new String[App.LANGUAGES.values().length];
		for(int i = 0; i < lang.length; i++) {
			lang[i] = i +"";
		}
		langPref.setEntryValues(lang);
		langPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				preference.getSharedPreferences().edit().putString(getString(R.string.lang_key), newValue.toString()).commit();
				App.getInstance().changeLocale();
				settingsChanged();
				return true;
			}
		});
		
		ListPreference silentPref = (ListPreference)findPreference(getString(R.string.silent_key));
		String[] silent = new String[App.SILENT_MODE.values().length];
		for(int i = 0; i < silent.length; i++) {
			silent[i] = App.SILENT_MODE.values()[i].name();
		}
		silentPref.setEntryValues(silent);
		
		ListPreference readingPref = (ListPreference)findPreference(getString(R.string.read_key));
		String[] reading = new String[App.READING_MODE.values().length];
		for(int i = 0; i < reading.length; i++) {
			reading[i] = App.READING_MODE.values()[i].name();
		}
		readingPref.setEntryValues(reading);
	}
	
	private void settingsChanged() {
		App.getInstance().setSettingsChanged(true);
		Bundle temp_bundle = new Bundle();
		onSaveInstanceState(temp_bundle);
		Intent intent = new Intent(this, PreferencesActivity.class);
		intent.putExtra("bundle", temp_bundle);
		startActivity(intent);
		finish();
		if(Build.VERSION.SDK_INT < 11) {
			Toast.makeText(this, getString(R.string.settings_changed), Toast.LENGTH_LONG).show();
		}
	}
}
