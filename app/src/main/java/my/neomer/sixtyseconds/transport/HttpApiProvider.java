package my.neomer.sixtyseconds.transport;

import android.support.annotation.NonNull;
import android.util.Log;

import my.neomer.sixtyseconds.MyApp;
import my.neomer.sixtyseconds.dao.AnswerDTO;
import my.neomer.sixtyseconds.dao.QuestionDTO;
import my.neomer.sixtyseconds.dao.UserRatingDTO;
import my.neomer.sixtyseconds.helpers.DifficultyHelper;
import my.neomer.sixtyseconds.model.Answer;
import my.neomer.sixtyseconds.model.Question;
import my.neomer.sixtyseconds.model.UserRating;
import retrofit2.Call;
import retrofit2.Response;

public class HttpApiProvider
        implements IApiProvider, retrofit2.Callback<QuestionDTO>  {

    private static final String TAG = "HttpApiProvider";
    private Callback<Question> callbackQuestion;
    private Callback<Answer> callbackAnswer;
    private TransportConfiguration configuration;

    public HttpApiProvider() {

    }

    @Override
    public void setConfiguration(TransportConfiguration config) {
        configuration = config;
    }

    @Override
    public void getNextQuestion(Question.Difficulty difficulty, Callback<Question> callback) {
        this.callbackQuestion = callback;

        RetrofitService.getInstance()
                .getApi()
                .getQuestion(configuration.getUser(), MyApp.Version, DifficultyHelper.ToInt(difficulty))
                .enqueue(this);
    }

    @Override
    public void getAnswer(Question question, String guess, Callback<Answer> callback) {
        this.callbackAnswer = callback;
        RetrofitService.getInstance()
                .getApi()
                .getAnswer(configuration.getUser(), question.getId(), guess)
                .enqueue(new retrofit2.Callback<AnswerDTO>() {
                    @Override
                    public void onResponse(@NonNull Call<AnswerDTO> call, @NonNull Response<AnswerDTO> response) {
                        callbackAnswer.onReady(response.body() != null ? response.body().toAnswer() : null);
                    }

                    @Override
                    public void onFailure(@NonNull Call<AnswerDTO> call, @NonNull Throwable t) {
                        callbackAnswer.onFailure(t);
                    }
                });

    }

    @Override
    public void like(Question question) {
        RetrofitService.getInstance()
                .getApi()
                .vote(configuration.getUser(), question.getId(), 1)
                .enqueue(new retrofit2.Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        Log.d(TAG, "Question liked");
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        Log.d(TAG, "Execution failed");
                    }
                });
    }

    @Override
    public void dislike(Question question) {
        RetrofitService.getInstance()
                .getApi()
                .vote(configuration.getUser(), question.getId(), -1)
                .enqueue(new retrofit2.Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        Log.d(TAG, "Question liked");
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        Log.d(TAG, "Execution failed");
                    }
                });
    }

    @Override
    public void loadUserRating(final Callback<UserRating> callback) {
        RetrofitService.getInstance()
                .getApi()
                .getUserRating(configuration.getUser())
                .enqueue(new retrofit2.Callback<UserRatingDTO>() {
                    @Override
                    public void onResponse(@NonNull Call<UserRatingDTO> call, @NonNull Response<UserRatingDTO> response) {
                        callback.onReady(response.body() != null ? response.body().toUserRating() : null);
                    }

                    @Override
                    public void onFailure(@NonNull Call<UserRatingDTO> call, @NonNull Throwable t) {
                        callback.onFailure(t);
                    }
                });

    }

    @Override
    public void onResponse(@NonNull Call<QuestionDTO> call, @NonNull Response<QuestionDTO> response) {
        this.callbackQuestion.onReady(response.body() != null ? response.body().toQuestion() : null);
    }

    @Override
    public void onFailure(@NonNull Call<QuestionDTO> call, @NonNull Throwable t) {
        this.callbackQuestion.onFailure(t);
    }

}
