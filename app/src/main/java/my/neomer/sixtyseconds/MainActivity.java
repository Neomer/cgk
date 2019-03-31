package my.neomer.sixtyseconds;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.concurrent.TimeUnit;

import my.neomer.sixtyseconds.model.Question;

public class MainActivity
        extends AppCompatActivity
        implements ICountdownListener, View.OnClickListener, Observer<Question>, SoundPool.OnLoadCompleteListener {

    private static final int MAX_STREAMS = 2;
    private static final String TAG = "MainActivity";
    private static final int SKIP_ADS_COUNT = 3;        // Сколько вопросов показывать без рекламы
    private TextView txtCountdown;
    private Button btnStart;
    private Countdown countdown;
    private QuestionFragmentViewModel mViewModel;
    private QuestionFragment questionFragment;
    private InterstitialAd mInterstitialAd;
    private int ad_skip = 0;

    private SoundPool soundPool;
    private int timeIsUpSoundId;
    private int clickSoundId;

    @Override
    public void onChanged(@Nullable Question question) {
        state = GameState.Idle;
        btnStart.setText(getResources().getString(R.string.start_countdown_text));
        txtCountdown.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.title_font_size));
        txtCountdown.setText(R.string.press_start_message);
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {

    }

    private enum GameState {
        Updating,
        Idle,
        Counting,
        Result,
        DisplayAds
    }
    private GameState state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        soundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener(this);

        timeIsUpSoundId = soundPool.load(this, R.raw.time_is_up, 1);
        clickSoundId = soundPool.load(this, R.raw.click, 1);

        MobileAds.initialize(this, "ca-app-pub-5078878060587689~8320307873");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-5078878060587689/9345738647");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });

        txtCountdown = findViewById(R.id.txtTime);

        btnStart = findViewById(R.id.btnStart);
        btnStart.setOnClickListener(this);

        mViewModel = ViewModelProviders.of(this).get(QuestionFragmentViewModel.class);
        mViewModel.getQuestion().observe(this, this);

        questionFragment = (QuestionFragment)getSupportFragmentManager().findFragmentById(R.id.fragment);

        updateQuestion();
    }



    private void updateQuestion() {
        state = GameState.Updating;
        btnStart.setText(R.string.wait_countdown_text);
        txtCountdown.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.title_font_size));
        txtCountdown.setText(R.string.updating_message);
        questionFragment.clear();
        mViewModel.update();
    }

    @Override
    public void updateTime(int value) {
        txtCountdown.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.clock_font_size));
        txtCountdown.setText(String.valueOf(value));
    }

    @Override
    public void countFinish() {
        soundPool.play(timeIsUpSoundId, 1, 1, 0,0,1);
        txtCountdown.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.title_font_size));
        txtCountdown.setText(R.string.finish_message);
        displayAnswer();
    }

    @Override
    public void countCancel() {
        soundPool.play(timeIsUpSoundId, 1, 1, 0,0,1);
        txtCountdown.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.title_font_size));
        txtCountdown.setText(R.string.cancel_message);
        displayAnswer();
    }

    private void displayAnswer() {
        state = GameState.Result;
        btnStart.setText(getResources().getString(R.string.next_question_message));
        questionFragment.displayAnswer();
    }

    private void startTimer() {
        state = GameState.Counting;
        btnStart.setText(getResources().getString(R.string.cancel_countdown_text));
        countdown = new Countdown(this);
        countdown.execute();
    }

    @Override
    public void onClick(View v) {
        if (v == btnStart) {
            soundPool.play(clickSoundId, 1, 1,0,0,1);

            switch (state) {
                case Updating:
                    break;

                case Idle:
                    if (mViewModel != null && mViewModel.hasValue()) {
                        startTimer();
                    }
                    break;

                case Counting:
                    countdown.cancel(true);
                    break;

                case Result:
                    displayAds();
                    break;

                case DisplayAds:
                    updateQuestion();
                    break;
            }
        }
    }

    private void displayAds() {
        state = GameState.DisplayAds;
        if (mInterstitialAd.isLoaded() && ++ad_skip >= SKIP_ADS_COUNT) {
            ad_skip = 0;
            mInterstitialAd.show();
        } else {
            Log.d(TAG, "Ad is not ready!");
            updateQuestion();
        }
    }

    private static class Countdown extends AsyncTask<Void, Integer, Void> {

        private ICountdownListener updateListener;

        Countdown(ICountdownListener updateListener) {
            this.updateListener = updateListener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (int time = 60; time > 0; --time) {
                try {
                    publishProgress(time);
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    return null;
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            updateListener.updateTime(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            updateListener.countFinish();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            updateListener.countCancel();
        }
    }

}
