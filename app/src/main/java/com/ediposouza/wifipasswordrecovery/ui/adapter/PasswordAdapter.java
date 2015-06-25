package com.ediposouza.wifipasswordrecovery.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ediposouza.wifipasswordrecovery.R;
import com.ediposouza.wifipasswordrecovery.model.PasswordItem;
import com.ediposouza.wifipasswordrecovery.ui.holder.PasswordViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ESS on 17/01/15.
 */
public class PasswordAdapter extends RecyclerView.Adapter<PasswordViewHolder> {

    private final List<PasswordItem> mList = new ArrayList<>();

    @Override
    public PasswordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_password, parent, false);
        return new PasswordViewHolder(parent.getContext(), view);
    }

    @Override
    public void onBindViewHolder(PasswordViewHolder holder, int position) {
        PasswordItem passwordItem = mList.get(position);
        holder.bind(passwordItem);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void addPasswordItem(PasswordItem passwordItem) {
        mList.add(passwordItem);
        notifyItemInserted(mList.size() - 1);
    }

    public void clearPasswordItems() {
        mList.clear();
        notifyDataSetChanged();
    }

}
