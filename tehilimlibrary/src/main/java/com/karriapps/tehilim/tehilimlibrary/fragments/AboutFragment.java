package com.karriapps.tehilim.tehilimlibrary.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.helpshift.Helpshift;
import com.karriapps.tehilim.tehilimlibrary.R;
import com.karriapps.tehilim.tehilimlibrary.utils.App;

public class AboutFragment extends DialogFragment {

    private Button mVersionButton;
    private Button mFacebookButton;
    private Button mFeedbackButton;
    private Button mGithubButton;
    private Button mRateButton;

    private TextView mVersionInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View view = inflater.inflate(R.layout.about, container, false);

        mVersionButton = (Button) view.findViewById(R.id.version_button);
        mFacebookButton = (Button) view.findViewById(R.id.facebook_button);
        mFeedbackButton = (Button) view.findViewById(R.id.email_button);
        mGithubButton = (Button) view.findViewById(R.id.github_button);
        mRateButton = (Button) view.findViewById(R.id.rate_button);

        mVersionInfo = (TextView) view.findViewById(R.id.version_info);
        mVersionInfo.setText(Html.fromHtml(getString(R.string.version_info)));

        mVersionButton.setText(App.getInstance().getAppVersionName());

        setOnClickListener(mVersionButton, mFacebookButton, mFeedbackButton,
                mGithubButton, mRateButton);

        if (android.os.Build.VERSION.SDK_INT > 13)
            animateOpening();

        return view;
    }

    private void setOnClickListener(Button... buttons) {
        for (Button b : buttons) {
            b.setOnClickListener(mOnClickListner);
        }
    }

    OnClickListener mOnClickListner = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (v.equals(mFacebookButton)) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.facebook_url))));
            } else if (v.equals(mGithubButton)) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.github_url))));
            } else if (v.equals(mRateButton)) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getActivity().getPackageName())));
            } else if (v.equals(mFeedbackButton)) {
                Helpshift.showConversation(getActivity());
            } else if (v.equals(mVersionButton)) {
                if (mVersionInfo.getVisibility() == View.GONE)
                    mVersionInfo.setVisibility(View.VISIBLE);
                else
                    mVersionInfo.setVisibility(View.GONE);
            }
        }
    };

    @SuppressLint("NewApi")
    private void animateOpening() {
        mVersionButton.setTranslationX(500);
        mFacebookButton.setTranslationX(500);
        mFeedbackButton.setTranslationX(500);
        mGithubButton.setTranslationX(500);
        mRateButton.setTranslationX(500);

        mVersionButton.animate().translationX(0).setDuration(150).start();
        mFacebookButton.animate().translationX(0).setDuration(250).start();
        mFeedbackButton.animate().translationX(0).setDuration(350).start();
        mGithubButton.animate().translationX(0).setDuration(450).start();
        mRateButton.animate().translationX(0).setDuration(550).start();
    }
}
