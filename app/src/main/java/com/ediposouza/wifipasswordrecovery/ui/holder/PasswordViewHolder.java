package com.ediposouza.wifipasswordrecovery.ui.holder;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ediposouza.wifipasswordrecovery.R;
import com.ediposouza.wifipasswordrecovery.model.PasswordItem;

/**
 * Created by ESS on 17/01/15.
 */
public class PasswordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final Context mContext;

    private final TextView mSSID;
    private final TextView mKey;

    public PasswordViewHolder(Context context, View itemView) {
        super(itemView);
        this.mContext = context;
        mSSID = (TextView) itemView.findViewById(R.id.password_ssid);
        mKey = (TextView) itemView.findViewById(R.id.password_key);

        itemView.setOnClickListener(this);
    }

    public void bind(PasswordItem passwordItem) {
        if (passwordItem == null) {
            return;
        }
        mSSID.setText(passwordItem.getSSID());
        mKey.setText(passwordItem.getKey());
    }

    @Override
    public void onClick(View v) {
        String key = mKey.getText().toString();
        String ssid = mSSID.getText().toString();
        String label = String.format(mContext.getString(R.string.label_ssid_password), ssid);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ClipboardManager clipboard = (ClipboardManager)
                    mContext.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setPrimaryClip(ClipData.newPlainText(label, key));
        } else {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager)
                    mContext.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(key);
        }
        Toast.makeText(mContext, mContext.getString(R.string.wpr_password_copied), Toast.LENGTH_SHORT).show();
    }
}
