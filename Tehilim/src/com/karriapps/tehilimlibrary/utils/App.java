package com.karriapps.tehilimlibrary.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.sourceforge.zmanim.hebrewcalendar.HebrewDateFormatter;
import net.sourceforge.zmanim.hebrewcalendar.JewishCalendar;

import com.karriapps.tehilimlibrary.BuildConfig;
import com.karriapps.tehilimlibrary.R;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

public class App extends Application {
	
	private static App _app;
	private boolean portraitOnly, first, rtl_fix, keepAwake;
	private int fontSize;
	private THEME theme;
	private LANGUAGES lang;
	private FONTS font;
	private SILENT_MODE silent;
	private READING_MODE read;
	private HashMap<Integer, Boolean> favorites;
	private final String FAVORITES_FILE = "favorites.json";
	private PsalmsHelper mPsalms;
	
	public App(){
		super();
		new readFavorites().execute();
	}
	
	public static App getInstance(){
		return _app;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		_app = this;
	}
		
	/**
	 * Choose between modes of switching to silent
	 * <br>ALWAYS - Always switch to silent mode when entering the app
	 * <br>NEVER - Never switch to silent mode
	 * <br>ASK - Ask the user each time he's entering the app whether to switch
	 */
	public enum SILENT_MODE {ALWAYS, NEVER, ASK};
	/**
	 * HEBREW
	 * <br>ENGLISH
	 */
	public enum LANGUAGES {HEBREW, ENGLISH, DEFAULT};
	/**
	 * Choose between these fonts
	 * <br>TIMES - Times New Roman, the default font
	 * <br>GUTTMAN - Guttman Keren
	 * <br>NARKIS - Narkisim
	 * <br>CARDO - Cardo
	 * <br>KETER - Keter Aram Zova
	 */
	public enum FONTS {TIMES, GUTTMAN, NARKIS, ALEF, KETER};
	/**
	 * Choose between these themes
	 * <br>LIGHT
	 * <br>DARK
	 */
	public enum THEME {LIGHT, DARK};
	/**
	 * Choose reasing mode between
	 * <br>SIMPLE - With no Teamim
	 * <br>TRADITIONAL - With Teamim and Ktiv
	 *
	 */
	public enum READING_MODE {SIMPLE, TRADITIONAL};
		
	public Context getAplicationContext(){
		return getApplicationContext();
	}
	
	private SharedPreferences getSharedPreferences(){
		return PreferenceManager.getDefaultSharedPreferences(_app);
	}
	
	/**
	 * Get whether the user getSharedPreferences()ers to lock the screen to portrait mode
	 */
	public boolean isPortraitOnly(){
		portraitOnly = getSharedPreferences().getBoolean(getString(R.string.portrait_key), false); 
		return portraitOnly;
	}
	
	/**
	 * Get if the user needs to apply this patch for rtl fix
	 * <br>
	 * Prior to HONEYCOMB only 
	 */
	public boolean isRtlFixNeeded(){
		rtl_fix = getSharedPreferences().getBoolean(getString(R.string.rtl_key), true); 
		return rtl_fix;
	}
	
	/**
	 * Get if the user wants to keep the screen on as long as he's inside of the app
	 */
	public boolean isKeepAwake() {
		keepAwake = getSharedPreferences().getBoolean(getString(R.string.awake_key), false);
		return keepAwake;
	}
	
	/**
	 * Get what mode of silence to apply upon entering the application
	 */
	public SILENT_MODE getSilentMode() {
		silent = SILENT_MODE.values()[getSharedPreferences().getInt(getString(R.string.silent_key), SILENT_MODE.ASK.ordinal())];; 
		return silent;
	}
	
	public LANGUAGES getLang() {
		lang = LANGUAGES.values()[getSharedPreferences().getInt(getString(R.string.lang_key), LANGUAGES.ENGLISH.ordinal())];;
		return LANGUAGES.HEBREW;// lang; 
	}
	
	public FONTS getFont() {
		font = FONTS.values()[getSharedPreferences().getInt(getString(R.string.font_key), FONTS.KETER.ordinal())];;
		return font;
	}
	
	public int getFontSize() {
		fontSize = getSharedPreferences().getInt(getString(R.string.font_sze_key), 12);
		return fontSize;
	}
	
	public THEME getAppTheme() {
		theme = THEME.values()[getSharedPreferences().getInt(getString(R.string.theme_key), THEME.LIGHT.ordinal())];
		theme = THEME.LIGHT;
		return theme;
	}
	
	public READING_MODE getReadingMode() {
		read = READING_MODE.values()[getSharedPreferences().getInt(getString(R.string.read_key), READING_MODE.SIMPLE.ordinal())];
		return read;
	}
	
	/**
	 * Is this the first time a user uses the application
	 */
	public boolean isThisFirst() {
		first = getSharedPreferences().getBoolean(getString(R.string.first_key), true);
		return first;
	}
	
	public JewishCalendar getJewishCalendar(){
		return new JewishCalendar(getToday().toDate());
	}
	
