package my.neomer.sixtyseconds.transport;

import my.neomer.sixtyseconds.model.Question;

public class FakeQuestionProvider implements IQuestionProvider {

    public FakeQuestionProvider() {
    }

    @Override
    public void getNextQuestion(Callback<Question> callback) {
        callback.onReady(new Question((int)(Math.random() * 100000), "Пример вопроса", "Какой-то ответ"));
    }
}
