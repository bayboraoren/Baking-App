package com.iskae.bakingtime.api;

import com.iskae.bakingtime.data.model.*;

import java.util.*;

import retrofit2.http.*;

/**
 * Created by iskae on 08.02.18.
 */

public interface BakingTimeService {
    @GET("android-baking-app-json")
    io.reactivex.Observable<List<Recipe>> getRecipes();
}
