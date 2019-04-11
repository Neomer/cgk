package my.neomer.sixtyseconds.transport;

import my.neomer.sixtyseconds.model.Answer;
import my.neomer.sixtyseconds.model.Question;

public interface IQuestionProvider {

    void setConfiguration(TransportConfiguration config);

    void getNextQuestion(Callback<Question> callback);

    void getAnswer(Question question, String guess, Callback<Answer> callback);

    void like(Question question);

    void dislike(Question question);
}
