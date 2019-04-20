package my.neomer.sixtyseconds.states;

import android.os.Parcelable;

import java.io.Serializable;

import my.neomer.sixtyseconds.gamemodes.BaseGameContext;
import my.neomer.sixtyseconds.gamemodes.IGameMode;

/**
 * Состояние игры.
 */
public interface IState extends Parcelable {

    /**
     * Подготовить контекст для перехода в данное состояние
     */
    void prepareState(BaseGameContext gameContext, IStateFinishListener stateFinishListener);

    /**
     * Начало
     */
    void start();

    /**
     * Пауза
     */
    void pause();

    /**
     * Продолжить выполнение
     */
    void proceed();

    /**
     * Конец работы состояния, необходимо перейти в следующее
     */
    void finish();

}
