package my.neomer.sixtyseconds.gamemodes;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import my.neomer.sixtyseconds.model.Question;

public class SinglePlayerWithRatesContext extends BaseGameContext {

    public SinglePlayerWithRatesContext(int gameModeId) {
        super(gameModeId);
        setDifficulty(Question.Difficulty.Hardest);
    }

}
