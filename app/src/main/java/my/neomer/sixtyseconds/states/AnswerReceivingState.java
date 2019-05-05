package my.neomer.sixtyseconds.states;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import my.neomer.sixtyseconds.QuestionFragment;
import my.neomer.sixtyseconds.R;
import my.neomer.sixtyseconds.gamemodes.BaseGameContext;
import my.neomer.sixtyseconds.helpers.ActivityHelper;
import my.neomer.sixtyseconds.helpers.ApplicationResources;
import my.neomer.sixtyseconds.model.Answer;
import my.neomer.sixtyseconds.transport.Callback;

public class AnswerReceivingState extends BaseState
    implements Callback<Answer> {

    private static final String LOG_TAG = "AnswerReceivingState";


    //region Parcelable

    public static final Parcelable.Creator<AnswerReceivingState> CREATOR = new Parcelable.Creator<AnswerReceivingState>() {

        @Override
        public AnswerReceivingState createFromParcel(Parcel source) {
            return new AnswerReceivingState(source);
        }

        @Override
        public AnswerReceivingState[] newArray(int size) {
            return new AnswerReceivingState[size];
        }
    };

    private AnswerReceivingState(Parcel in) {
        super(in);
    }

    //endregion

    @BindView(R.id.txtTime)
    TextView txtTimeCountdown;

    @BindView(R.id.btnStart)
    Button btnStart;

    private QuestionFragment questionFragment;


    public AnswerReceivingState() {

    }

    @Override
    public void prepareState(BaseGameContext gameContext, IStateFinishListener stateFinishListener) {
        super.prepareState(gameContext, stateFinishListener);

        ButterKnife.bind(this, getGameContext().getActivity());

        questionFragment = (QuestionFragment)getGameContext().getActivity()
                .getSupportFragmentManager()
                .findFragmentById(R.id.fragment);

        btnStart.setOnClickListener(null);
        ActivityHelper.hideKeyboard(getGameContext().getActivity());
    }

    @Override
    public void start() {
        btnStart.setText(R.string.wait_countdown_text);
        txtTimeCountdown.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getGameContext().getActivity().getResources().getDimension(R.dimen.title_font_size));
        txtTimeCountdown.setText(R.string.receiving_answer_message);
        questionFragment.clear();

        ApplicationResources.getInstance()
                .getQuestionProvider()
                .getAnswer(getGameContext().getQuestion().getValue(),
                        getGameContext().getGuess(),
                        this);
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void onReady(Answer data) {
        getGameContext().getAnswer().setValue(data);
        finish();
    }

    @Override
    public void onFailure(Throwable t) {
        Log.e(LOG_TAG, t.getMessage());
        start();
    }
}
