package my.neomer.sixtyseconds.states;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import my.neomer.sixtyseconds.QuestionFragment;
import my.neomer.sixtyseconds.R;
import my.neomer.sixtyseconds.gamemodes.BaseGameContext;
import my.neomer.sixtyseconds.helpers.ActivityHelper;

public class AnswerState extends BaseState {

    //region Parcelable

    public static final Parcelable.Creator<AnswerState> CREATOR = new Parcelable.Creator<AnswerState>() {

        @Override
        public AnswerState createFromParcel(Parcel source) {
            return new AnswerState(source);
        }

        @Override
        public AnswerState[] newArray(int size) {
            return new AnswerState[size];
        }
    };

    private AnswerState(Parcel in) {
        super(in);
    }

    //endregion

    @BindView(R.id.btnStart)
    Button btnStart;

    @BindView(R.id.txtTime)
    TextView txtTitle;

    private QuestionFragment questionFragment;

    public AnswerState() {

    }


    @Override
    public void prepareState(BaseGameContext gameContext, IStateFinishListener stateFinishListener) {
        super.prepareState(gameContext, stateFinishListener);

        ButterKnife.bind(this, getGameContext().getActivity());

        btnStart.setText(getGameContext().getActivity().getResources().getString(R.string.next_question_message));
        questionFragment = (QuestionFragment)getGameContext().getActivity()
                .getSupportFragmentManager()
                .findFragmentById(R.id.fragment);

        txtTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getGameContext().getActivity().getResources().getDimension(R.dimen.title_font_size));
        txtTitle.setText(R.string.answer_message);

        questionFragment.displayAnswer(getGameContext().getAnswer().getValue());
    }

    @Override
    public void start() {
        showVoting();
    }

    private void showVoting() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void proceed() {

    }

    @Override
    @OnClick(R.id.btnStart)
    public void finish() {
        ActivityHelper.hideKeyboard(getGameContext().getActivity());
        super.finish();
    }
}
