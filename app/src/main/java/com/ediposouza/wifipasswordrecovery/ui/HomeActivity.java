package com.ediposouza.wifipasswordrecovery.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ediposouza.wifipasswordrecovery.BuildConfig;
import com.ediposouza.wifipasswordrecovery.R;
import com.ediposouza.wifipasswordrecovery.WPRApp;
import com.ediposouza.wifipasswordrecovery.inappbilling.IabHelper;
import com.ediposouza.wifipasswordrecovery.inappbilling.IabResult;
import com.ediposouza.wifipasswordrecovery.inappbilling.Inventory;
import com.ediposouza.wifipasswordrecovery.inappbilling.Purchase;
import com.ediposouza.wifipasswordrecovery.sync.CheckSUAsync;
import com.ediposouza.wifipasswordrecovery.ui.adapter.PasswordAdapter;
import com.ediposouza.wifipasswordrecovery.ui.fragments.AdFragment;
import com.ediposouza.wifipasswordrecovery.ui.widget.DividerItemDecoration;
import com.ediposouza.wifipasswordrecovery.ui.widget.SlideInLeftAnimator;
import com.ediposouza.wifipasswordrecovery.utils.HomeHandler;

public class HomeActivity extends AppCompatActivity
        implements IabHelper.QueryInventoryFinishedListener, IabHelper.OnIabPurchaseFinishedListener, IabHelper.OnIabSetupFinishedListener {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private static final String iabpb = "+MQp+0oIEJYuj5LxyK1FEZctgSuXF1oWDsIst8HzhPLNa+xAGFdwLwNUobobw+" +
            "GUAo2Bju631xyH0gEd4OOXR9iZDLTcVS26VoPzDuWWlxi1bKcuEuMqZ23MvpIdja93n1kJXHLh";

    private static final boolean REMOVE_PRO_PURCHASE_TEST = false;
    private static final String ITEM_SKU_TEST = "android.test.purchased";
    private static final String ITEM_SKU_PRO = "com.ediposouza.wpr.pro";
    private static final String ITEM_SKU = BuildConfig.DEBUG ? ITEM_SKU_TEST : ITEM_SKU_PRO;

    private IabHelper mHelper;
    private MenuItem mnBuyItem;
    private LinearLayout mAdsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        WPRApp.initializeAppRate(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_lock_open);

        mAdsLayout = (LinearLayout) findViewById(R.id.ads_layout);
        TextView mLookingSU = (TextView) findViewById(R.id.looking_su);

        PasswordAdapter mPasswordAdapter = new PasswordAdapter();
        HomeHandler mHomeHandler = new HomeHandler(mLookingSU, mPasswordAdapter);
        CheckSUAsync checkSUAsync = new CheckSUAsync(mHomeHandler);
        checkSUAsync.execute();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.password_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
        recyclerView.setAdapter(mPasswordAdapter);
        recyclerView.setHasFixedSize(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            recyclerView.setItemAnimator(new SlideInLeftAnimator());
        }

        initializeIabHelper();
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
        switch (item.getItemId()) {
            case R.id.mnBuy: {
                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) {
                    return false;
                }
                mHelper.launchPurchaseFlow(this, ITEM_SKU, 10001, this);
                break;
            }
            case R.id.mnMore: {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(getString(R.string.link_my_apps)));
                startActivity(intent);
                break;
            }
            case R.id.mnAbout: {
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
    public void onIabSetupFinished(IabResult result) {
        Log.d(TAG, "IAB Setup finished.");
        if (!result.isSuccess()) {
            Toast.makeText(this, getString(R.string.iab_setup_fail) + result, Toast.LENGTH_SHORT).show();
            return;
        }
        // Have we been disposed of in the meantime? If so, quit.
        if (mHelper == null) {
            return;
        }
        Log.d(TAG, "Setup successful. Querying inventory.");
        mHelper.queryInventoryAsync(this);
    }

    @Override
    public void onQueryInventoryFinished(IabResult result, Inventory inv) {
        Log.d(TAG, "Query inventory finished.");
        // Have we been disposed of in the meantime? If so, quit.
        if (mHelper == null) {
            return;
        }
        if (result.isFailure()) {
            Toast.makeText(this, getString(R.string.iab_query_fail) + result, Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d(TAG, "Query inventory was successful.");
        Purchase proPurchase = inv.getPurchase(ITEM_SKU);
        if (proPurchase != null) {
            if (BuildConfig.DEBUG && REMOVE_PRO_PURCHASE_TEST) {
                removeProPurchase(proPurchase);
                return;
            }
            enableProVersion();
        }
    }

    private void removeProPurchase(Purchase proPurchase) {
        mHelper.consumeAsync(proPurchase, new IabHelper.OnConsumeFinishedListener() {
            @Override
            public void onConsumeFinished(Purchase purchase, IabResult result) {
                Log.d(TAG, result.toString());
                WPRApp.proVersion = false;
                Toast.makeText(HomeActivity.this, result.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onIabPurchaseFinished(IabResult result, Purchase info) {
        if (result.getResponse() == IabHelper.IABHELPER_USER_CANCELLED) {
            Toast.makeText(this, getString(R.string.iab_purchase_cancelled), Toast.LENGTH_SHORT).show();
            return;
        }
        if (result.isFailure()) {
            Toast.makeText(this, getString(R.string.iab_purchase_failure) + result, Toast.LENGTH_SHORT).show();
            return;
        }
        if (info.getSku().equals(ITEM_SKU)) {
            Toast.makeText(this, R.string.iab_purchase_pro_ok, Toast.LENGTH_SHORT).show();
            enableProVersion();
        }
    }

    private void initializeIabHelper() {
        mHelper = new IabHelper(this, WPRApp.iabpb.concat(iabpb).concat(AdFragment.iabpb));
        mHelper.enableDebugLogging(BuildConfig.DEBUG);
        mHelper.startSetup(this);
    }

    private void enableProVersion() {
        if (mnBuyItem == null) {
            Log.d(TAG, "mnBuyItem is null");
            new Handler(getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    enableProVersion();
                }
            }, DateUtils.SECOND_IN_MILLIS);
            return;
        }
        WPRApp.proVersion = true;
        mnBuyItem.setVisible(false);
        mAdsLayout.setVisibility(View.GONE);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(R.string.app_name_pro);
        }
    }

}
