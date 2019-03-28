package my.neomer.sixtyseconds;

import android.arch.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import java.util.Observable;
import java.util.concurrent.TimeUnit;

import my.neomer.sixtyseconds.model.Question;
import my.neomer.sixtyseconds.transport.FakeQuestionProvider;
import my.neomer.sixtyseconds.transport.IQuestionProvider;

public class MainActivity
        extends AppCompatActivity
        implements ICountdownListener, View.OnClickListener  {

    private TextView txtCountdown;
    private TextView txtAnswer;
    private Button btnStart;
    private Countdown countdown;
    private IQuestionProvider questionProvider;
    private QuestionFragmentViewModel mViewModel;

    private enum GameState {
        Idle,
        Counting,
        Result
    }
    private GameState state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        state = GameState.Idle;

        txtCountdown = findViewById(R.id.txtTime);
        txtAnswer = findViewById(R.id.txtAnswer);

        btnStart = findViewById(R.id.btnStart);
        btnStart.setOnClickListener(this);

        mViewModel = ViewModelProviders.of(this).get(QuestionFragmentViewModel.class);
        updateQuestion();
    }

    private void updateQuestion() {
        state = GameState.Idle;
        btnStart.setText(getResources().getString(R.string.start_countdown_text));
        txtCountdown.setText(R.string.press_start_message);
        txtAnswer.setText("");
        mViewModel.update();
    }

    @Override
    public void updateTime(int value) {
        txtCountdown.setText(String.valueOf(value));
    }

    @Override
    public void countFinish() {
        txtCountdown.setText(R.string.finish_message);
        displayAnswer();
    }

    @Override
    public void countCancel() {
        txtCountdown.setText(R.string.cancel_message);
        displayAnswer();
    }

    private void displayAnswer() {
        state = GameState.Result;
        btnStart.setText(getResources().getString(R.string.next_question_message));
        txtAnswer.setText(mViewModel.getQuestion().getValue().getAnswer());
    }

    private void startTimer() {
        state = GameState.Counting;
        btnStart.setText(getResources().getString(R.string.cancel_countdown_text));
        countdown = new Countdown(this);
        countdown.execute();
    }

    @Override
    public void onClick(View v) {
        if (v == btnStart) {
            switch (state) {
                case Idle:
                    startTimer();
                    break;

                case Counting:
                    countdown.cancel(true);
                    break;

                case Result:
                    updateQuestion();
                    break;
            }
        }
    }

    private class Countdown extends AsyncTask<Void, Integer, Void> {

        private ICountdownListener updateListener;

        public Countdown(ICountdownListener updateListener) {
            this.updateListener = updateListener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (int time = 60; time > 50; --time) {
                try {
                    publishProgress(time);
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    return null;
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            updateListener.updateTime(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            updateListener.countFinish();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            updateListener.countCancel();
        }
    }

}
