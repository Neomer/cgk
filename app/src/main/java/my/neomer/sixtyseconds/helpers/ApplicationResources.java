package my.neomer.sixtyseconds.helpers;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import my.neomer.sixtyseconds.R;
import my.neomer.sixtyseconds.ads.AdMobAdProvider;
import my.neomer.sixtyseconds.ads.IAdsProvider;
import my.neomer.sixtyseconds.transport.HttpQuestionProvider;
import my.neomer.sixtyseconds.transport.IQuestionProvider;

public class ApplicationResources {
    //region Singleton

    private static final ApplicationResources ourInstance = new ApplicationResources();
    private static final int MAX_STREAMS = 1;

    public static ApplicationResources getInstance() {
        return ourInstance;
    }

    private ApplicationResources() {
    }

    //endregion

    private IQuestionProvider questionProvider = new HttpQuestionProvider();
    private IAdsProvider adsProvider = new AdMobAdProvider();

    private SoundPool soundPool;
    private int timeIsUpSoundId;
    private int clickSoundId;
    private int countDownSoundId;
    private boolean isDebug;

    public void loadSounds(@NonNull Context context, @Nullable SoundPool.OnLoadCompleteListener completeListener) {
        soundPool = new SoundPool.Builder()
                .setMaxStreams(MAX_STREAMS)
                .setAudioAttributes(
                        new AudioAttributes.Builder()
                                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                                .build())
                .build();

        if (completeListener != null) {
            soundPool.setOnLoadCompleteListener(completeListener);
        }

        timeIsUpSoundId = soundPool.load(context, R.raw.time_is_up, 1);
        countDownSoundId = soundPool.load(context, R.raw.countdown, 1);
        clickSoundId = soundPool.load(context, R.raw.click, 1);
    }

    public void loadAds(@NonNull Context context) {
        adsProvider.initialize(context);
    }

    public void playClickSound() {
        soundPool.play(clickSoundId, 1, 1, 0, 0, 1);
    }

    public void playCountDownSound() {
        soundPool.play(countDownSoundId, 1, 1, 0,0,1);
    }

    public void playTimeIsUpSound() {
        soundPool.stop(countDownSoundId);
        soundPool.play(timeIsUpSoundId, 1, 1, 0,0,1);
    }

    public IQuestionProvider getQuestionProvider() {
        return questionProvider;
    }

    public IAdsProvider getAdsProvider() {
        return adsProvider;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public void setDebug(boolean debug) {
        isDebug = debug;
    }
}
