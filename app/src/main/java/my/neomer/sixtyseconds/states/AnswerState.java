package my.neomer.sixtyseconds.states;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import my.neomer.sixtyseconds.QuestionFragment;
import my.neomer.sixtyseconds.R;
import my.neomer.sixtyseconds.gamemodes.BaseGameContext;
import my.neomer.sixtyseconds.helpers.ActivityHelper;
import my.neomer.sixtyseconds.helpers.ApplicationResources;

public class AnswerState extends BaseEscalationState {

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

    protected AnswerState(Parcel in) {
        super(in);
    }

    //endregion

    @BindView(R.id.btnStart)
    Button btnStart;

    @BindView(R.id.txtTime)
    TextView txtTitle;

    @BindView(R.id.cardCorrect)
    CardView cardCorrect;

    @BindView(R.id.txtCorrect)
    TextView txtCorrect;

    @BindView(R.id.imgCorrect)
    ImageView imgCorrect;

    @BindView(R.id.layoutVote)
    ConstraintLayout layoutVote;

    @BindView(R.id.imgShowVoting)
    ImageButton imgShowVoting;

    private QuestionFragment questionFragment;

    public AnswerState() {
        super(3);
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

        imgShowVoting.setVisibility(View.VISIBLE);
        questionFragment.displayAnswer(getGameContext().getAnswer().getValue());
        showIsCorrect();
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    protected void onTick(int time) {

    }

    @Override
    protected void onFinish() {
        hideIsCorrect();
    }

    @Override
    protected void onCancel() {

    }

    protected void showIsCorrect() {
        if (Objects.requireNonNull(getGameContext().getAnswer().getValue()).isCorrect()) {
            imgCorrect.setImageResource(R.drawable.ic_correct_w48);
            txtCorrect.setText(R.string.correct_label);
        } else {
            imgCorrect.setImageResource(R.drawable.ic_wrong_w48);
            txtCorrect.setText(R.string.wrong_label);
        }
        cardCorrect.setVisibility(View.VISIBLE);
    }

    private void hideIsCorrect() {
        cardCorrect.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.imgShowVoting)
    void showVoting() {
        if (layoutVote.getVisibility() == View.VISIBLE) {
            layoutVote.setVisibility(View.INVISIBLE);
        } else {
            layoutVote.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.btnLike)
    void likeIt() {
        ApplicationResources.getInstance()
                .getQuestionProvider()
                .like(getGameContext().getQuestion().getValue());
        showVoting();
        imgShowVoting.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.btnDislike)
    void dislikeIt() {
        ApplicationResources.getInstance()
                .getQuestionProvider()
                .dislike(getGameContext().getQuestion().getValue());
        showVoting();
        imgShowVoting.setVisibility(View.INVISIBLE);
    }


    @Override
    @OnClick(R.id.btnStart)
    public void finish() {
        CancelTimer();
        ActivityHelper.hideKeyboard(getGameContext().getActivity());
        hideIsCorrect();
        imgShowVoting.setVisibility(View.INVISIBLE);
        super.finish();
    }
}
