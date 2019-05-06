package my.neomer.sixtyseconds.gamemodes;

import android.os.Parcel;
import android.os.Parcelable;

import my.neomer.sixtyseconds.core.InfinitePipeline;
import my.neomer.sixtyseconds.states.AdsDisplayingState;
import my.neomer.sixtyseconds.states.AnswerReceivingState;
import my.neomer.sixtyseconds.states.GuessInputState;
import my.neomer.sixtyseconds.states.IState;
import my.neomer.sixtyseconds.states.QuestionReceivingState;
import my.neomer.sixtyseconds.states.QuestionState;
import my.neomer.sixtyseconds.states.RatingAnswerState;

public class SinglePlayerWithRatesGameMode extends BaseGameMode {

    //region Parcelable

    protected SinglePlayerWithRatesGameMode(Parcel in) {
        super(in);

    }


    public static final Parcelable.Creator<SinglePlayerWithRatesGameMode> CREATOR = new Parcelable.Creator<SinglePlayerWithRatesGameMode>() {

        @Override
        public SinglePlayerWithRatesGameMode createFromParcel(Parcel source) {
            return new SinglePlayerWithRatesGameMode(source);
        }

        @Override
        public SinglePlayerWithRatesGameMode[] newArray(int size) {
            return new SinglePlayerWithRatesGameMode[size];
        }
    };

    //endregion

    /**
     * Новая игра.
     */
    public SinglePlayerWithRatesGameMode() {
        super(new SinglePlayerWithRatesContext(),
                new InfinitePipeline<IState>(
                        new QuestionReceivingState(),
                        new QuestionState(),
                        new GuessInputState(),
                        new AnswerReceivingState(),
                        new RatingAnswerState(),
                        new AdsDisplayingState()

        ));
    }



}
