package com.ediposouza.wifipasswordrecovery;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import com.ediposouza.wifipasswordrecovery.ui.HomeActivity;

import hotchemi.android.rate.AppRate;
import hotchemi.android.rate.OnClickButtonListener;

/**
 * Created by edipo2s on 6/22/15.
 */
public class WifiPasswordRecoveryApp extends Application {

    public static boolean proVersion;

    public static void initializeAppRate(Activity activity) {
        AppRate.with(activity)
                .setInstallDays(0) // default 10, 0 means install day.
                .setLaunchTimes(3) // default 10
                .setRemindInterval(2) // default 1
                .setShowNeutralButton(true) // default true
                .setDebug(false) // default false
                .setOnClickButtonListener(new OnClickButtonListener() { // callback listener.
                    @Override
                    public void onClickButton(int which) {
                        Log.d(HomeActivity.class.getName(), Integer.toString(which));
                    }
                })
                .monitor();

        // Show a dialog if meets conditions
        AppRate.showRateDialogIfMeetsConditions(activity);
    }

}
