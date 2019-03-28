package my.neomer.sixtyseconds;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import my.neomer.sixtyseconds.model.Question;
import my.neomer.sixtyseconds.transport.Callback;
import my.neomer.sixtyseconds.transport.FakeQuestionProvider;
import my.neomer.sixtyseconds.transport.IQuestionProvider;

public class QuestionFragmentViewModel extends ViewModel {

    private IQuestionProvider provider = new FakeQuestionProvider();
    private final MutableLiveData<Question> question = new MutableLiveData<>();

    public LiveData<Question> getQuestion() {
        return question;
    }

    public void update() {
        provider.getNextQuestion(new Callback<Question>() {
            @Override
            public void onReady(Question data) {
                question.setValue(data);
            }
        });
    }

}
