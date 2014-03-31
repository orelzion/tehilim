package com.karriapps.tehilimlibrary.utils;

import java.util.Locale;
import java.util.Map;

import org.joda.time.DateTime;

import net.sourceforge.zmanim.hebrewcalendar.HebrewDateFormatter;
import net.sourceforge.zmanim.hebrewcalendar.JewishCalendar;

import com.karriapps.tehilimlibrary.R;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.util.Log;

public class App extends Application {
	
	private static App _app;
	private Map<Integer, Boolean> favorites;
//	private final String FAVORITES_FILE = "favorites.json";
	private PsalmsHelper mPsalms;
	
	public App(){
		super();
//		new readFavorites().execute();
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
	 * <br>TIMES - Times New Roman
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
		return getSharedPreferences().getBoolean(getString(R.string.portrait_key), false); 
	}
	
	/**
	 * Get if the user needs to apply this patch for rtl fix
	 * <br>
	 * Prior to HONEYCOMB only 
	 */
	public boolean isRtlFixNeeded(){
		return getSharedPreferences().getBoolean(getString(R.string.rtl_key), true); 
	}
	
	/**
	 * Get if the user wants to keep the screen on as long as he's inside of the app
	 */
	public boolean isKeepAwake() {
		return getSharedPreferences().getBoolean(getString(R.string.awake_key), false);
	}
	
	/**
	 * Get what mode of silence to apply upon entering the application
	 */
	public SILENT_MODE getSilentMode() {
		return SILENT_MODE.valueOf(getSharedPreferences().getString(getString(R.string.silent_key), SILENT_MODE.ASK.name()));
	}
	
	public LANGUAGES getLang() {
		return LANGUAGES.valueOf(getSharedPreferences().getString(getString(R.string.lang_key), LANGUAGES.DEFAULT.name()));
	}
	
	public FONTS getFont() {
		return FONTS.valueOf(getSharedPreferences().getString(getString(R.string.font_key), FONTS.KETER.name()));
	}
	
	public float getFontSize() {
		return getSharedPreferences().getFloat(getString(R.string.font_sze_key), 15);
	}
	
	public void setFontSize(float size) {
		getSharedPreferences().edit().putFloat(getString(R.string.font_sze_key), size).commit();
	}
	
	public THEME getAppTheme() {
		return THEME.valueOf(getSharedPreferences().getString(getString(R.string.theme_key), THEME.LIGHT.name()));
	}
	
	public READING_MODE getReadingMode() {
		return READING_MODE.valueOf(getSharedPreferences().getString(getString(R.string.read_key), READING_MODE.SIMPLE.name()));
	}
	
	/**
	 * Is this the first time a user uses the application
	 */
	public boolean isThisFirst() {
		boolean returnValue = getSharedPreferences().getBoolean(getString(R.string.first_key), true);
		getSharedPreferences().edit().putBoolean(getString(R.string.first_key), false).commit();
		return returnValue;
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
	
//	private class saveFavorites extends AsyncTask<Void, Void, Void>{
//		@Override
//		protected Void doInBackground(Void... params) {
//			
//			FileOutputStream fos;
//			
//			try{
//				fos = getAplicationContext().openFileOutput(FAVORITES_FILE, Context.MODE_PRIVATE);
//				JSONArray array = new JSONArray();
//				JSONObject obj;
//				Iterator<Entry<Integer, Boolean>> i = favorites.entrySet().iterator();
//				while(i.hasNext()){
//					 obj = new JSONObject();
//					 @SuppressWarnings("rawtypes")
//					 Map.Entry pairs = (Map.Entry)i.next();
//					 obj.put("key", pairs.getKey());
//					 obj.put("value", pairs.getValue());
//					 array.put(obj);
//				}
//				fos.write(array.toString().getBytes());
//				fos.close();
//			}
//			catch(IOException ex){
//				if(BuildConfig.DEBUG)
//					Log.e("saving favorites", ex.getMessage());
//			}
//			catch (JSONException e) {
//				if(BuildConfig.DEBUG)
//					Log.e("saving favorites", e.getMessage());
//			}
//			
//			return null;
//		}
//	}
	
//	private class readFavorites extends AsyncTask<Void, Void, Void>{
//		
//		@Override
//		protected Void doInBackground(Void... params) {
//			favorites = new HashMap<Integer, Boolean>();
//			FileInputStream fis;
//			File file = new File(getFilesDir(), FAVORITES_FILE);
//			try{
//				fis = new FileInputStream(file);
//				String fav = Tools.convertStreamToString(fis);
//				JSONArray array = new JSONArray(fav);
//				
//				for(int i = 0; i < array.length(); i++){
//					JSONObject obj = array.getJSONObject(i);
//					favorites.put(obj.getInt("key"), obj.getBoolean("value"));
//				}
//			}
//			catch(IOException ex){
//				if(BuildConfig.DEBUG)
//					Log.e("reading favorites", ex.getMessage());
//			}
//			catch (JSONException e) {
//				if(BuildConfig.DEBUG)
//					Log.e("reading favorites", e.getMessage());
//			}
//			
//			return null;
//		}
//
//	}
	
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
				l = Locale.getDefault();
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
//		new saveFavorites().execute();
	}
}
