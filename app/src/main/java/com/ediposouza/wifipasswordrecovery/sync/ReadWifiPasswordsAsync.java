package com.ediposouza.wifipasswordrecovery.sync;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.ediposouza.wifipasswordrecovery.model.PasswordItem;
import com.ediposouza.wifipasswordrecovery.ui.adapter.PasswordAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import eu.chainfire.libsuperuser.Shell;

/**
 * Created by ESS on 17/01/15.
 */
public class ReadWifiPasswordsAsync extends AsyncTask<Void, Void, List<PasswordItem>> {

    private static final String WAP_SUPPLICANT_DATA_PATH = "/misc/wifi/wpa_supplicant.conf";
    private static final String SSID_FIELD = "ssid";
    private static final String PSK_FIELD = "psk";
    private static final String WEP_KEY_FIELD = "wep_key";

    private final PasswordAdapter mAdapter;

    public ReadWifiPasswordsAsync(PasswordAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    @Override
    protected List<PasswordItem> doInBackground(Void... params) {
        List<PasswordItem> passwordsItems = new ArrayList<>();
        String ssid = "";
        String cmdCatWap = "cat " + Environment.getDataDirectory() + WAP_SUPPLICANT_DATA_PATH;
        for (String line : Shell.SU.run(cmdCatWap)) {
            try {
                if (line.contains(SSID_FIELD)) {
                    StringTokenizer st = new StringTokenizer(line, String.valueOf('"'));
                    st.nextToken();
                    ssid = st.nextToken();
                } else if (line.contains(PSK_FIELD) || line.contains(WEP_KEY_FIELD)) {
                    StringTokenizer st = new StringTokenizer(line, String.valueOf('"'));
                    if (line.contains(String.valueOf('"'))) {
                        st.nextToken();
                        passwordsItems.add(new PasswordItem(ssid, st.nextToken()));
                    } else {
                        st.nextToken("=");
                        passwordsItems.add(new PasswordItem(ssid, st.nextToken("=")));
                    }
                }
            } catch (Exception e) {
                if (e.getMessage() != null) {
                    Log.e("WPR ERROR", e.getMessage());
                }
            }
        }
        return passwordsItems;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mAdapter.clearPasswordItems();
    }

    @Override
    protected void onPostExecute(List<PasswordItem> passwordItems) {
        super.onPostExecute(passwordItems);
        for (PasswordItem passwordItem : passwordItems) {
            mAdapter.addPasswordItem(passwordItem);
        }
    }

}
