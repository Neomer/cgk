package my.neomer.sixtyseconds.transport;

import android.support.annotation.NonNull;

import my.neomer.sixtyseconds.dao.QuestionDTO;
import my.neomer.sixtyseconds.model.Question;
import retrofit2.Call;
import retrofit2.Response;

public class HttpQuestionProvider
        implements IQuestionProvider, retrofit2.Callback<QuestionDTO> {

    private Callback<Question> callback;
    private TransportConfiguration configuration;

    public HttpQuestionProvider() {

    }

    @Override
    public void setConfiguration(TransportConfiguration config) {
        configuration = config;
    }

    @Override
    public void getNextQuestion(Callback<Question> callback) {
        this.callback = callback;
        RetrofitService.getInstance()
                .getApi()
                .getQuestion(configuration.getUser())
                .enqueue(this);
    }

    @Override
    public void onResponse(@NonNull Call<QuestionDTO> call, @NonNull Response<QuestionDTO> response) {
        this.callback.onReady(response.body() != null ? response.body().toQuestion() : null);
    }

    @Override
    public void onFailure(@NonNull Call<QuestionDTO> call, @NonNull Throwable t) {
        this.callback.onFailure(t);
    }
}
