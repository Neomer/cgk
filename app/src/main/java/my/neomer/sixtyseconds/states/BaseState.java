package my.neomer.sixtyseconds.states;

import android.os.Parcel;

import my.neomer.sixtyseconds.gamemodes.BaseGameContext;

public abstract class BaseState implements IState {

    //region Parcelable

    BaseState(Parcel in) {
        gameContext = (BaseGameContext) in.readSerializable();
        stateFinishListener = (IStateFinishListener) in.readSerializable();
        paused = in.readInt()  == 1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(getGameContext(), 0);
        dest.writeSerializable(stateFinishListener);
        dest.writeInt(paused ? 1 : 0);
    }

    //endregion

    private BaseGameContext gameContext;
    private IStateFinishListener stateFinishListener;

    BaseState() {

    }

    @Override
    public void prepareState(BaseGameContext gameContext, IStateFinishListener stateFinishListener) {
        this.gameContext = gameContext;
        this.stateFinishListener = stateFinishListener;
    }

    BaseGameContext getGameContext() {
        return gameContext;
    }

    @Override
    public void finish() {
        if (stateFinishListener != null) {
            stateFinishListener.onFinish(this);
        }
    }

    private boolean paused = false;

    @Override
    public void pause() {
        paused = true;
    }

    @Override
    public void proceed() {
        paused = false;
    }

    @Override
    public boolean isOnPause() {
        return paused;
    }
}
