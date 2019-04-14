package my.neomer.sixtyseconds.states;

import android.os.AsyncTask;
import android.util.Log;

import java.util.concurrent.TimeUnit;

class EscalationTimer extends AsyncTask<Void, Integer, Void>
{
    private static final String LOG_TAG = "EscalationTimer";

    private int escalationTime;
    private BaseEscalationState state;
    private volatile boolean pause = false;
    private volatile boolean eventsDisabled = false;

    EscalationTimer(BaseEscalationState state, int escalationTime) {
        this.escalationTime = escalationTime;
        this.state = state;
    }

    void setState(BaseEscalationState state) {
        this.state = state;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (!eventsDisabled) {
            state.onFinish();
        }
        super.onPostExecute(aVoid);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        if (!eventsDisabled) {
            state.onTick(values[0]);
        }
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled() {
        Log.d(LOG_TAG, "EscalationTimer.onCancelled()");
        if (!eventsDisabled) {
            state.onCancel();
        }
        super.onCancelled();
    }

    void disableEvents() {
        eventsDisabled = true;
    }

    void enableEvents() {
        eventsDisabled = false;
    }

    void proceed() {
        pause = false;
    }

    void pause() {
        pause = true;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        for (int time = this.escalationTime; time > 0; --time) {
            try {
                publishProgress(time);
                do {
                    TimeUnit.SECONDS.sleep(1);
                } while (pause);
            } catch (InterruptedException e) {
                return null;
            }
        }
        return null;
    }
}
