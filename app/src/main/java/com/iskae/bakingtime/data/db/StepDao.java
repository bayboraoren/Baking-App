package com.iskae.bakingtime.data.db;

import android.arch.lifecycle.*;
import android.arch.persistence.room.*;

import com.iskae.bakingtime.data.model.*;

import java.util.*;

/**
 * Created by iskae on 13.02.18.
 */

@Dao
public interface StepDao {
    @Query("SELECT * FROM Step WHERE Step.recipe_id = :recipeId ORDER BY Step.id")
    LiveData<List<Step>> getStepsByRecipeId(long recipeId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertSteps(List<Step> steps);
}
