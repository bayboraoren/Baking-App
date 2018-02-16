package com.iskae.bakingtime.data.source.remote;

import com.iskae.bakingtime.data.model.Recipe;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.GET;

/**
 * Created by iskae on 08.02.18.
 */

public interface BakingTimeService {
  @GET("android-baking-app-json")
  Flowable<List<Recipe>> getRecipes();
}
