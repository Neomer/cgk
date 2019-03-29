package my.neomer.sixtyseconds.transport;

import my.neomer.sixtyseconds.dao.QuestionDTO;
import my.neomer.sixtyseconds.model.Question;
import retrofit2.Call;
import retrofit2.http.GET;

public interface AskMeAPI {

    @GET("question.php")
    Call<QuestionDTO> getQuestion();

}
