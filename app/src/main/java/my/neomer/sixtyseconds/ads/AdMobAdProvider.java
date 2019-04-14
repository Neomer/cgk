package my.neomer.sixtyseconds.ads;

import android.content.Context;
import android.net.sip.SipSession;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import my.neomer.sixtyseconds.helpers.ApplicationResources;

public class AdMobAdProvider extends AdListener implements IAdsProvider {
    private static final String LOG_TAG = "AdMobAdProvider";

    private static final String ADMOB_APP_KEY = "ca-app-pub-5078878060587689~8320307873";
    private static final String ADMOB_APP_KEY_DEBUG = "ca-app-pub-3940256099942544~3347511713"; // Sample adMob app
    private static final String ADMOB_BETWEEN_ACTIVITY_KEY = "ca-app-pub-5078878060587689/9345738647";
    private static final String ADMOB_BETWEEN_ACTIVITY_KEY_DEBUG = "ca-app-pub-3940256099942544/1033173712"; // Sample InterstitialAd

    private InterstitialAd interstitialAd;
    private IRewardedAdResultListener resultListener;

    @Override
    public void initialize(Context context) {
        MobileAds.initialize(context, (ApplicationResources.getInstance().isDebug() ? ADMOB_APP_KEY_DEBUG : ADMOB_APP_KEY));
        interstitialAd = new InterstitialAd(context);
        interstitialAd.setAdUnitId((ApplicationResources.getInstance().isDebug() ? ADMOB_BETWEEN_ACTIVITY_KEY_DEBUG : ADMOB_BETWEEN_ACTIVITY_KEY));
        interstitialAd.loadAd(new AdRequest.Builder().build());
        interstitialAd.setAdListener(this);
    }

    @Override
    public void onAdClosed() {
        Log.d(LOG_TAG, "Ad closed. Reload...");
        interstitialAd.loadAd(new AdRequest.Builder().build());
        if (resultListener != null) {
            resultListener.onAdComplete();
        }
    }

    @Override
    public void onAdFailedToLoad(int i) {
        Log.e(LOG_TAG, "Failed to load ad! Error: " + i);
        interstitialAd.loadAd(new AdRequest.Builder().build());
        if (resultListener != null) {
            resultListener.onAdLoadFailed();
        }
    }

    @Override
    public void onAdClicked() {
        Log.d(LOG_TAG, "Ad clicked!");
        interstitialAd.loadAd(new AdRequest.Builder().build());
        if (resultListener != null) {
            resultListener.onAdClick();
        }
    }

    @Override
    public void onAdImpression() {
        Log.d(LOG_TAG, "Ad impressed!");
        interstitialAd.loadAd(new AdRequest.Builder().build());
        if (resultListener != null) {
            resultListener.onAdClick();
        }
    }

    @Override
    public boolean showInterstitialAd(IRewardedAdResultListener listener) {
        resultListener = listener;
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
            return true;
        }
        return false;
    }

    @Override
    public boolean showRewardedAd(IRewardedAdResultListener listener) {
        resultListener = listener;
        return false;
    }
}
