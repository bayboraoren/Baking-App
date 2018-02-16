package com.iskae.bakingtime.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.iskae.bakingtime.data.model.Recipe;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by iskae on 13.02.18.
 */
@Dao
public interface RecipeDao {
  @Query("SELECT * FROM Recipe")
  Flowable<List<Recipe>> getAllRecipes();

  @Query("SELECT * FROM Recipe WHERE id = :recipeId")
  Flowable<Recipe> getRecipeById(long recipeId);

  @Query("DELETE FROM Recipe")
  void deleteRecipes();

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertRecipes(Recipe recipe);

}
