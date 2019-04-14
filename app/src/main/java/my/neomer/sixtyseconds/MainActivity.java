package my.neomer.sixtyseconds;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.yandex.metrica.YandexMetrica;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import my.neomer.sixtyseconds.model.Answer;
import my.neomer.sixtyseconds.model.Question;
import my.neomer.sixtyseconds.transport.TransportConfiguration;

public class MainActivity
        extends AppCompatActivity
        implements ICountdownListener, View.OnClickListener, Observer<Question>, SoundPool.OnLoadCompleteListener {

    private static final int MAX_STREAMS = 2;
    private static final String TAG = "MainActivity";
    private static final int SKIP_ADS_COUNT = 5;        // Сколько вопросов показывать без рекламы
    private static final int GUESS_MAX_TIME = 20;

    // Configuration keys
    private static final String USER_UUID_KEY = "USER_UUID";
    private static final String DIFFICULTY_KEY = "DIFFICULTY";
    private static final String ADS_COUNTER_KEY = "ADS";

    private TextView txtCountdown;
    private TextView txtGuess;
    private Button btnStart, btnSendGuess;
    private AsyncTask<Void, Integer, Void> mainCountdown, guessCountdown;
    private ConstraintLayout voteLayout, mainLayout;
    private ImageButton btnLike, btnDislike;
    private CardView cardGuess, cardCorrect;
    private ProgressBar progressBarGuess;
    private ImageView imgCorrect;
    private TextView txtCorrect;

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
        btnStart.callOnClick();
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {

    }

    private enum GameState {
        Updating,
        Idle,
        Counting,
        TypingGuess,
        Result,
        DisplayAds
    }
    private GameState state;

    @Override
    protected void onPause() {
        YandexMetrica.getReporter(getApplicationContext(), AppMetricaHelper.AppKey).pauseSession();
        saveAdsState();
        super.onPause();
    }

    @Override
    protected void onStop() {
        saveAdsState();
        super.onStop();
    }

    /**
     * Сохраняет количество пропущенных вопросов для показа рекламы
     */
    private void saveAdsState() {
        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = pref.edit();
        ed.putInt(ADS_COUNTER_KEY, ad_skip);
        ed.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        YandexMetrica.getReporter(getApplicationContext(), AppMetricaHelper.AppKey).resumeSession();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadSounds();
        loadAds();

        txtCountdown = findViewById(R.id.txtTime);

        btnStart = findViewById(R.id.btnStart);
        btnStart.setOnClickListener(this);

        mainLayout = findViewById(R.id.mainLayout);
        voteLayout = findViewById(R.id.layoutVote);

        btnLike = findViewById(R.id.btnLike);
        btnLike.setOnClickListener(this);

        btnDislike = findViewById(R.id.btnDislike);
        btnDislike.setOnClickListener(this);

        cardGuess = findViewById(R.id.cardGuess);
        progressBarGuess = findViewById(R.id.progressBarGuess);
        progressBarGuess.setMax(GUESS_MAX_TIME);
        btnSendGuess = findViewById(R.id.btnSendGuess);
        btnSendGuess.setOnClickListener(this);
        txtGuess = findViewById(R.id.txtGuess);

        cardCorrect = findViewById(R.id.cardCorrect);
        imgCorrect = findViewById(R.id.imgCorrect);
        txtCorrect = findViewById(R.id.txtCorrect);

        mViewModel = ViewModelProviders.of(this).get(QuestionFragmentViewModel.class);
        mViewModel.getQuestion().observe(this, this);
        mViewModel.getAnswer().observe(this, new Observer<Answer>() {
            @Override
            public void onChanged(@Nullable Answer answer) {
                showCardCorrect(answer.isCorrect());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideCardCorrect();
                    }
                }, 3000);
            }
        });

        questionFragment = (QuestionFragment)getSupportFragmentManager().findFragmentById(R.id.fragment);

        loadPreferences();
        updateQuestion();
    }

    private void showCardCorrect(boolean correct) {
        if (correct) {
            imgCorrect.setImageResource(R.drawable.ic_correct_w48);
            txtCorrect.setText(R.string.correct_label);
        } else {
            imgCorrect.setImageResource(R.drawable.ic_wrong_w48);
            txtCorrect.setText(R.string.wrong_label);
        }
        cardCorrect.setVisibility(View.VISIBLE);
    }

    private void hideCardCorrect() {
        cardCorrect.setVisibility(View.INVISIBLE);
    }

    /**
     * Подготовить систему отображения рекламы
     */
    private void loadAds() {
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
    }

    /**
     * Подготовтиь библиотеку звуков
     */
    private void loadSounds() {
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
    }

    /**
     * Загрузить настройки
     */
    private void loadPreferences() {
        SharedPreferences pref = getPreferences(MODE_PRIVATE);

        String user = pref.getString(USER_UUID_KEY, null);
        int difficulty = pref.getInt(DIFFICULTY_KEY, 5);
        ad_skip = pref.getInt(ADS_COUNTER_KEY, 0);

        if (user == null) {
            user = UUID.randomUUID().toString();
            SharedPreferences.Editor ed = pref.edit();
            ed.putString(USER_UUID_KEY, user);
            ed.apply();
        }
        mViewModel.getProvider().setConfiguration(new TransportConfiguration(user, difficulty));
    }

    //region States processing

    /**
     * Получить новый вопрос от сервера
     */
    private void updateQuestion() {
        state = GameState.Updating;
        btnStart.setText(R.string.wait_countdown_text);
        txtCountdown.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.title_font_size));
        txtCountdown.setText(R.string.updating_message);
        questionFragment.clear();
        mViewModel.update();
    }

    /**
     * Обновить счетчик времени
     * @param value Время
     */
    @Override
    public void updateTime(AsyncTask<Void, Integer, Void> timer, int value) {
        if (timer instanceof Countdown) {
            txtCountdown.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    getResources().getDimension(R.dimen.clock_font_size));
            txtCountdown.setText(String.valueOf(value));
            if (value == 5) {
                soundPool.play(countDownSoundId, 1, 1, 0 ,0, 1);
            }
        } else if (timer instanceof GuessTypingCountdown) {
            progressBarGuess.setProgress(value);
        }
    }

    /**
     * Отсчет закончился. Время вышло.
     */
    @Override
    public void countFinish(AsyncTask<Void, Integer, Void> timer) {
        if (timer instanceof Countdown) {
            displayGuessCard();
            txtCountdown.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    getResources().getDimension(R.dimen.title_font_size));
            txtCountdown.setText(R.string.finish_message);
        } else if (timer instanceof GuessTypingCountdown) {
            displayAnswer();
        }
    }

    /**
     * Отсчет закончился. Нажали "Есть ответ".
     */
    @Override
    public void countCancel(AsyncTask<Void, Integer, Void> timer) {
        if (timer instanceof Countdown) {
            displayGuessCard();
            soundPool.play(timeIsUpSoundId, 1, 1, 0, 0, 1);
            txtCountdown.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    getResources().getDimension(R.dimen.title_font_size));
            txtCountdown.setText(R.string.cancel_message);
        } else if (timer instanceof GuessTypingCountdown) {
            displayAnswer();
        }
    }

    /**
     * Отобразить окно ввода ответа
     */
    private void displayGuessCard() {
        state = GameState.TypingGuess;
        btnStart.setEnabled(false);
        cardGuess.setVisibility(View.VISIBLE);
        guessCountdown = new GuessTypingCountdown(this);
        guessCountdown.execute();
    }

    private void hideGuessCard() {
        ActivityHelper.hideKeyboard(this);
        btnStart.setEnabled(true);
        cardGuess.setVisibility(View.INVISIBLE);
        txtGuess.setText("");
    }

    /**
     * Отобразить правильный ответ
     */
    private void displayAnswer() {
        String guess = txtGuess.getText().toString();
        hideGuessCard();
        showVoting();
        state = GameState.Result;
        btnStart.setText(getResources().getString(R.string.next_question_message));
        questionFragment.displayAnswer(guess);
    }

    /**
     * Начать отсчет времени
     */
    private void startTimer() {
        state = GameState.Counting;
        btnStart.setText(getResources().getString(R.string.cancel_countdown_text));
        mainCountdown = new Countdown(this);
        mainCountdown.execute();
    }

    /**
     * Отобразить рекламу
     */
    private void displayAds() {
        hideGuessCard();
        hideCardCorrect();
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

    //endregion

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
                    mainCountdown.cancel(true);
                    break;

                case TypingGuess:
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
        } else if (v == btnSendGuess) {
            guessCountdown.cancel(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_privacypolicy:
            {
                startActivity(new Intent(this, PrivacyPolicyActivity.class));
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    /**
     * Спрятать окно для оценки вопроса
     */
    private void hideVoting() {
        voteLayout.setVisibility(View.INVISIBLE);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(mainLayout);
        constraintSet.connect(R.id.fragment, ConstraintSet.BOTTOM, R.id.btnStart, ConstraintSet.TOP,0);
        constraintSet.applyTo(mainLayout);
    }

    /**
     * Отобразить окно для оценки вопроса
     */
    private void showVoting() {
        voteLayout.setVisibility(View.VISIBLE);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(mainLayout);
        constraintSet.connect(R.id.fragment, ConstraintSet.BOTTOM, R.id.layoutVote, ConstraintSet.TOP,0);
        constraintSet.applyTo(mainLayout);
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
            updateListener.updateTime(this, values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            updateListener.countFinish(this);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            updateListener.countCancel(this);
        }
    }

    private static class GuessTypingCountdown extends AsyncTask<Void, Integer, Void>
    {

        private final ICountdownListener updateListener;

        GuessTypingCountdown(ICountdownListener updateListener) {
            this.updateListener = updateListener;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (int time = GUESS_MAX_TIME; time > 0; --time) {
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
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            this.updateListener.countFinish(this);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            this.updateListener.updateTime(this, values[0]);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            updateListener.countCancel(this);
        }
    }

}
