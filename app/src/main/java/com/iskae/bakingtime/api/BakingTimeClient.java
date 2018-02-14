package com.iskae.bakingtime.api;

import retrofit2.*;
import retrofit2.converter.gson.*;

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
                    .build();
        }
        return client;
    }
}
