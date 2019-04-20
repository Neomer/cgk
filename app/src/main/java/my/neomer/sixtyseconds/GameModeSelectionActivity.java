package my.neomer.sixtyseconds;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import my.neomer.sixtyseconds.gamemodes.IGameMode;
import my.neomer.sixtyseconds.gamemodes.SinglePlayerGameMode;
import my.neomer.sixtyseconds.gamemodes.SinglePlayerWithRatesGameMode;
import my.neomer.sixtyseconds.helpers.ApplicationResources;

public class GameModeSelectionActivity extends AppCompatActivity {

    public static final String GAMEMODE_TAG = "GameModeTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_mode_selection);

        ApplicationResources.getInstance().setDebug(0 != (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));

        ApplicationResources.getInstance().loadSounds(this, null);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnSinglePlayerWithRatesGameMode)
    void SinglePlayerWithRatesGameMode() {
        ApplicationResources.getInstance().playClickSound();
        runGame(new SinglePlayerWithRatesGameMode());
    }

    @OnClick(R.id.btnSinglePlayerGameMode)
    void SinglePlayerGameMode() {
        ApplicationResources.getInstance().playClickSound();
        runGame(new SinglePlayerGameMode());
    }

    private void runGame(IGameMode gameMode) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(GAMEMODE_TAG, gameMode);
        startActivity(intent);
    }

}
