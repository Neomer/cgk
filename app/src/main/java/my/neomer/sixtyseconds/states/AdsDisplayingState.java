package my.neomer.sixtyseconds.states;

import android.os.Parcel;
import android.os.Parcelable;

import my.neomer.sixtyseconds.ads.IRewardedAdResultListener;
import my.neomer.sixtyseconds.helpers.ApplicationResources;

public class AdsDisplayingState extends BaseState implements IRewardedAdResultListener {

    private static final String LOG_TAG = "AdsDisplayingState";
    private static final int ADS_SKIP_COUNT = 3;

    //region Parcelable

    public static final Parcelable.Creator<AdsDisplayingState> CREATOR = new Parcelable.Creator<AdsDisplayingState>() {

        @Override
        public AdsDisplayingState createFromParcel(Parcel source) {
            return new AdsDisplayingState(source);
        }

        @Override
        public AdsDisplayingState[] newArray(int size) {
            return new AdsDisplayingState[size];
        }
    };

    private AdsDisplayingState(Parcel in) {
        super(in);
    }

    //endregion

    public AdsDisplayingState() {

    }

    @Override
    public void start() {
        if (getGameContext().getAdsSkipped() < ADS_SKIP_COUNT || !ApplicationResources.getInstance().getAdsProvider().showInterstitialAd(this)) {
            getGameContext().adsSkipped();
            finish();
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void proceed() {

    }

    //region IRewardedAdResultListener implementation

    @Override
    public void onAdClick() {
        getGameContext().setAdsSkipped(0);
        finish();
    }

    @Override
    public void onAdComplete() {
        getGameContext().setAdsSkipped(0);
        finish();
    }

    @Override
    public void onAdLoadFailed() {
        getGameContext().adsSkipped();
        finish();
    }

    //endregion
}
