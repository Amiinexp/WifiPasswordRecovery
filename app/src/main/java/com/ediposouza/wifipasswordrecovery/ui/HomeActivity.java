package com.ediposouza.wifipasswordrecovery.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ediposouza.wifipasswordrecovery.R;
import com.ediposouza.wifipasswordrecovery.adapter.PasswordAdapter;
import com.ediposouza.wifipasswordrecovery.sync.ReadWifiPasswordsAsync;
import com.ediposouza.wifipasswordrecovery.ui.widget.DividerItemDecoration;
import com.ediposouza.wifipasswordrecovery.ui.widget.SlideInLeftAnimator;

public class HomeActivity extends ActionBarActivity {

    private ReadWifiPasswordsAsync mReadWifiPasswordsAsync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        PasswordAdapter mPasswordAdapter = new PasswordAdapter();
        mReadWifiPasswordsAsync = new ReadWifiPasswordsAsync(mPasswordAdapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.action_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_lock_open);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.password_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
        recyclerView.setAdapter(mPasswordAdapter);
        recyclerView.setHasFixedSize(true);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            recyclerView.setItemAnimator(new SlideInLeftAnimator());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mReadWifiPasswordsAsync.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.mnMore:{
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(getString(R.string.link_my_apps)));
                startActivity(intent);
                break;
            }
            case R.id.mnAbout:{
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(getString(R.string.link_about)));
                startActivity(intent);
                break;
            }
        }
        return false;
    }

}
