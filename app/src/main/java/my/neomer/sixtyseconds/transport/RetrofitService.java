package my.neomer.sixtyseconds.transport;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class RetrofitService {
    private static final RetrofitService ourInstance = new RetrofitService();
    private static final String BASE_URL = "http://neomer.info:47561/AskMe";

    private AskMeAPI api;

    static RetrofitService getInstance() {
        return ourInstance;
    }

    private RetrofitService() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        api = retrofit.create(AskMeAPI.class);
    }

    public AskMeAPI getApi() {
        return api;
    }
}
