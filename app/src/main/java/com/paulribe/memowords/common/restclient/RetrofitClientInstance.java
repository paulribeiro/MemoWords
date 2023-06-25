package com.paulribe.memowords.common.restclient;

import com.paulribe.memowords.restclient.IdlingResources;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {

    private static final OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15L, TimeUnit.SECONDS)
            .writeTimeout(15L, TimeUnit.SECONDS);

    private static Retrofit retrofitPons;
    private static Retrofit retrofitMyMemory;
    private static final String BASE_URL_PONS = "https://api.pons.com";
    private static final String BASE_URL_MY_MEMORY = "https://api.mymemory.translated.net";

    private RetrofitClientInstance () {}

    public static Retrofit getRetrofitInstance() {
        if (retrofitPons == null) {
            OkHttpClient httpClient = httpClientBuilder.build();
            IdlingResources.registerOkHttp(httpClient);
            retrofitPons = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL_PONS)
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitPons;
    }

    public static Retrofit getRetrofitMyMemoryInstance() {
        if (retrofitMyMemory == null) {
            OkHttpClient httpClient = httpClientBuilder.build();
            IdlingResources.registerOkHttp(httpClient);
            retrofitMyMemory = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL_MY_MEMORY)
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitMyMemory;
    }
}