package my.neomer.sixtyseconds;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import my.neomer.sixtyseconds.helpers.ApplicationResources;
import my.neomer.sixtyseconds.model.UserRating;
import my.neomer.sixtyseconds.transport.Callback;

public class RatingActivity extends AppCompatActivity
    implements Callback<UserRating>
{

    @BindView(R.id.txtPoints)
    TextView txtPoints;

    @BindView(R.id.txtPlace)
    TextView txtPlace;

    @BindView(R.id.imvPlaceIcon)
    ImageView imvPlaceIcon;

    @BindView(R.id.loadUserRatingProgressBar)
    ProgressBar loadUserRatingProgressBar;

    @BindView(R.id.txtFirstPlace)
    TextView txtFirstPlace;

    @BindView(R.id.txtSecondPlace)
    TextView txtSecondPlace;

    @BindView(R.id.txtThirdPlace)
    TextView txtThirdPlace;

    @BindView(R.id.txtFourthPlace)
    TextView txtFourthPlace;

    @BindView(R.id.txtFifthPlace)
    TextView txtFifthPlace;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadUserRatingProgressBar.setVisibility(View.VISIBLE);

        ApplicationResources.getInstance()
                .getQuestionProvider()
                .loadUserRating(this);

    }

    @Override
    public void onReady(UserRating data) {
        loadUserRatingProgressBar.setVisibility(View.INVISIBLE);
        imvPlaceIcon.setVisibility(View.INVISIBLE);

        if (data != null) {
            txtPlace.setText(String.valueOf(data.getPlace()));
            txtPoints.setText(String.valueOf(data.getPoints()));

            if (data.getLeaderboard() != null && data.getLeaderboard().size() > 0) {
                List<TextView> placesViews = new ArrayList<>();
                placesViews.add(txtFirstPlace);
                placesViews.add(txtSecondPlace);
                placesViews.add(txtThirdPlace);
                placesViews.add(txtFourthPlace);
                placesViews.add(txtFifthPlace);
                for (int i = 0; i < data.getLeaderboard().size(); i++) {
                    placesViews.get(i).setText(String.valueOf(data.getLeaderboard().get(i).getPoints()));
                }
            }
        }
    }

    @Override
    public void onFailure(Throwable t) {

        ApplicationResources.getInstance()
                .getQuestionProvider()
                .loadUserRating(this);

    }
}
