package com.iskae.bakingtime.di;

import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.persistence.room.Room;
import android.content.Context;

import com.iskae.bakingtime.data.RecipesRepository;
import com.iskae.bakingtime.data.source.local.LocalRecipesRepository;
import com.iskae.bakingtime.data.source.local.RecipeDao;
import com.iskae.bakingtime.data.source.local.RecipeDatabase;
import com.iskae.bakingtime.data.source.remote.BakingTimeClient;
import com.iskae.bakingtime.data.source.remote.BakingTimeService;
import com.iskae.bakingtime.data.source.remote.RemoteRecipesRepository;
import com.iskae.bakingtime.util.PreferenceUtils;
import com.iskae.bakingtime.util.ViewModelFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by iskae on 14.02.18.
 */

@Module
public class RepositoryModule {

  private final RecipeDatabase recipeDatabase;
  private final BakingTimeService service;

  public RepositoryModule(Application application) {
    this.recipeDatabase = Room.databaseBuilder(application,
        RecipeDatabase.class, "recipe.db").build();
    this.service = BakingTimeClient.getBakingTimeClient().create(BakingTimeService.class);
  }

  @Provides
  @Singleton
  LocalRecipesRepository provideLocalRecipesRepository(RecipeDao recipeDao) {
    return new LocalRecipesRepository(recipeDao);
  }

  @Provides
  @Singleton
  RemoteRecipesRepository provideRemoteRecipesRepository() {
    return new RemoteRecipesRepository(service);
  }

  @Singleton
  @Provides
  PreferenceUtils providePreferenceUtils(Context context) {
    return new PreferenceUtils(context);
  }

  @Provides
  @Singleton
  RecipesRepository provideRecipesRepository(LocalRecipesRepository localRecipesRepository,
                                             RemoteRecipesRepository remoteRecipesRepository, PreferenceUtils preferenceUtils) {
    return new RecipesRepository(localRecipesRepository, remoteRecipesRepository, preferenceUtils);
  }

  @Provides
  @Singleton
  RecipeDao provideRecipeDao(RecipeDatabase database) {
    return database.recipeDao();
  }

  @Provides
  @Singleton
  RecipeDatabase provideRecipeDatabase() {
    return recipeDatabase;
  }

  @Provides
  @Singleton
  ViewModelProvider.Factory provideViewModelFactory(RecipesRepository recipesRepository) {
    return new ViewModelFactory(recipesRepository);
  }
}
