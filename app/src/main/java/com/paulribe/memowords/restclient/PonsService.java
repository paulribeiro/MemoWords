package com.paulribe.memowords.restclient;

import com.paulribe.memowords.model.pons.PonsResult;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface PonsService {

    @GET("/v1/dictionary")
    Call<List<PonsResult>> getAllTranslations(@Query("l") String language, @Query("q") String wordToTranslate,
                                              @Query("in") String inputLanguage, @Header("X-Secret") String userkey);
}
