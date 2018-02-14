package com.iskae.bakingtime.data.local;

import android.arch.lifecycle.*;

import com.iskae.bakingtime.data.db.*;
import com.iskae.bakingtime.data.model.*;

import java.util.*;

import javax.inject.*;

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

    public LiveData<List<Recipe>> getAllRecipes() {
        return recipeDao.getAllRecipes();
    }

    public LiveData<List<Ingredient>> getIngredientsByRecipeId(long recipeId) {
        return ingredientDao.getIngredientsByRecipeId(recipeId);
    }

    public LiveData<List<Step>> getStepsByRecipeId(long recipeId) {
        return stepDao.getStepsByRecipeId(recipeId);
    }

    public long insertRecipes(List<Recipe> recipes) {
        return recipeDao.insertRecipes(recipes);
    }

    public long insertIngredients(List<Ingredient> ingredients) {
        return ingredientDao.insertIngredients(ingredients);
    }

    public long insertSteps(List<Step> steps) {
        return stepDao.insertSteps(steps);
    }

    public LiveData<Recipe> getRecipeById(long recipeId) {
        return recipeDao.getRecipeById(recipeId);
    }
}
