package my.neomer.sixtyseconds.transport;

public interface Callback<T> {

    void onReady(T data);

    void onFailure(Throwable t);

}
