package com.iskae.bakingtime.util;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.iskae.bakingtime.data.RecipesRepository;
import com.iskae.bakingtime.details.DetailsViewModel;
import com.iskae.bakingtime.list.RecipeListViewModel;
import com.iskae.bakingtime.step.StepViewModel;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by iskae on 14.02.18.
 */

@Singleton
public class ViewModelFactory implements ViewModelProvider.Factory {
  private final RecipesRepository recipesRepository;

  @Inject
  public ViewModelFactory(RecipesRepository recipesRepository) {
    this.recipesRepository = recipesRepository;
  }


  @Override
  public <T extends ViewModel> T create(Class<T> modelClass) {
    if (modelClass.isAssignableFrom(RecipeListViewModel.class))
      return (T) new RecipeListViewModel(recipesRepository);

    else if (modelClass.isAssignableFrom(DetailsViewModel.class))
      return (T) new DetailsViewModel(recipesRepository);

    else if (modelClass.isAssignableFrom(StepViewModel.class))
      return (T) new StepViewModel(recipesRepository);

    else {
      throw new IllegalArgumentException("ViewModel Not Found");
    }
  }
}
