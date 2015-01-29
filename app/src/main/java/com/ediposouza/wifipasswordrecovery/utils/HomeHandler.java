package com.ediposouza.wifipasswordrecovery.utils;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.ediposouza.wifipasswordrecovery.R;
import com.ediposouza.wifipasswordrecovery.adapter.PasswordAdapter;
import com.ediposouza.wifipasswordrecovery.sync.ReadWifiPasswordsAsync;

/**
 * Created by edipo2s on 1/28/15.
 */
public class HomeHandler extends Handler {

    public static final int MSG_NO_ROOT_FOUND = 0;
    public static final int MSG_READ_PASSWORDS = 1;

    private final TextView mLookingSU;
    private ReadWifiPasswordsAsync mReadWifiPasswordsAsync;

    public HomeHandler(TextView lookingSU, PasswordAdapter passwordAdapter){
        this.mLookingSU = lookingSU;
        mReadWifiPasswordsAsync = new ReadWifiPasswordsAsync(passwordAdapter);
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){
            case MSG_NO_ROOT_FOUND:
                mLookingSU.setText(R.string.textview_su_not_found);
                break;
            case MSG_READ_PASSWORDS:
                mLookingSU.setVisibility(View.GONE);
                if(mReadWifiPasswordsAsync.getStatus() != AsyncTask.Status.RUNNING) {
                    mReadWifiPasswordsAsync.execute();
                }
        }
    }

}
