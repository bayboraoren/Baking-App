package com.iskae.bakingtime.data.db;

import android.arch.lifecycle.*;
import android.arch.persistence.room.*;

import com.iskae.bakingtime.data.model.*;

import java.util.*;

/**
 * Created by iskae on 13.02.18.
 */
@Dao
public interface IngredientDao {
    @Query("SELECT * FROM Ingredient WHERE Ingredient.recipe_id = :recipeId ORDER BY Ingredient.id")
    LiveData<List<Ingredient>> getIngredientsByRecipeId(long recipeId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertIngredients(List<Ingredient> ingredients);
}
