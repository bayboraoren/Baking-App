package com.iskae.bakingtime.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.iskae.bakingtime.data.model.Step;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by iskae on 13.02.18.
 */

@Dao
public interface StepDao {
  @Query("SELECT * FROM Step WHERE Step.recipe_id = :recipeId ORDER BY Step.id")
  Flowable<List<Step>> getStepsByRecipeId(long recipeId);

  @Query("DELETE FROM Step")
  void deleteSteps();

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertStep(Step step);
}
