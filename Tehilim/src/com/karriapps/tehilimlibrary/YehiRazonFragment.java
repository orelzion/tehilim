package com.karriapps.tehilimlibrary;

import com.karriapps.tehilimlibrary.R;
import com.karriapps.tehilimlibrary.utils.TehilimTextView;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class YehiRazonFragment extends DialogFragment {
	
	private TehilimTextView mText;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.yehi_fragment, container, false);
		mText = (TehilimTextView)view.findViewById(R.id.text);
						
		if(getArguments().getBoolean("before")){
			getDialog().setTitle(R.string.yehiTitle);
			mText.setText(Html.fromHtml(getString(R.string.yehiBefore)));
		} else {
			getDialog().setTitle(R.string.yehiAfterTitle);
			mText.setText(Html.fromHtml(String.format(getString(R.string.yehiAfter), getString(R.string.for_partial_tehilim), getString(R.string.for_full_tehilim))));
		}
		
		return view;
	}
	
}
