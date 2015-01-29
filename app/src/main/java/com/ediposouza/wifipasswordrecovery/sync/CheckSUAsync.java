package com.ediposouza.wifipasswordrecovery.sync;

import android.os.AsyncTask;
import android.os.Handler;

import com.ediposouza.wifipasswordrecovery.utils.HomeHandler;

import eu.chainfire.libsuperuser.Shell;

/**
 * Created by edipo2s on 1/28/15.
 */
public class CheckSUAsync extends AsyncTask<Void, Void, Boolean> {

    private final Handler mHandler;

    public CheckSUAsync(Handler handler) {
        this.mHandler = handler;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        return Shell.SU.available();
    }

    @Override
    protected void onPostExecute(Boolean available) {
        super.onPostExecute(available);
        int msgWhat = available ? HomeHandler.MSG_READ_PASSWORDS : HomeHandler.MSG_NO_ROOT_FOUND;
        mHandler.sendEmptyMessage(msgWhat);
    }
}
