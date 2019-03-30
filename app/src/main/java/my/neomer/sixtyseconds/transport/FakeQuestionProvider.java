package my.neomer.sixtyseconds.transport;

import my.neomer.sixtyseconds.model.Question;

public class FakeQuestionProvider implements IQuestionProvider {

    public FakeQuestionProvider() {
    }

    @Override
    public void getNextQuestion(Callback<Question> callback) {

    }
}
