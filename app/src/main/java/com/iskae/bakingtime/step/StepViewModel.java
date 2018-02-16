package com.iskae.bakingtime.step;

import android.arch.lifecycle.ViewModel;

import com.iskae.bakingtime.data.RecipesRepository;

/**
 * Created by iskae on 15.02.18.
 */

public class StepViewModel extends ViewModel {
  private final RecipesRepository recipesRepository;

  public StepViewModel(RecipesRepository recipesRepository) {
    this.recipesRepository = recipesRepository;
  }
}
