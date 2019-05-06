package my.neomer.sixtyseconds.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import java.util.UUID;

import my.neomer.sixtyseconds.R;
import my.neomer.sixtyseconds.ads.AdMobAdProvider;
import my.neomer.sixtyseconds.ads.IAdsProvider;
import my.neomer.sixtyseconds.transport.HttpApiProvider;
import my.neomer.sixtyseconds.transport.IApiProvider;
import my.neomer.sixtyseconds.transport.TransportConfiguration;

public class ApplicationResources {
    //region Singleton

    private static final ApplicationResources ourInstance = new ApplicationResources();
    private static final int MAX_STREAMS = 1;
    // Configuration keys
    private static final String USER_UUID_KEY = "USER_UUID";
    private static final String DIFFICULTY_KEY = "DIFFICULTY";
    private static final String ADS_COUNTER_KEY = "ADS";

    public static ApplicationResources getInstance() {
        return ourInstance;
    }

    //endregion

    private ApplicationResources() {
    }

    private IApiProvider questionProvider = new HttpApiProvider();
    private IAdsProvider adsProvider = new AdMobAdProvider();

    private SoundPool soundPool;
    private int timeIsUpSoundId;
    private int clickSoundId;
    private int countDownSoundId;
    private boolean isDebug;
    private int skippedAds;

    public void loadPreferences(FragmentActivity activity) {
        SharedPreferences pref = activity.getPreferences(activity.MODE_PRIVATE);

        String user = pref.getString(USER_UUID_KEY, null);
        int difficulty = pref.getInt(DIFFICULTY_KEY, 5);
        skippedAds = pref.getInt(ADS_COUNTER_KEY, 0);

        if (user == null) {
            user = UUID.randomUUID().toString();
            SharedPreferences.Editor ed = pref.edit();
            ed.putString(USER_UUID_KEY, user);
            ed.apply();
        }
        questionProvider.setConfiguration(new TransportConfiguration(user, difficulty));
    }

    public void savePreferences(FragmentActivity activity) {
        SharedPreferences pref = activity.getPreferences(activity.MODE_PRIVATE);
        SharedPreferences.Editor ed = pref.edit();
        ed.putInt(ADS_COUNTER_KEY, skippedAds);
        ed.apply();
    }

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

    public IApiProvider getQuestionProvider() {
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

    public int getSkippedAds() {
        return skippedAds;
    }

    public void AdSkipped() {
        this.skippedAds++;
    }

    public void AdDisplayed() {
        this.skippedAds = 0;
    }
}
