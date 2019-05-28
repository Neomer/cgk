package my.neomer.sixtyseconds.transport;

import my.neomer.sixtyseconds.model.Answer;
import my.neomer.sixtyseconds.model.Question;
import my.neomer.sixtyseconds.model.UserRating;

public interface IApiProvider {

    void setConfiguration(TransportConfiguration config);

    void getNextQuestion(Question.Difficulty difficulty, int gameModeId, Callback<Question> callback);

    void getAnswer(Question question, String guess, Callback<Answer> callback);

    void like(Question question);

    void dislike(Question question);

    void loadUserRating(Callback<UserRating> callback);
}
