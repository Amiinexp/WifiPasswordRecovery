package com.ediposouza.wifipasswordrecovery.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.ediposouza.wifipasswordrecovery.R;
import com.ediposouza.wifipasswordrecovery.WifiPasswordRecoveryApp;
import com.ediposouza.wifipasswordrecovery.adapter.PasswordAdapter;
import com.ediposouza.wifipasswordrecovery.inappbilling.IabHelper;
import com.ediposouza.wifipasswordrecovery.inappbilling.IabResult;
import com.ediposouza.wifipasswordrecovery.inappbilling.Inventory;
import com.ediposouza.wifipasswordrecovery.inappbilling.Purchase;
import com.ediposouza.wifipasswordrecovery.sync.CheckSUAsync;
import com.ediposouza.wifipasswordrecovery.ui.widget.DividerItemDecoration;
import com.ediposouza.wifipasswordrecovery.ui.widget.SlideInLeftAnimator;
import com.ediposouza.wifipasswordrecovery.utils.HomeHandler;

public class HomeActivity extends AppCompatActivity
        implements IabHelper.QueryInventoryFinishedListener {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private static final String ITEM_SKU_TEST = "android.test.purchased";
    private static final String ITEM_SKU_PRO = "com.ediposouza.wifipasswordrecovery";
    private static final String ITEM_SKU = ITEM_SKU_TEST;

    private IabHelper mHelper;
    private MenuItem mnBuyItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        TextView mLookingSU = (TextView) findViewById(R.id.looking_su);

        PasswordAdapter mPasswordAdapter = new PasswordAdapter();
        HomeHandler mHomeHandler = new HomeHandler(mLookingSU, mPasswordAdapter);
        CheckSUAsync checkSUAsync = new CheckSUAsync(mHomeHandler);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_lock_open);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.password_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
        recyclerView.setAdapter(mPasswordAdapter);
        recyclerView.setHasFixedSize(true);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            recyclerView.setItemAnimator(new SlideInLeftAnimator());
        }

        //checkSUAsync.execute();

        initializeIabHelper();
        //WifiPasswordRecoveryApp.initializeAppRate(this);
    }

    private void initializeIabHelper() {
        mHelper = new IabHelper(this, getString(R.string.base64EncodedPublicKey));
        mHelper.enableDebugLogging(true);
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished.");

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    Toast.makeText(HomeActivity.this, "Problem setting up in-app billing: " + result, Toast.LENGTH_SHORT).show();
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                Log.d(TAG, "Setup successful. Querying inventory.");
                //mHelper.consumeAsync((3, getPackageName(),purchaseToken);
                mHelper.queryInventoryAsync(HomeActivity.this);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHelper != null) {
            mHelper.dispose();
        }
        mHelper = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        mnBuyItem = menu.findItem(R.id.mnBuy);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.mnBuy: {
                if (mHelper == null) {
                    return false;
                }
                mHelper.launchPurchaseFlow(this, ITEM_SKU, 10001, new IabHelper.OnIabPurchaseFinishedListener() {
                    @Override
                    public void onIabPurchaseFinished(IabResult result, Purchase info) {
                        if (result.isFailure()) {
                            Toast.makeText(HomeActivity.this, R.string.purchace_failure, Toast.LENGTH_SHORT).show();
                        } else if (info.getSku().equals(ITEM_SKU)) {
                            enableProVersion();
                        }
                    }
                });
                break;
            }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onQueryInventoryFinished(IabResult result, Inventory inv) {
        Log.d(TAG, "Query inventory finished.");

        // Have we been disposed of in the meantime? If so, quit.
        if (mHelper == null) return;

        // Is it a failure?
        if (result.isFailure()) {
            Toast.makeText(this, "Failed to query inventory: " + result, Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "Query inventory was successful.");

        Purchase proPurchase = inv.getPurchase(ITEM_SKU);
        if (proPurchase != null) {
            enableProVersion();
        }
    }

    private void enableProVersion() {
        WifiPasswordRecoveryApp.proVersion = true;
        mnBuyItem.setVisible(false);
        setTitle(R.string.app_name_pro);
    }

}
