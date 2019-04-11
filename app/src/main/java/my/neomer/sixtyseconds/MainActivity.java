package my.neomer.sixtyseconds;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.yandex.metrica.YandexMetrica;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import my.neomer.sixtyseconds.model.Question;
import my.neomer.sixtyseconds.transport.TransportConfiguration;

public class MainActivity
        extends AppCompatActivity
        implements ICountdownListener, View.OnClickListener, Observer<Question>, SoundPool.OnLoadCompleteListener {

    private static final int MAX_STREAMS = 2;
    private static final String TAG = "MainActivity";
    private static final int SKIP_ADS_COUNT = 5;        // Сколько вопросов показывать без рекламы
    private static final String USER_UUID_KEY = "USER_UUID";
    private static final String DIFFICULTY_KEY = "DIFFICULTY";
    private TextView txtCountdown;
    private Button btnStart;
    private Countdown countdown;
    private ConstraintLayout voteLayout, mainLayout;
    private ImageButton btnLike, btnDislike;

    private QuestionFragmentViewModel mViewModel;
    private QuestionFragment questionFragment;
    private InterstitialAd mInterstitialAd;
    private int ad_skip = 0;

    private SoundPool soundPool;
    private int timeIsUpSoundId;
    private int clickSoundId;
    private int countDownSoundId;

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
    protected void onPause() {
        YandexMetrica.getReporter(getApplicationContext(), AppMetricaHelper.AppKey).pauseSession();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        YandexMetrica.getReporter(getApplicationContext(), AppMetricaHelper.AppKey).resumeSession();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        soundPool = new SoundPool.Builder()
                .setMaxStreams(MAX_STREAMS)
                .setAudioAttributes(
                        new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build())
                .build();
        soundPool.setOnLoadCompleteListener(this);

        timeIsUpSoundId = soundPool.load(this, R.raw.time_is_up, 1);
        clickSoundId = soundPool.load(this, R.raw.click, 1);
        countDownSoundId = soundPool.load(this, R.raw.countdown, 1);

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

        mainLayout = findViewById(R.id.mainLayout);
        voteLayout = findViewById(R.id.layoutVote);

        btnLike = findViewById(R.id.btnLike);
        btnDislike = findViewById(R.id.btnDislike);

        btnLike.setOnClickListener(this);
        btnDislike.setOnClickListener(this);

        mViewModel = ViewModelProviders.of(this).get(QuestionFragmentViewModel.class);
        mViewModel.getQuestion().observe(this, this);

        questionFragment = (QuestionFragment)getSupportFragmentManager().findFragmentById(R.id.fragment);

        loadPreferences();
        updateQuestion();
    }

    private void loadPreferences() {
        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        String user = pref.getString(USER_UUID_KEY, null);
        int difficulty = pref.getInt(DIFFICULTY_KEY, 5);

        if (user == null) {
            user = UUID.randomUUID().toString();
            SharedPreferences.Editor ed = pref.edit();
            ed.putString(USER_UUID_KEY, user);
            ed.commit();
        }
        mViewModel.getProvider().setConfiguration(new TransportConfiguration(user, difficulty));
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
        if (value == 5) {
            soundPool.play(countDownSoundId, 1, 1, 0 ,0, 1);
        }
    }

    @Override
    public void countFinish() {
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
        showVoting();
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
        soundPool.play(clickSoundId, 1, 1,0,0,1);
        if (v == btnStart) {

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
        } else if (v == btnLike) {
            mViewModel.like();
            hideVoting();
        } else if (v == btnDislike) {
            mViewModel.dislike();
            hideVoting();
        }
    }

    private void hideVoting() {
        voteLayout.setVisibility(View.INVISIBLE);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(mainLayout);
        constraintSet.connect(R.id.fragment, ConstraintSet.BOTTOM, R.id.btnStart, ConstraintSet.TOP,0);
        constraintSet.applyTo(mainLayout);
    }

    private void showVoting() {
        voteLayout.setVisibility(View.VISIBLE);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(mainLayout);
        constraintSet.connect(R.id.fragment, ConstraintSet.BOTTOM, R.id.layoutVote, ConstraintSet.TOP,0);
        constraintSet.applyTo(mainLayout);
    }


    private void displayAds() {
        voteLayout.setVisibility(View.INVISIBLE);
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
