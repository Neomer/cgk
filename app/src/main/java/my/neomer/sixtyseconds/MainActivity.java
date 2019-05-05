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
    private IGameMode gameMode;

    @Override
    protected void onPause() {
        YandexMetrica.getReporter(getApplicationContext(), AppMetricaHelper.AppKey).pauseSession();
        saveAdsState();
        gameMode.getCurrentState().pause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        saveAdsState();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        gameMode.finish();
        super.onDestroy();
    }

    /**
     * Сохраняет количество пропущенных вопросов для показа рекламы
     */
    private void saveAdsState() {
        ApplicationResources.getInstance().savePreferences(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        YandexMetrica.getReporter(getApplicationContext(), AppMetricaHelper.AppKey).resumeSession();
        gameMode.getCurrentState().proceed();
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

        loadGameMode();
        loadAds();
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

    /**
     * Подготовить систему отображения рекламы
     */
    private void loadAds() {
        ApplicationResources.getInstance().loadAds(this);
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
}
