package my.neomer.sixtyseconds;

import android.os.AsyncTask;

public interface ICountdownListener {

    void updateTime(AsyncTask<Void, Integer, Void> timer, int value);

    void countFinish(AsyncTask<Void, Integer, Void> timer);

    void countCancel(AsyncTask<Void, Integer, Void> timer);

}
