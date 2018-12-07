package com.karriapps.tehilim.tehilimlibrary.gcm;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.helpshift.Core;

import static androidx.legacy.content.WakefulBroadcastReceiver.startWakefulService;

/**
 * Created by orelsara on 24/07/14.
 */
public class GcmBroadcastReceiver extends JobService {

    private final static String TAG = GcmBroadcastReceiver.class.getName();

    //@Override
    public boolean onStartJob(Context context, Intent intent) {

        if(intent.getExtras().getString("origin").equals("helpshift")) {
            Core.handlePush(context, intent);
        }
        // Explicitly specify that GcmIntentService will handle the intent.
        ComponentName comp = new ComponentName(context.getPackageName(),
                GcmIntentService.class.getName());
        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, (intent.setComponent(comp)));
        //setResultCode(Activity.RESULT_OK);
        return false;
    }

    @Override
    public boolean onStartJob(JobParameters params) {

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {

        return false;
    }
}
