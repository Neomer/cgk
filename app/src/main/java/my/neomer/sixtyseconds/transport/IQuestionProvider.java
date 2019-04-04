package my.neomer.sixtyseconds.transport;

import my.neomer.sixtyseconds.model.Question;

public interface IQuestionProvider {

    void setConfiguration(TransportConfiguration config);

    void getNextQuestion(Callback<Question> callback);

}
