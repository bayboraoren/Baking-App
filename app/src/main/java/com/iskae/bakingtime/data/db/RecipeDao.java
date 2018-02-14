package com.iskae.bakingtime.data.db;

import android.arch.lifecycle.*;
import android.arch.persistence.room.*;

import com.iskae.bakingtime.data.model.*;

import java.util.*;

/**
 * Created by iskae on 13.02.18.
 */
@Dao
public interface RecipeDao {
    @Query("SELECT * FROM Recipe")
    LiveData<List<Recipe>> getAllRecipes();

    @Query("SELECT * FROM Recipe WHERE id = :recipeId")
    LiveData<Recipe> getRecipeById(long recipeId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertRecipes(List<Recipe> recipes);

}
