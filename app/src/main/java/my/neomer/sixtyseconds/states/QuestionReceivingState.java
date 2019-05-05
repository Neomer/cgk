package my.neomer.sixtyseconds.states;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import my.neomer.sixtyseconds.QuestionFragment;
import my.neomer.sixtyseconds.R;
import my.neomer.sixtyseconds.gamemodes.BaseGameContext;
import my.neomer.sixtyseconds.helpers.ActivityHelper;
import my.neomer.sixtyseconds.helpers.ApplicationResources;
import my.neomer.sixtyseconds.model.Question;
import my.neomer.sixtyseconds.transport.Callback;

/**
 * Состояние: Получение вопроса.
 */
public class QuestionReceivingState extends BaseState
        implements Callback<Question> {

    //region Parcelable

    public static final Parcelable.Creator<QuestionReceivingState> CREATOR = new Parcelable.Creator<QuestionReceivingState>() {

        @Override
        public QuestionReceivingState createFromParcel(Parcel source) {
            return new QuestionReceivingState(source);
        }

        @Override
        public QuestionReceivingState[] newArray(int size) {
            return new QuestionReceivingState[size];
        }
    };

    private QuestionReceivingState(Parcel in) {
        super(in);
    }

    //endregion

    public QuestionReceivingState() {
    }

    private static final String TAG = "QuestionReceivingState";

    @BindView(R.id.txtTime)
    TextView txtTimeCountdown;

    @BindView(R.id.btnStart)
    Button btnStart;

    @BindView(R.id.settingsLayout)
    ConstraintLayout settingsLayout;

    private QuestionFragment questionFragment;

    @Override
    public void prepareState(BaseGameContext gameContext, IStateFinishListener stateFinishListener) {
        super.prepareState(gameContext, stateFinishListener);
        questionFragment = (QuestionFragment)getGameContext().getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment);

        ButterKnife.bind(this, getGameContext().getActivity());

        btnStart.setOnClickListener(null);
        ActivityHelper.hideKeyboard(getGameContext().getActivity());
    }

    @Override
    public void start() {
        btnStart.setText(R.string.wait_countdown_text);
        txtTimeCountdown.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getGameContext().getActivity().getResources().getDimension(R.dimen.title_font_size));
        txtTimeCountdown.setText(R.string.updating_message);
        questionFragment.clear();

        ApplicationResources.getInstance()
                .getQuestionProvider()
                .getNextQuestion(getGameContext().getDifficulty(), this);
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void onReady(Question data) {
        getGameContext().getQuestion().setValue(data);
        finish();
    }

    @Override
    public void onFailure(Throwable t) {
        Log.e(TAG, t.getMessage());
        start();
    }
}
