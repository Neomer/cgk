package my.neomer.sixtyseconds.states;

import android.app.MediaRouteButton;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import my.neomer.sixtyseconds.R;
import my.neomer.sixtyseconds.gamemodes.BaseGameContext;
import my.neomer.sixtyseconds.model.Question;

public class QuestionSettingsState extends BaseState {

    //region Parcelable

    public static final Parcelable.Creator<QuestionSettingsState> CREATOR = new Parcelable.Creator<QuestionSettingsState>() {

        @Override
        public QuestionSettingsState createFromParcel(Parcel source) {
            return new QuestionSettingsState(source);
        }

        @Override
        public QuestionSettingsState[] newArray(int size) {
            return new QuestionSettingsState[size];
        }
    };

    private QuestionSettingsState(Parcel in) {
        super(in);
    }

    //endregion

    @BindView(R.id.settingsLayout)
    ConstraintLayout settingsLayout;

    @BindView(R.id.spinnerDifficulty)
    Spinner spinnerDifficulty;

    @BindView(R.id.btnStart)
    Button btnStart;

    public QuestionSettingsState() {

    }

    @Override
    public void prepareState(BaseGameContext gameContext, IStateFinishListener stateFinishListener) {
        super.prepareState(gameContext, stateFinishListener);

        ButterKnife.bind(this, getGameContext().getActivity());
        settingsLayout.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btnStart)
    void saveGameSettings() {
        getGameContext().setDifficulty(
                Question.Difficulty.values()[(int)spinnerDifficulty.getSelectedItemId()]);
        finish();
    }

    @Override
    public void start() {

    }

    @Override
    public void finish() {
        settingsLayout.setVisibility(View.INVISIBLE);
        super.finish();
    }
}
