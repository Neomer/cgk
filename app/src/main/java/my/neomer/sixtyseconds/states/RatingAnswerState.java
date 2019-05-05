package my.neomer.sixtyseconds.states;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import my.neomer.sixtyseconds.R;
import my.neomer.sixtyseconds.gamemodes.BaseGameContext;

public class RatingAnswerState extends AnswerState {

    //region Parcelable

    public static final Parcelable.Creator<RatingAnswerState> CREATOR = new Parcelable.Creator<RatingAnswerState>() {

        @Override
        public RatingAnswerState createFromParcel(Parcel source) {
            return new RatingAnswerState(source);
        }

        @Override
        public RatingAnswerState[] newArray(int size) {
            return new RatingAnswerState[size];
        }
    };

    private RatingAnswerState(Parcel in) {
        super(in);
    }

    //endregion

    public RatingAnswerState() {

    }

    @BindView(R.id.txtAdditionalPoints)
    TextView txtAdditionalPoints;

    @Override
    public void prepareState(BaseGameContext gameContext, IStateFinishListener stateFinishListener) {
        super.prepareState(gameContext, stateFinishListener);

        ButterKnife.bind(this, gameContext.getActivity());
        if (getGameContext().getAnswer().getValue() != null && getGameContext().getAnswer().getValue().isCorrect()) {
            txtAdditionalPoints.setText(getGameContext().getActivity().getString(R.string.additional_rate_points, getGameContext().getAnswer().getValue().getPoints()));
            txtAdditionalPoints.setVisibility(View.VISIBLE);
        } else {
            txtAdditionalPoints.setVisibility(View.GONE);
        }
    }

    @Override
    public void finish() {
        txtAdditionalPoints.setVisibility(View.GONE);
        super.finish();
    }

    @Override
    protected void showIsCorrect() {
        super.showIsCorrect();
    }
}
