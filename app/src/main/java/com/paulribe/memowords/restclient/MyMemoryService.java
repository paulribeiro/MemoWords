package com.paulribe.memowords.restclient;

import com.paulribe.memowords.model.mymemory.MyMemoryResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MyMemoryService {

    @GET("/get")
    Call<MyMemoryResult> getTranslations(@Query("q") String wordToTranslate, @Query("langpair") String languages);
}
