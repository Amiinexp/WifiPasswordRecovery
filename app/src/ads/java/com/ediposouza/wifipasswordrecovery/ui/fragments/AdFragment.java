package com.ediposouza.wifipasswordrecovery.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ediposouza.wifipasswordrecovery.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

/**
 * Created by edipo2s on 1/28/15.
 */
public class AdFragment extends Fragment {

    private AdRequest adRequest;
    private InterstitialAd interstitial;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adRequest = new AdRequest.Builder().build();
        interstitial = new InterstitialAd(getActivity());
        interstitial.setAdUnitId(getString(R.string.ad_interstitial_id));
        interstitial.loadAd(adRequest);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ads, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        if(view != null) {
            AdView mAdView = (AdView) view.findViewById(R.id.adView);
            mAdView.loadAd(adRequest);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }

}
