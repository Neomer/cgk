package my.neomer.sixtyseconds.transport;

import my.neomer.sixtyseconds.dao.AnswerDTO;
import my.neomer.sixtyseconds.dao.QuestionDTO;
import my.neomer.sixtyseconds.dao.UserRatingDTO;
import my.neomer.sixtyseconds.model.Question;
import my.neomer.sixtyseconds.model.UserRating;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AskMeAPI {

    @GET("question2.php")
    Call<QuestionDTO> getQuestion(@Query("user") String userId, @Query("version") String appVersion, @Query("level") int difficulty, @Query("gamemode") int gameModeId);

    @GET("answer.php")
    Call<AnswerDTO> getAnswer(@Query("user") String userId, @Query("question") long questionId, @Query("answer") String answer);

    @GET("vote.php")
    Call<Void> vote(@Query("user") String userId, @Query("question") long questionId, @Query("vote") int vote);

    @GET("adclick.php")
    Call<Void> adclick(@Query("user") String userId);

    @GET("user_rating.php")
    Call<UserRatingDTO> getUserRating(@Query("user") String userId);
}
