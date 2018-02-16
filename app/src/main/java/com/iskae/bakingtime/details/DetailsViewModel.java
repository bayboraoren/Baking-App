package com.iskae.bakingtime.details;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.iskae.bakingtime.data.RecipesRepository;
import com.iskae.bakingtime.data.model.Ingredient;
import com.iskae.bakingtime.data.model.Step;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by iskae on 15.02.18.
 */

public class DetailsViewModel extends ViewModel {
  private final RecipesRepository recipesRepository;

  private final CompositeDisposable disposables = new CompositeDisposable();
  private final MutableLiveData<List<Step>> stepsList = new MutableLiveData<>();
  private final MutableLiveData<List<Ingredient>> ingredientsList = new MutableLiveData<>();
  private final MutableLiveData<Throwable> error = new MutableLiveData<>();

  public DetailsViewModel(RecipesRepository recipesRepository) {
    this.recipesRepository = recipesRepository;
  }

  @Override
  protected void onCleared() {
    disposables.clear();
  }

  LiveData<List<Ingredient>> getIngredientsList() {
    return ingredientsList;
  }

  LiveData<List<Step>> getStepsList() {
    return stepsList;
  }

  LiveData<Throwable> getError() {
    return error;
  }

  void loadRecipeIngredients(long recipeId) {
    disposables.add(recipesRepository.getIngredientsList(recipeId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(ingredientsList::setValue, error::setValue));
  }

  void loadRecipeSteps(long recipeId) {
    disposables.add(recipesRepository.getStepsList(recipeId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(stepsList::setValue, error::setValue));
  }
}
