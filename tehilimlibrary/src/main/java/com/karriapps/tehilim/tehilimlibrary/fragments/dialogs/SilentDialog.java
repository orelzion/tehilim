package com.karriapps.tehilim.tehilimlibrary.fragments.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;

import com.karriapps.tehilim.tehilimlibrary.R;
import com.karriapps.tehilim.tehilimlibrary.utils.App;

/**
 * Created by orelsara on 5/9/14.
 */
public class SilentDialog extends DialogFragment implements DialogInterface.OnDismissListener {

    private AudioManager mAudioManager;
    private Button mOKButton;
    private Button mNoButton;
    private CheckBox mRememberCheckBox;
    private App.SILENT_MODE mSilentMode;

    public static SilentDialog newInstance(AudioManager audioManager) {
        return new SilentDialog().setAudioManager(audioManager);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog d = new Dialog(getActivity());
        d.requestWindowFeature(Window.FEATURE_LEFT_ICON);
        d.setContentView(R.layout.silent_dialog);
        d.setCancelable(true);
        d.setCanceledOnTouchOutside(true);
        d.setOnDismissListener(this);
        d.setTitle(R.string.silent);
        mOKButton = d.findViewById(R.id.ok_txt_box);
        mNoButton = d.findViewById(R.id.no_txt_box);
        mRememberCheckBox = d.findViewById(R.id.remember);
        setButton(mOKButton, mNoButton);
        d.show();

        d.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_launcher);

        return d;
    }

    private void setButton(Button... buttons) {
        for (Button b : buttons) {
            b.setOnClickListener(onClick);
        }
    }

    View.OnClickListener onClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (v.equals(mNoButton)) {
                mSilentMode = App.SILENT_MODE.NEVER;
                dismiss();
            } else if (v.equals(mOKButton)) {
                mSilentMode = App.SILENT_MODE.ALWAYS;
                if (mAudioManager != null)
                    mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                dismiss();
            }
        }
    };

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        if (mRememberCheckBox.isChecked()) {
            if (mSilentMode == null)
                App.getInstance().setSilentMode(App.SILENT_MODE.ASK);
            else {
                App.getInstance().setSilentMode(mSilentMode);
            }
        }
    }

    private SilentDialog setAudioManager(AudioManager audioManager) {
        this.mAudioManager = audioManager;
        return this;
    }

}
