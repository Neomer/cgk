package my.neomer.sixtyseconds;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.yandex.metrica.YandexMetrica;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import my.neomer.sixtyseconds.gamemodes.BaseGameContext;
import my.neomer.sixtyseconds.gamemodes.IGameMode;
import my.neomer.sixtyseconds.helpers.AppMetricaHelper;
import my.neomer.sixtyseconds.helpers.ApplicationResources;
import my.neomer.sixtyseconds.transport.TransportConfiguration;

public class MainActivity
        extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int SKIP_ADS_COUNT = 5;        // Сколько вопросов показывать без рекламы

    // Configuration keys
    private static final String USER_UUID_KEY = "USER_UUID";
    private static final String DIFFICULTY_KEY = "DIFFICULTY";
    private static final String ADS_COUNTER_KEY = "ADS";

    private ConstraintLayout voteLayout, mainLayout;
    private CardView cardGuess, cardCorrect;
    private ImageView imgCorrect;
    private TextView txtCorrect;

    private InterstitialAd mInterstitialAd;
    private int ad_skip = 0;

    private IGameMode gameMode;

    private enum GameState {
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

        mainLayout = findViewById(R.id.mainLayout);
        voteLayout = findViewById(R.id.layoutVote);

        cardCorrect = findViewById(R.id.cardCorrect);
        imgCorrect = findViewById(R.id.imgCorrect);
        txtCorrect = findViewById(R.id.txtCorrect);

        loadGameMode();
        loadAds();
        loadPreferences();
    }

    @Override
    protected void onStart() {
        super.onStart();
        gameMode.run();
    }

    private void loadGameMode() {
        gameMode = getIntent().getParcelableExtra(GameModeSelectionActivity.GAMEMODE_TAG);
        if (gameMode == null) {
            Log.e(TAG, "GameMode not set!");
            finish();
        }
        gameMode.getGameContext().setActivity(this);
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
        ApplicationResources.getInstance().loadAds(this);
    }

    /**
     * Загрузить настройки
     */
    private void loadPreferences() {
        SharedPreferences pref = getPreferences(MODE_PRIVATE);

        String user = pref.getString(USER_UUID_KEY, null);
        int difficulty = pref.getInt(DIFFICULTY_KEY, 5);
        gameMode.getGameContext().setAdsSkipped(pref.getInt(ADS_COUNTER_KEY, 0));

        if (user == null) {
            user = UUID.randomUUID().toString();
            SharedPreferences.Editor ed = pref.edit();
            ed.putString(USER_UUID_KEY, user);
            ed.apply();
        }
        ApplicationResources.getInstance()
                .getQuestionProvider()
                .setConfiguration(new TransportConfiguration(user, difficulty));
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
}
