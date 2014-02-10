package com.karriapps.tehilimlibrary.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TehilimTextView extends TextView {

	public TehilimTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		switch(App.getInstance().getFont()){
			case TIMES:
				setTypeface(Typeface.createFromAsset(App.getInstance().getAssets(),"TIMES.ttf"));
				break;
			case GUTTMAN:
				setTypeface(Typeface.createFromAsset(App.getInstance().getAssets(),"Guttman Keren-Normal.ttf"));
				break;
			case NARKIS:
				setTypeface(Typeface.createFromAsset(App.getInstance().getAssets(),"NRKIS.ttf")); 
				break;
			case ALEF:
				setTypeface(Typeface.createFromAsset(App.getInstance().getAssets(),"Alef-Regular.ttf"));
				break;
			case KETER:
				setTypeface(Typeface.createFromAsset(App.getInstance().getAssets(),"KeterYG-Medium.ttf"));
				break;
		}
		setTextSize(App.getInstance().getFontSize());
	}
}
