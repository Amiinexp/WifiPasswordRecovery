package com.ediposouza.wifipasswordrecovery.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ediposouza.wifipasswordrecovery.R;
import com.ediposouza.wifipasswordrecovery.model.PasswordItem;

/**
 * Created by ESS on 17/01/15.
 */
public class PasswordViewHolder extends RecyclerView.ViewHolder{

    private TextView mSSID;
    private TextView mKey;

    public PasswordViewHolder(View itemView) {
        super(itemView);
        mSSID = (TextView) itemView.findViewById(R.id.password_ssid);
        mKey = (TextView) itemView.findViewById(R.id.password_key);
    }

    public void bind(PasswordItem passwordItem){
        if(passwordItem == null){
            return;
        }
        mSSID.setText(passwordItem.getSSID());
        mKey.setText(passwordItem.getKey());
    }

}
