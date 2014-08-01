package com.karriapps.tehilim.tehilimlibrary.gcm;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.helpshift.Helpshift;

/**
 * Created by orelsara on 24/07/14.
 */
public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

    private final static String TAG = GcmBroadcastReceiver.class.getName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getExtras().getString("origin").equals("helpshift")) {
            Helpshift.handlePush(context, intent);
        }
        // Explicitly specify that GcmIntentService will handle the intent.
        ComponentName comp = new ComponentName(context.getPackageName(),
                GcmIntentService.class.getName());
        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
}
