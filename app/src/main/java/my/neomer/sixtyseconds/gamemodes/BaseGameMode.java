package my.neomer.sixtyseconds.gamemodes;

import android.os.Parcel;
import android.support.annotation.NonNull;
import android.util.Log;

import my.neomer.sixtyseconds.core.Pipeline;
import my.neomer.sixtyseconds.states.IState;
import my.neomer.sixtyseconds.states.IStateFinishListener;

public abstract class BaseGameMode implements IGameMode, IStateFinishListener {

    private static final String LOG_TAG = "BaseGameMode";
    private BaseGameContext gameContext;
    private Pipeline<IState> statePipeline;
    private boolean finish = false;

    /**
     * Новая игра.
     */
    public BaseGameMode(@NonNull BaseGameContext gameContext, @NonNull Pipeline<IState> statePipeline) {
        this.statePipeline = statePipeline;
        this.gameContext = gameContext;
    }

    @Override
    public BaseGameContext getGameContext() {
        return gameContext;
    }

    @Override
    public IState getCurrentState() {
        return !statePipeline.isEnd() ? statePipeline.current() : null;
    }

    @Override
    public void onFinish(IState state) {
        if (!statePipeline.isEnd()) {
            IState nextState = statePipeline.next();
            if (nextState != null) {
                runState(nextState);
            }
        }
    }

    @Override
    public void run() {
        IState initialState = getCurrentState();
        if (initialState != null) {
            if (initialState.isOnPause()) {
                initialState.proceed();
            } else {
                runState(initialState);
            }
        }
    }

    @Override
    public void finish() {
        IState state = getCurrentState();
        finish = true;
        if (state != null) {
            state.finish();
        }
    }

    private void runState(@NonNull final IState state) {
        if (!finish) {
            final IStateFinishListener stateFinishListener = this;
            state.prepareState(getGameContext(), stateFinishListener);
            state.start();
        }
    }


    //region Parcelable

    protected BaseGameMode(Parcel in) {
        gameContext = (BaseGameContext) in.readParcelable(BaseGameContext.class.getClassLoader());
        Class<?> pipelineClass = (Class<?>) in.readSerializable();
        if (pipelineClass != null) {
            statePipeline = in.readParcelable(pipelineClass.getClassLoader());
        } else {
            Log.e(LOG_TAG, "Class de-serialization failed!");
        }
    }

    protected BaseGameMode() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(gameContext, 0);
        dest.writeSerializable(statePipeline.getClass());
        dest.writeParcelable(statePipeline, 0);
    }

    //endregion

}
