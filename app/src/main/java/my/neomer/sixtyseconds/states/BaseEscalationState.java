package my.neomer.sixtyseconds.states;

import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.concurrent.TimeUnit;

/**
 * Базовый класс для состояний с эскалацией по времени.
 * Указывается время отсчета. Для начала отсчета наследник должен вызвать метод StartTimer(), если эскалация не требуется,
 * вызвать метод CancelTimer().
 * onTick(int) - вызыватся каждую секунду, передает оставшееся до эскалации время.
 * onFinish() - вызывается при достижении эскалации.
 * onCancel() - вызывается при отмене эскалации.
 */
public abstract class BaseEscalationState extends BaseState {
    private static final String LOG_TAG = "BaseEscalationState";

    //region Parcelable

    BaseEscalationState(Parcel in) {
        super(in);
        escalationTime = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(escalationTime);
    }

    //endregion

    private EscalationTimer escalationTimer;
    private int escalationTime;

    BaseEscalationState(int escalationTime) {
        super();
        this.escalationTime = escalationTime;
    }

    private void StartTimer()
    {
        Log.d(LOG_TAG, "BaseEscalationState.StartTimer()");
        escalationTimer = new EscalationTimer(this, escalationTime);
        escalationTimer.enableEvents();
        escalationTimer.execute();
    }

    final void CancelTimer()
    {
        Log.d(LOG_TAG, "BaseEscalationState.CancelTimer()");
        if (!escalationTimer.isCancelled()) {
            escalationTimer.cancel(true);
        }
    }

    @Override
    public void finish() {
        escalationTimer.disableEvents();
        CancelTimer();
        super.finish();
    }

    @Override
    public void start() {
        StartTimer();
    }

    protected abstract void onTick(int time);
    protected abstract void onFinish();
    protected abstract void onCancel();



}
