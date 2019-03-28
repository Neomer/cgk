package my.neomer.sixtyseconds;

public interface ICountdownListener {

    void updateTime(int value);

    void countFinish();

    void countCancel();

}
