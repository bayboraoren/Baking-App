package com.iskae.bakingtime.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.iskae.bakingtime.data.RecipesRepository;
import com.iskae.bakingtime.data.model.Recipe;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by iskae on 15.02.18.
 */

public class SharedRecipeViewModel extends ViewModel {
  private final RecipesRepository recipesRepository;

  private final CompositeDisposable disposables = new CompositeDisposable();
  private final MutableLiveData<Recipe> recipe = new MutableLiveData<>();
  private final MutableLiveData<Integer> currentStep = new MutableLiveData<>();
  private final MutableLiveData<Throwable> error = new MutableLiveData<>();

  public SharedRecipeViewModel(RecipesRepository recipesRepository) {
    this.recipesRepository = recipesRepository;
  }

  @Override
  protected void onCleared() {
    disposables.clear();
  }

  public LiveData<Integer> getCurrentStep() {
    return currentStep;
  }

  public LiveData<Recipe> getRecipe() {
    return recipe;
  }

  public void setCurrentStep(int stepId) {
    currentStep.setValue(stepId);
  }

  public LiveData<Throwable> getError() {
    return error;
  }

  public void loadRecipeById(long recipeId) {
    disposables.add(recipesRepository.getRecipeById(recipeId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this.recipe::setValue, error::setValue));
  }
}
