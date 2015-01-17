package com.ediposouza.wifipasswordrecovery.sync;

import android.os.AsyncTask;

import com.ediposouza.wifipasswordrecovery.adapter.PasswordAdapter;
import com.ediposouza.wifipasswordrecovery.model.PasswordItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ESS on 17/01/15.
 */
public class GetPasswordsAsync extends AsyncTask<Void, Void, List<PasswordItem>> {

    private PasswordAdapter mAdapter;

    public GetPasswordsAsync(PasswordAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mAdapter.clearPasswordItems();
    }

    @Override
    protected List<PasswordItem> doInBackground(Void... params) {
        List<PasswordItem> passwordsItems = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            passwordsItems.add(new PasswordItem("SSID" + i, "Password"));
        }
        return passwordsItems;
    }

    @Override
    protected void onPostExecute(List<PasswordItem> passwordItems) {
        super.onPostExecute(passwordItems);
        for (PasswordItem passwordItem : passwordItems) {
            mAdapter.addPasswordItem(passwordItem);
        }
    }
}
