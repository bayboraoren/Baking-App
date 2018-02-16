package com.iskae.bakingtime.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.iskae.bakingtime.data.model.Ingredient;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by iskae on 13.02.18.
 */
@Dao
public interface IngredientDao {
  @Query("SELECT * FROM Ingredient WHERE Ingredient.recipe_id = :recipeId ORDER BY Ingredient.recipe_id")
  Flowable<List<Ingredient>> getIngredientsByRecipeId(long recipeId);

  @Query("DELETE FROM Ingredient")
  void deleteIngredients();

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertIngredient(Ingredient ingredient);
}
