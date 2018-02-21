package com.iskae.bakingtime.data.source.local;

import com.iskae.bakingtime.data.model.Recipe;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;

/**
 * Created by iskae on 13.02.18.
 */

public class LocalRecipesRepository {

  private final RecipeDao recipeDao;

  @Inject
  public LocalRecipesRepository(RecipeDao recipeDao) {
    this.recipeDao = recipeDao;
  }

  public Flowable<List<Recipe>> getAllRecipes() {
    return recipeDao.getAllRecipes();
  }

  public void insertRecipe(Recipe recipe) {
    Runnable runnable = () -> recipeDao.insertRecipes(recipe);
    runnable.run();
  }

  public Flowable<Recipe> getRecipeById(long recipeId) {
    return recipeDao.getRecipeById(recipeId);
  }
}
