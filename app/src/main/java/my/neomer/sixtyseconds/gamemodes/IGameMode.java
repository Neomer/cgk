package my.neomer.sixtyseconds.gamemodes;

import android.content.Context;
import android.os.Parcelable;

import my.neomer.sixtyseconds.states.IState;

/**
 * Тип игры (одиночная, групповая итд).
 * Реализация паттерна "Состояние"
 */
public interface IGameMode extends Parcelable {

    /**
     * Контекст игры
     */
    BaseGameContext getGameContext();

    /**
     * Текущее состояние игры
     */
    IState getCurrentState();

    /**
     * Начать игру
     */
    void run();
}
