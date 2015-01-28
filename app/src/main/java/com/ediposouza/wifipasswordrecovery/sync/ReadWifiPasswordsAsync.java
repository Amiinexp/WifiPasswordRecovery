package com.ediposouza.wifipasswordrecovery.sync;

import android.os.AsyncTask;

import com.ediposouza.wifipasswordrecovery.adapter.PasswordAdapter;
import com.ediposouza.wifipasswordrecovery.model.PasswordItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ESS on 17/01/15.
 */
public class ReadWifiPasswordsAsync extends AsyncTask<Void, Void, List<PasswordItem>> {

    private PasswordAdapter mAdapter;

    public ReadWifiPasswordsAsync(PasswordAdapter mAdapter) {
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

//    public void getDataFile(){
//        String ssid = "";
//        String psk = "";
//        ArrayList<String> res = root.exec("cat " + Environment.getDataDirectory() + "/misc/wifi/wpa_supplicant.conf");
//        for(String line : res){
//            try{
//                if(line.contains("ssid")){
//                    StringTokenizer st = new StringTokenizer(line, String.valueOf('"'));
//                    st.nextToken();
//                    ssid = st.nextToken();
//                    continue;
//                }else if(line.contains("psk") || line.contains("wep_key")){
//                    StringTokenizer st = new StringTokenizer(line, String.valueOf('"'));
//                    if(line.contains(String.valueOf('"'))){
//                        st.nextToken();
//                        psk = st.nextToken();
//                    }else{
//                        st.nextToken(String.valueOf("="));
//                        psk = st.nextToken(String.valueOf("="));
//                    }
//                    hMap.put(ssid, psk);
//                    continue;
//                }
//            }catch(Exception e){
//                Log.e("WPR ERRO", e.getMessage());
//                continue;
//            }
//        }
//    }

}
