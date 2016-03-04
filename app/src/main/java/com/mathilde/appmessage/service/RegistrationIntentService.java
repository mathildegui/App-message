package com.mathilde.appmessage.service;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.mathilde.appmessage.R;
import com.mathilde.appmessage.utils.Constant;

import java.io.IOException;

public class RegistrationIntentService extends IntentService {

    private static final String TAG      = "RegIntentService";
    private static final String[] TOPICS = {"global"};

    public RegistrationIntentService() {
        super(TAG);
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public RegistrationIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        InstanceID instanceID         = InstanceID.getInstance(this);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            // [START get_token]
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            // [END get_token]
            Log.i(TAG, "GCM Registration Token: " + token);

            subscribe(token);

            sharedPrefs.edit().putBoolean(Constant.SENT_TOKEN, true).apply();
            sharedPrefs.edit().putString(Constant.TOKEN_REGISTRATION, token).apply();
        } catch (IOException e) {
            e.printStackTrace();
            sharedPrefs.edit().putBoolean(Constant.SENT_TOKEN, false).apply();
        }

        Intent registrationComplete = new Intent(Constant.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    /**
     *
     * @param token
     * @throws IOException
     */
    private void subscribe(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        for (String s : TOPICS) {
            pubSub.subscribe(token, "/topics/" + s, null);
        }
    }
}
