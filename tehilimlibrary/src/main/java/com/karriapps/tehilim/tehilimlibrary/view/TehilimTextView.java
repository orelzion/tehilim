package com.karriapps.tehilim.tehilimlibrary.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.karriapps.tehilim.tehilimlibrary.utils.App;

public class TehilimTextView extends TextView {

    private float mRatio = 1.0f;

    public TehilimTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        switch (App.getInstance().getFont()) {
            case TIMES:
                setTypeface(Typeface.createFromAsset(App.getInstance().getAssets(), "TIMES.ttf"));
                break;
            case GUTTMAN:
                setTypeface(Typeface.createFromAsset(App.getInstance().getAssets(), "Guttman Keren-Normal.ttf"));
                break;
            case NARKIS:
                setTypeface(Typeface.createFromAsset(App.getInstance().getAssets(), "NRKIS.ttf"));
                break;
            case ALEF:
                setTypeface(Typeface.createFromAsset(App.getInstance().getAssets(), "Alef-Regular.ttf"));
                break;
            case KETER:
                setTypeface(Typeface.createFromAsset(App.getInstance().getAssets(), "KeterYG-Medium.ttf"));
                break;
        }
        setTextSize(App.getInstance().getFontSize());
        if (App.getInstance().isRtlFixNeeded())
            this.setGravity(Gravity.RIGHT);
    }

    @Override
    public void setTextSize(float size) {
        size += mRatio;
        super.setTextSize(size);
    }

//	
//	@Override
//	public void setText(CharSequence text, BufferType type) {
//		if(android.os.Build.VERSION.SDK_INT < 16) {
////			text = getHebrewFormattedText(text.toString());
//			text = "\u200e" + text;
//		}
//		super.setText(text, type);
//	}
//	
//	private String getHebrewFormattedText(String text) {
//		String[] words = text.split(" ");
//		StringBuilder finalText = new StringBuilder();
//		
//		String englishExpression = "([A-Z].*|[a-z].*)";
//		String numbersExpression = "([0-9].*):?([0-9].*)";
//		boolean wasEnglish = false;
//		
//		String rtlUnicode = "\u200e";
//		String ltrUnicode = "\u202a";
//		for(int i = 0; i < words.length; i++) {
//			if(words[i].matches(englishExpression)) {
//				if(!wasEnglish) {
//					wasEnglish = true;
//					finalText.append(rtlUnicode + words[i] + " ");
//				} else {
//					finalText.append(words[i]  + " ");
//				}
//			} else if(words[i].matches(numbersExpression)) {
//				if(wasEnglish) {
//					finalText.append(words[i]  + " ");
//				} else {
//					finalText.append(rtlUnicode + words[i]  + " ");
//				}
//			} else {
//				if(wasEnglish) {
//					wasEnglish = false;
//					finalText.append(rtlUnicode + words[i]  + " ");
//				} else {
//					finalText.append(words[i]  + " ");
//				}
//			}
//		}
//		if(finalText.length() > 0)
//			text = finalText.toString();
//		return text;
//	}
}
