package com.iskae.bakingtime.data.source.local;

import com.iskae.bakingtime.data.model.Ingredient;
import com.iskae.bakingtime.data.model.Recipe;
import com.iskae.bakingtime.data.model.Step;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;

/**
 * Created by iskae on 13.02.18.
 */

public class LocalRecipesRepository {

  private final RecipeDao recipeDao;
  private final IngredientDao ingredientDao;
  private final StepDao stepDao;

  @Inject
  public LocalRecipesRepository(RecipeDao recipeDao, IngredientDao ingredientDao, StepDao stepDao) {
    this.recipeDao = recipeDao;
    this.ingredientDao = ingredientDao;
    this.stepDao = stepDao;
  }

  public Flowable<List<Recipe>> getAllRecipes() {
    return recipeDao.getAllRecipes();
  }

  public Flowable<List<Ingredient>> getIngredientsByRecipeId(long recipeId) {
    return ingredientDao.getIngredientsByRecipeId(recipeId);
  }

  public Flowable<List<Step>> getStepsByRecipeId(long recipeId) {
    return stepDao.getStepsByRecipeId(recipeId);
  }

  public void insertRecipe(Recipe recipe) {
    Runnable runnable = () -> recipeDao.insertRecipes(recipe);

    runnable.run();
  }

  public void insertIngredient(Ingredient ingredient) {
    Runnable runnable = () -> ingredientDao.insertIngredient(ingredient);

    runnable.run();
  }

  public void insertStep(Step step) {
    Runnable runnable = () -> stepDao.insertStep(step);

    runnable.run();
  }

  public void deleteDatabase() {
    Runnable runnable = () -> {
      stepDao.deleteSteps();
      ingredientDao.deleteIngredients();
      recipeDao.deleteRecipes();
    };
    runnable.run();
  }

  public Flowable<Recipe> getRecipeById(long recipeId) {
    return recipeDao.getRecipeById(recipeId);
  }
}
