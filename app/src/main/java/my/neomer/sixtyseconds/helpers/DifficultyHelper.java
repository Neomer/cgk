package my.neomer.sixtyseconds.helpers;

import my.neomer.sixtyseconds.model.Question;

public final class DifficultyHelper {

    public static int ToInt(Question.Difficulty difficulty) {
        if (difficulty == null) {
            return -1;
        }
        switch (difficulty) {
            case Easiest: return 0;
            case Normal: return 1;
            case Moderate: return 2;
            case Professional: return 3;
            case Hardest: return 4;
            default: return -1;
        }
    }

    public static Question.Difficulty FromInt(int value) {
        switch (value) {
            case 0: return Question.Difficulty.Easiest;
            case 1: return Question.Difficulty.Normal;
            case 2: return Question.Difficulty.Moderate;
            case 3: return Question.Difficulty.Professional;
            case 4: return Question.Difficulty.Hardest;
            default: return Question.Difficulty.Unknown;
        }
    }

}
