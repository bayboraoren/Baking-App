package com.iskae.bakingtime.util;

import com.iskae.bakingtime.data.RecipesRepository;
import com.iskae.bakingtime.data.model.Recipe;

import javax.inject.Inject;

import io.reactivex.Flowable;

/**
 * Created by iskae on 21.02.18.
 */

public class WidgetUtils {
  private final PreferenceUtils preferenceUtils;
  private final RecipesRepository recipesRepository;

  @Inject
  public WidgetUtils(PreferenceUtils preferenceUtils, RecipesRepository recipesRepository) {
    this.preferenceUtils = preferenceUtils;
    this.recipesRepository = recipesRepository;
  }

  public Flowable<Recipe> getRecipeById(long recipeId) {
    return recipesRepository.getRecipeById(recipeId);
  }

  public void setAppWidgetRecipeId(int appWidgetId, long recipeId) {
    preferenceUtils.setAppWidgetRecipeId(appWidgetId, recipeId);
  }

  public long getAppWidgetRecipeId(int appWidgetId) {
    return preferenceUtils.getAppWidgetRecipeId(appWidgetId);
  }

  public void removeAppWidget(int appWidgetId) {
    preferenceUtils.removeAppWidget(appWidgetId);
  }

}
