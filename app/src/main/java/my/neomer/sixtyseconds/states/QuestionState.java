package my.neomer.sixtyseconds.states;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import my.neomer.sixtyseconds.QuestionFragment;
import my.neomer.sixtyseconds.R;
import my.neomer.sixtyseconds.gamemodes.BaseGameContext;
import my.neomer.sixtyseconds.helpers.ActivityHelper;
import my.neomer.sixtyseconds.helpers.ApplicationResources;

public class QuestionState extends BaseEscalationState {
    private static final String LOG_TAG = "QuestionState";

    //region Parcelable

    public static final Parcelable.Creator<QuestionState> CREATOR = new Parcelable.Creator<QuestionState>() {

        @Override
        public QuestionState createFromParcel(Parcel source) {
            return new QuestionState(source);
        }

        @Override
        public QuestionState[] newArray(int size) {
            return new QuestionState[size];
        }
    };

    private QuestionState(Parcel in) {
        super(in);
    }

    //endregion

    @BindView(R.id.txtTime)
    TextView txtCountdown;

    @BindView(R.id.btnStart)
    Button btnStart;

    private QuestionFragment questionFragment;

    public QuestionState() {
        super(60);
    }

    @Override
    protected void onTick(int time) {
        txtCountdown.setTextSize(TypedValue.COMPLEX_UNIT_PX, getGameContext().getActivity().getResources().getDimension(R.dimen.clock_font_size));
        txtCountdown.setText(String.valueOf(time));

        if (time == 5) {
            ApplicationResources.getInstance().playCountDownSound();
        }

    }

    @Override
    protected void onFinish() {
        Log.d(LOG_TAG, "QuestionState.onFinish()");
        txtCountdown.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getGameContext().getActivity().getResources().getDimension(R.dimen.title_font_size));
        txtCountdown.setText(R.string.finish_message);
        finish();
    }

    @Override
    protected void onCancel() {
        Log.d(LOG_TAG, "QuestionState.onCancel()");
        ApplicationResources.getInstance().playTimeIsUpSound();
        txtCountdown.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getGameContext().getActivity().getResources().getDimension(R.dimen.title_font_size));
        txtCountdown.setText(R.string.cancel_message);
        finish();
    }

    @Override
    public void prepareState(BaseGameContext gameContext, IStateFinishListener stateFinishListener) {
        Log.d(LOG_TAG, "QuestionState.prepareState()");
        super.prepareState(gameContext, stateFinishListener);
        ButterKnife.bind(this, getGameContext().getActivity());

        btnStart.setText(getGameContext().getActivity().getResources().getString(R.string.cancel_countdown_text));

        questionFragment = (QuestionFragment)getGameContext().getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment);
        questionFragment.displayQuestion(getGameContext().getQuestion().getValue());
        ActivityHelper.hideKeyboard(getGameContext().getActivity());
    }

    @OnClick(R.id.btnStart)
    void Escalation()
    {
        ApplicationResources.getInstance().playClickSound();
        CancelTimer();
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void finish() {
        Log.d(LOG_TAG, "QuestionState.finish()");
        btnStart.setOnClickListener(null);
        super.finish();
    }
}
