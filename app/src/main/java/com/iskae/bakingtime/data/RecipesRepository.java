package com.iskae.bakingtime.data;

import com.iskae.bakingtime.data.model.Recipe;
import com.iskae.bakingtime.data.source.local.LocalRecipesRepository;
import com.iskae.bakingtime.data.source.remote.RemoteRecipesRepository;
import com.iskae.bakingtime.util.PreferenceUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Flowable;

/**
 * Created by iskae on 15.02.18.
 */

public class RecipesRepository {
  private final LocalRecipesRepository localRecipesRepository;
  private final RemoteRecipesRepository remoteRecipesRepository;
  private final PreferenceUtils preferenceUtils;

  Map<Long, Recipe> cachedRecipes;
  private boolean cacheIsDirty;

  @Inject
  public RecipesRepository(LocalRecipesRepository localRecipesRepository,
                           RemoteRecipesRepository remoteRecipesRepository, PreferenceUtils preferenceUtils) {
    this.localRecipesRepository = localRecipesRepository;
    this.remoteRecipesRepository = remoteRecipesRepository;
    this.preferenceUtils = preferenceUtils;
  }

  public Flowable<List<Recipe>> getAllRecipes() {
    if (cachedRecipes != null && !cacheIsDirty && cachedRecipes.size() > 0) {
      return Flowable.fromIterable(cachedRecipes.values()).toList().toFlowable();
    } else if (cachedRecipes == null) {
      cachedRecipes = new LinkedHashMap<>();
    }
    if (preferenceUtils.shouldUpdateRepository()) {
      return getAndSaveRemoteRecipes();
    }
    return getAndCacheLocalRecipes();
  }

  private Flowable<List<Recipe>> getAndSaveRemoteRecipes() {
    preferenceUtils.setLastSyncTime(System.currentTimeMillis());
    return remoteRecipesRepository.getAllRecipes().flatMap(recipes ->
        Flowable.fromIterable(recipes).doOnNext(recipe -> {
          localRecipesRepository.insertRecipe(recipe);
          cachedRecipes.put(recipe.getId(), recipe);
        }).toList().toFlowable()).doOnComplete(() -> cacheIsDirty = false);
  }

  private Flowable<List<Recipe>> getAndCacheLocalRecipes() {
    return localRecipesRepository.getAllRecipes()
        .flatMap(recipes -> Flowable.fromIterable(recipes)
            .doOnNext(recipe -> cachedRecipes.put(recipe.getId(), recipe))
            .toList()
            .toFlowable());
  }

  public Flowable<List<Recipe>> refreshRepository() {
    return getAndSaveRemoteRecipes();
  }

  public Flowable<Recipe> getRecipeById(long recipeId) {
    return localRecipesRepository.getRecipeById(recipeId);
  }
}
