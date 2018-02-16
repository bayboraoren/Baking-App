package com.iskae.bakingtime.data.source.remote;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by iskae on 08.02.18.
 */

public class BakingTimeClient {
  private static final String BAKING_TIME_BASE_URL = "http://go.udacity.com/";

  private static Retrofit client = null;

  public static Retrofit getBakingTimeClient() {
    if (client == null) {
      client = new Retrofit.Builder()
          .baseUrl(BAKING_TIME_BASE_URL)
          .addConverterFactory(GsonConverterFactory.create())
          .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
          .build();
    }
    return client;
  }
}
