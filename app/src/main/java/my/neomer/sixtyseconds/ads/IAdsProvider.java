package my.neomer.sixtyseconds.ads;

import android.content.Context;

public interface IAdsProvider {

    void initialize(Context context);

    boolean showInterstitialAd(IRewardedAdResultListener listener);

    boolean showRewardedAd(IRewardedAdResultListener listener);

}
