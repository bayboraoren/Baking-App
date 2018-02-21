package com.iskae.bakingtime.util;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.iskae.bakingtime.data.RecipesRepository;
import com.iskae.bakingtime.viewmodel.RecipeListViewModel;
import com.iskae.bakingtime.viewmodel.SharedRecipeViewModel;

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

    else if (modelClass.isAssignableFrom(SharedRecipeViewModel.class))
      return (T) new SharedRecipeViewModel(recipesRepository);

    else {
      throw new IllegalArgumentException("ViewModel Not Found");
    }
  }
}
