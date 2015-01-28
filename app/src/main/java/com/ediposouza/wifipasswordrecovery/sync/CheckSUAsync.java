package com.ediposouza.wifipasswordrecovery.sync;

import android.os.AsyncTask;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.ediposouza.wifipasswordrecovery.utils.HomeHandler;

import eu.chainfire.libsuperuser.Shell;

/**
 * Created by edipo2s on 1/28/15.
 */
public class CheckSUAsync extends AsyncTask<Void, Void, Boolean> {

    private final TextView mLookingSU;
    private Handler mHandler;

    public CheckSUAsync(TextView lookingSU, Handler handler) {
        this.mLookingSU = lookingSU;
        this.mHandler = handler;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        return Shell.SU.available();
    }

    @Override
    protected void onPostExecute(Boolean available) {
        super.onPostExecute(available);
        mLookingSU.setVisibility(View.GONE);
        mHandler.sendEmptyMessage(HomeHandler.MSG_READ_PASSWORDS);
    }
}
