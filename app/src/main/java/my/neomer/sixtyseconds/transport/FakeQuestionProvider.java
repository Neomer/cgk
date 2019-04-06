package my.neomer.sixtyseconds.transport;

import my.neomer.sixtyseconds.model.Question;

public class FakeQuestionProvider implements IQuestionProvider {

    public FakeQuestionProvider() {
    }

    @Override
    public void setConfiguration(TransportConfiguration config) {

    }

    @Override
    public void getNextQuestion(Callback<Question> callback) {

    }

    @Override
    public void like(Question question) {

    }

    @Override
    public void dislike(Question question) {

    }

}
