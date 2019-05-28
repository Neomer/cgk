package my.neomer.sixtyseconds.gamemodes;

import android.annotation.SuppressLint;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;

import java.io.Serializable;

import my.neomer.sixtyseconds.helpers.DifficultyHelper;
import my.neomer.sixtyseconds.model.Answer;
import my.neomer.sixtyseconds.model.Question;

public class BaseGameContext extends ViewModel implements Parcelable {

    //region Parcelable

    protected BaseGameContext(Parcel in) {
        difficulty = DifficultyHelper.FromInt(in.readInt());
    }

    public static final Creator<BaseGameContext> CREATOR = new Creator<BaseGameContext>() {
        @Override
        public BaseGameContext createFromParcel(Parcel in) {
            return new BaseGameContext(in);
        }

        @Override
        public BaseGameContext[] newArray(int size) {
            return new BaseGameContext[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(DifficultyHelper.ToInt(difficulty));
    }

    //endregion

    public BaseGameContext(int gameModeId) {
        this.gameMode = gameModeId;
    }

    private final MutableLiveData<Question> question = new MutableLiveData<>();
    private final MutableLiveData<Answer> answer = new MutableLiveData<>();
    @SuppressLint("StaticFieldLeak")
    private FragmentActivity activity;
    private String guess;
    private Question.Difficulty difficulty;
    private int gameMode;

    public MutableLiveData<Question> getQuestion() {
        return question;
    }
    public MutableLiveData<Answer> getAnswer() {
        return answer;
    }
    public FragmentActivity getActivity() {
        return activity;
    }
    public void setActivity(FragmentActivity activity) {
        this.activity = activity;
    }
    public String getGuess() {
        return guess;
    }
    public void setGuess(String guess) {
        this.guess = guess;
    }
    public Question.Difficulty getDifficulty() {
        return difficulty;
    }
    public void setDifficulty(Question.Difficulty difficulty) {
        this.difficulty = difficulty;
    }
    public int getGameModeId() { return gameMode; }
}
