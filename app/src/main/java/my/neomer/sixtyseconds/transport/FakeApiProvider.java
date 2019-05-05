package my.neomer.sixtyseconds.transport;

import my.neomer.sixtyseconds.model.Answer;
import my.neomer.sixtyseconds.model.Question;
import my.neomer.sixtyseconds.model.UserRating;

public class FakeApiProvider implements IApiProvider {

    public FakeApiProvider() {
    }

    @Override
    public void setConfiguration(TransportConfiguration config) {

    }

    @Override
    public void getNextQuestion(Question.Difficulty difficulty, Callback<Question> callback) {
        Question question = new Question();
        question.setId(0);
        question.setVote(0);
        question.setDifficulty(difficulty);
        question.setText("Sample question text generated by FakeApiProvider");

        callback.onReady(question);
    }

    @Override
    public void getAnswer(Question question, String guess, Callback<Answer> callback) {
        Answer answer = new Answer();
        answer.setAnswer("Sample answer on the question generated by FakeApiProvider");
        answer.setCorrect(true);
        answer.setComment("Simple comment");

        callback.onReady(answer);
    }

    @Override
    public void like(Question question) {

    }

    @Override
    public void dislike(Question question) {

    }

    @Override
    public void loadUserRating(Callback<UserRating> callback) {
        UserRating userRating = new UserRating();
        userRating.setPoints(-1);
        userRating.setPlace(666);

        callback.onReady(userRating);
    }

}