	public HebrewDateFormatter getHebrewDateFormatter() {
		HebrewDateFormatter formatter = new HebrewDateFormatter();
		formatter.setHebrewFormat(isHebrew());
		return formatter;
	}
	
	public DateTime getToday(){
		return new DateTime();
	}
	
	/**
	 * Check if a chapter is favorite
	 * @param chapterID
	 */
	public boolean isFavorite(int chapterID){
		if(favorites != null)
		{
			if(favorites.get(chapterID) != null)
				return favorites.get(chapterID);
		}
		
		return false;
	}
	
	/**
	 * Toggle chapter from favorite to none and vice versa
	 * @param chapterID
	 */
	public void toggleFavorite(int chapterID){
		if(favorites != null)
		{
			favorites.put(chapterID, !isFavorite(chapterID));
			Log.d("favorite", chapterID + "");
		}
	}
	
	public PsalmsHelper getPsalms(){
		if(mPsalms == null)
			mPsalms= new PsalmsHelper();
		return mPsalms;
	}
	
	private class saveFavorites extends AsyncTask<Void, Void, Void>{
		@Override
		protected Void doInBackground(Void... params) {
			
			FileOutputStream fos;
			
			try{
				fos = getAplicationContext().openFileOutput(FAVORITES_FILE, Context.MODE_PRIVATE);
				JSONArray array = new JSONArray();
				JSONObject obj;
				Iterator<Entry<Integer, Boolean>> i = favorites.entrySet().iterator();
				while(i.hasNext()){
					 obj = new JSONObject();
					 @SuppressWarnings("rawtypes")
					 Map.Entry pairs = (Map.Entry)i.next();
					 obj.put("key", pairs.getKey());
					 obj.put("value", pairs.getValue());
					 array.put(obj);
				}
				fos.write(array.toString().getBytes());
				fos.close();
			}
			catch(IOException ex){
				if(BuildConfig.DEBUG)
					Log.e("saving favorites", ex.getMessage());
			}
			catch (JSONException e) {
				if(BuildConfig.DEBUG)
					Log.e("saving favorites", e.getMessage());
			}
			
			return null;
		}
	}
	
	private class readFavorites extends AsyncTask<Void, Void, Void>{
		
		@Override
		protected Void doInBackground(Void... params) {
			favorites = new HashMap<Integer, Boolean>();
			FileInputStream fis;
			File file = new File(getFilesDir(), FAVORITES_FILE);
			try{
				fis = new FileInputStream(file);
				String fav = Tools.convertStreamToString(fis);
				JSONArray array = new JSONArray(fav);
				
				for(int i = 0; i < array.length(); i++){
					JSONObject obj = array.getJSONObject(i);
					favorites.put(obj.getInt("key"), obj.getBoolean("value"));
				}
			}
			catch(IOException ex){
				if(BuildConfig.DEBUG)
					Log.e("reading favorites", ex.getMessage());
			}
			catch (JSONException e) {
				if(BuildConfig.DEBUG)
					Log.e("reading favorites", e.getMessage());
			}
			
			return null;
		}

	}
	
	public boolean isHebrew() {
		return getLocale().getLanguage().equals("iw") || getLocale().getLanguage().equals("he");
	}
	
	public Locale getLocale(){
		
		Locale l = null;
		switch(getLang()){
			case ENGLISH:
				l = new Locale("en");
				break;
			case HEBREW:
				l = new Locale("iw");
				break;
			case DEFAULT:
				l = getLocale();
				break;
		}
		
		return l;
	}
	
	@SuppressLint("NewApi")
	public void changeLocale(){
		if(!getLang().equals(LANGUAGES.DEFAULT)) {
			Locale.setDefault(getLocale());
			Configuration config = getResources().getConfiguration();
			if(android.os.Build.VERSION.SDK_INT > 16)
				config.setLocale(getLocale());
			else
				config.locale = getLocale();
			getResources().updateConfiguration(config, getResources().getDisplayMetrics());
		}
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if(!getLang().equals(LANGUAGES.DEFAULT)) 
			changeLocale();
	}
	
	public void save(){
		
		getSharedPreferences().edit().putBoolean(getString(R.string.portrait_key), isPortraitOnly());
		getSharedPreferences().edit().putBoolean(getString(R.string.awake_key), isKeepAwake());
		getSharedPreferences().edit().putBoolean(getString(R.string.rtl_key), isRtlFixNeeded());
		getSharedPreferences().edit().putBoolean(getString(R.string.first_key), first);
		getSharedPreferences().edit().putInt(getString(R.string.font_sze_key), getFontSize());
		getSharedPreferences().edit().putInt(getString(R.string.font_key), getFont().ordinal());
		getSharedPreferences().edit().putInt(getString(R.string.lang_key), getLang().ordinal());
		getSharedPreferences().edit().putInt(getString(R.string.read_key), getReadingMode().ordinal());
		getSharedPreferences().edit().putInt(getString(R.string.silent_key), getSilentMode().ordinal());
		getSharedPreferences().edit().putInt(getString(R.string.theme_key), getAppTheme().ordinal());
		
		new saveFavorites().execute();
		getSharedPreferences().edit().commit();
	}
}
