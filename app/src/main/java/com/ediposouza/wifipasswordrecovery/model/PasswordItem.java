package com.ediposouza.wifipasswordrecovery.model;

/**
 * Created by ESS on 17/01/15.
 */
public class PasswordItem {

    private final String mSSID;
    private final String mKey;

    public PasswordItem(String mSSID, String mKey) {
        this.mSSID = mSSID;
        this.mKey = mKey;
    }

    public String getSSID() {
        return mSSID;
    }

    public String getKey() {
        return mKey;
    }

}
