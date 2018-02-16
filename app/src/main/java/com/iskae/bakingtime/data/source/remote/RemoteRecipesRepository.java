package com.iskae.bakingtime.data.source.remote;

import com.iskae.bakingtime.data.model.Recipe;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;

/**
 * Created by iskae on 14.02.18.
 */

public class RemoteRecipesRepository {

  private final BakingTimeService service;

  @Inject
  public RemoteRecipesRepository(BakingTimeService service) {
    this.service = service;
  }

  public Flowable<List<Recipe>> getAllRecipes() {
    return service.getRecipes();
  }
}
