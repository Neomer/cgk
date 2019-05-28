package my.neomer.sixtyseconds.gamemodes;

import android.os.Parcel;
import android.os.Parcelable;

import my.neomer.sixtyseconds.core.InfinitePipeline;
import my.neomer.sixtyseconds.states.AdsDisplayingState;
import my.neomer.sixtyseconds.states.AnswerReceivingState;
import my.neomer.sixtyseconds.states.AnswerState;
import my.neomer.sixtyseconds.states.GuessInputState;
import my.neomer.sixtyseconds.states.IState;
import my.neomer.sixtyseconds.states.QuestionReceivingState;
import my.neomer.sixtyseconds.states.QuestionSettingsState;
import my.neomer.sixtyseconds.states.QuestionState;

public class SinglePlayerGameMode extends BaseGameMode {

    //region Parcelable

    protected SinglePlayerGameMode(Parcel in) {
        super(in);

    }

    public static final Parcelable.Creator<SinglePlayerGameMode> CREATOR = new Parcelable.Creator<SinglePlayerGameMode>() {

        @Override
        public SinglePlayerGameMode createFromParcel(Parcel source) {
            return new SinglePlayerGameMode(source);
        }

        @Override
        public SinglePlayerGameMode[] newArray(int size) {
            return new SinglePlayerGameMode[size];
        }
    };

    //endregion

    public SinglePlayerGameMode() {
        super(new SinglePlayerContext(1),
                new InfinitePipeline<IState>(
                        1,
                        new QuestionSettingsState(),
                        new QuestionReceivingState(),
                        new QuestionState(),
                        new GuessInputState(),
                        new AnswerReceivingState(),
                        new AnswerState(),
                        new AdsDisplayingState()
                ));
    }

    @Override
    public int gameModeId() {
        return 1;
    }
}
