package com.iskae.bakingtime.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.iskae.bakingtime.data.RecipesRepository;
import com.iskae.bakingtime.data.model.Recipe;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by iskae on 14.02.18.
 */

public class RecipeListViewModel extends ViewModel {
  private final RecipesRepository recipesRepository;

  private final CompositeDisposable disposables = new CompositeDisposable();
  private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
  private final MutableLiveData<List<Recipe>> recipesList = new MutableLiveData<>();
  private final MutableLiveData<Throwable> error = new MutableLiveData<>();

  public RecipeListViewModel(RecipesRepository recipesRepository) {
    this.recipesRepository = recipesRepository;
  }

  @Override
  protected void onCleared() {
    disposables.clear();
  }

  public LiveData<Boolean> getLoadingStatus() {
    return isLoading;
  }

  public LiveData<List<Recipe>> getRecipesList() {
    return recipesList;
  }

  public LiveData<Throwable> getError() {
    return error;
  }

  public void loadRecipesList() {
    disposables.add(recipesRepository.getAllRecipes()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(s -> isLoading.setValue(true))
        .doAfterTerminate(() -> isLoading.setValue(false))
        .subscribe(
            recipesList::setValue,
            error::setValue
        )
    );
  }

  public void refreshRecipes() {
    recipesRepository.refreshRepository();
    loadRecipesList();
  }
}
