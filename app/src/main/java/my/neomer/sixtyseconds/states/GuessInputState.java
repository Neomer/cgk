package my.neomer.sixtyseconds.states;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import my.neomer.sixtyseconds.R;
import my.neomer.sixtyseconds.gamemodes.BaseGameContext;
import my.neomer.sixtyseconds.helpers.ActivityHelper;
import my.neomer.sixtyseconds.helpers.ApplicationResources;
import my.neomer.sixtyseconds.model.Answer;
import my.neomer.sixtyseconds.transport.Callback;

public class GuessInputState extends BaseEscalationState {
    private static final String LOG_TAG = "GuessInputState";

    //region Parcelable

    private GuessInputState(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<GuessInputState> CREATOR = new Parcelable.Creator<GuessInputState>() {

        @Override
        public GuessInputState createFromParcel(Parcel source) {
            return new GuessInputState(source);
        }

        @Override
        public GuessInputState[] newArray(int size) {
            return new GuessInputState[size];
        }
    };

    //endregion

    @BindView(R.id.cardGuess)
    CardView cardGuess;

    @BindView(R.id.progressBarGuess)
    ProgressBar progressBarGuess;

    @BindView(R.id.btnSendGuess)
    Button btnSendGuess;

    @BindView(R.id.txtGuess)
    TextView txtGuess;

    public GuessInputState() {
        super(20);
    }

    @Override
    protected void onTick(int time) {
        progressBarGuess.setProgress(time);
    }

    @Override
    protected void onFinish() {
        finish();
    }

    @Override
    protected void onCancel() {
        finish();
    }

    @Override
    public void prepareState(BaseGameContext gameContext, IStateFinishListener stateFinishListener) {
        super.prepareState(gameContext, stateFinishListener);
        ButterKnife.bind(this, getGameContext().getActivity());

        txtGuess.setText("");
        btnSendGuess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationResources.getInstance().playClickSound();
                saveGuess();
            }
        });
    }

    private void saveGuess() {
        getGameContext().setGuess(txtGuess.getText().toString());
        finish();
    }

    @Override
    public void start() {
        super.start();
        cardGuess.setVisibility(View.VISIBLE);
    }

    @Override
    public void finish() {
        ActivityHelper.hideKeyboard(getGameContext().getActivity());
        cardGuess.setVisibility(View.INVISIBLE);
        super.finish();
    }
}
