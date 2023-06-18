package com.paulribe.memowords.restclient;

import androidx.test.espresso.IdlingRegistry;

import com.jakewharton.espresso.OkHttp3IdlingResource;

import okhttp3.OkHttpClient;

public abstract class IdlingResources
{
    public static void registerOkHttp(OkHttpClient client)
    {
        IdlingRegistry.getInstance().register(OkHttp3IdlingResource.create("okhttp", client));
    }
}