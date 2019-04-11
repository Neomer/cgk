package my.neomer.sixtyseconds;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;
import android.widget.Toast;

import my.neomer.sixtyseconds.model.Answer;
import my.neomer.sixtyseconds.model.Question;
import my.neomer.sixtyseconds.transport.Callback;
import my.neomer.sixtyseconds.transport.FakeQuestionProvider;
import my.neomer.sixtyseconds.transport.HttpQuestionProvider;
import my.neomer.sixtyseconds.transport.IQuestionProvider;

public class QuestionFragmentViewModel extends ViewModel {

    private static final String TAG = "QuestionFragmentVM";
    private IQuestionProvider provider = new HttpQuestionProvider();
    private final MutableLiveData<Question> question = new MutableLiveData<>();
    private final MutableLiveData<Answer> answer = new MutableLiveData<>();

    boolean hasValue() { return question.getValue() != null; }

    public LiveData<Question> getQuestion() {
        return question;
    }

    public LiveData<Answer> getAnswer() { return answer; }

    IQuestionProvider getProvider() { return provider; }

    void like() {
        if (hasValue()) {
            provider.like(question.getValue());
        }
    }

    void dislike() {
        if (hasValue()) {
            provider.dislike(question.getValue());
        }
    }

    void checkAnswer(String guess) {
        provider.getAnswer(question.getValue(), guess, new Callback<Answer>() {
            @Override
            public void onReady(Answer data) {
                answer.setValue(data);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.getLocalizedMessage());
            }
        });
    }

    void update() {
        provider.getNextQuestion(new Callback<Question>() {
            @Override
            public void onReady(Question data) {
                question.setValue(data);
            }

            @Override
            public void onFailure(Throwable t) {
                provider.getNextQuestion(this);
            }
        });
    }
}
