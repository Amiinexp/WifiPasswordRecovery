package com.ediposouza.wifipasswordrecovery.utils;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.ediposouza.wifipasswordrecovery.adapter.PasswordAdapter;
import com.ediposouza.wifipasswordrecovery.sync.ReadWifiPasswordsAsync;

/**
 * Created by edipo2s on 1/28/15.
 */
public class HomeHandler extends Handler {

    public static final int MSG_READ_PASSWORDS = 1;

    private ReadWifiPasswordsAsync mReadWifiPasswordsAsync;

    public HomeHandler(PasswordAdapter passwordAdapter){
        mReadWifiPasswordsAsync = new ReadWifiPasswordsAsync(passwordAdapter);
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){
            case MSG_READ_PASSWORDS:
                if(mReadWifiPasswordsAsync.getStatus() != AsyncTask.Status.RUNNING) {
                    mReadWifiPasswordsAsync.execute();
                }
        }
    }

}
