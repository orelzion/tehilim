package com.karriapps.tehilimlibrary;

import com.karriapps.tehilimlibrary.R;
import com.karriapps.tehilimlibrary.utils.App;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class YehiRazonFragment extends DialogFragment {
	
	private TextView mText;
	private Typeface mFont;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.yehi_fragment, container, false);
		mText = (TextView)view.findViewById(R.id.text);
				
		switch(App.getInstance().getFont()){
			case TIMES:
				mFont = Typeface.createFromAsset(App.getInstance().getAssets(),"TIMES.TTF");
				break;
			case GUTTMAN:
				mFont = Typeface.createFromAsset(App.getInstance().getAssets(),"Guttman Keren-Normal.TTF");
				break;
			case NARKIS:
				mFont = Typeface.createFromAsset(App.getInstance().getAssets(),"NRKIS.TTF"); 
				break;
			case ALEF:
				mFont = Typeface.createFromAsset(App.getInstance().getAssets(),"Alef-Regular.ttf");
				break;
			case KETER:
				mFont = Typeface.createFromAsset(App.getInstance().getAssets(),"keterAramTsova.ttf");
				break;
		}
		mText.setTypeface(mFont);
		
		if(getArguments().getBoolean("before")){
			getDialog().setTitle(R.string.yehiTitle);
			mText.setText(Html.fromHtml(getString(R.string.yehiBefore)));
		} else {
			getDialog().setTitle(R.string.yehiAfterTitle);
			mText.setText(Html.fromHtml(String.format(getString(R.string.yehiAfter), getString(R.string.for_partial_tehilim), getString(R.string.for_full_tehilim))));
		}
		
		mText.setTextSize(App.getInstance().getFontSize());
		
		return view;
	}
	
}
