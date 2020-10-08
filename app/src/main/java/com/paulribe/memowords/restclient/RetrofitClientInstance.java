package com.paulribe.memowords.restclient;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {

    private static Retrofit retrofitPons;
    private static Retrofit retrofitMyMemory;
    private static final String BASE_URL_PONS = "https://api.pons.com";
    private static final String BASE_URL_MY_MEMORY = "https://api.mymemory.translated.net";

    public static Retrofit getRetrofitInstance() {
        if (retrofitPons == null) {
            retrofitPons = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL_PONS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitPons;
    }

    public static Retrofit getRetrofitMyMemoryInstance() {
        if (retrofitMyMemory == null) {
            retrofitMyMemory = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL_MY_MEMORY)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitMyMemory;
    }
}