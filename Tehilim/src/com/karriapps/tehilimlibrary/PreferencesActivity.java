package com.karriapps.tehilimlibrary;

import com.karriapps.tehilimlibrary.utils.App;
import com.karriapps.tehilimlibrary.utils.App.THEME;

import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;

public class PreferencesActivity extends PreferenceActivity{
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
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
				if(App.getInstance().getAppTheme().equals(THEME.DARK)) {
					setTheme(R.style.AppBaseThemeDark);
				} else {
					setTheme(R.style.AppBaseTheme);
				}
				return true;
			}
		});
		
		ListPreference langPref = (ListPreference)findPreference(getString(R.string.lang_key));
		String[] lang = new String[App.LANGUAGES.values().length];
		for(int i = 0; i < lang.length; i++) {
			lang[i] = App.LANGUAGES.values()[i].name();
		}
		langPref.setEntryValues(lang);
		langPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				App.getInstance().changeLocale();
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
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}
}
