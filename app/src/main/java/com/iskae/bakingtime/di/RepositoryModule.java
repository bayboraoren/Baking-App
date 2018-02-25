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
import retrofit2.Retrofit;

/**
 * Created by iskae on 14.02.18.
 */

@Module
public class RepositoryModule {

  @Provides
  @Singleton
  LocalRecipesRepository provideLocalRecipesRepository(RecipeDao recipeDao) {
    return new LocalRecipesRepository(recipeDao);
  }

  @Provides
  @Singleton
  RemoteRecipesRepository provideRemoteRecipesRepository(BakingTimeService service) {
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
  RecipeDatabase provideRecipeDatabase(Application application) {
    return Room.databaseBuilder(application,
        RecipeDatabase.class, "recipe.db").build();
  }

  @Provides
  @Singleton
  BakingTimeService provideBakingTimeService(Retrofit retrofit) {
    return retrofit.create(BakingTimeService.class);
  }

  @Provides
  @Singleton
  Retrofit provideRetrofit() {
    return BakingTimeClient.getBakingTimeClient();
  }

  @Provides
  @Singleton
  ViewModelProvider.Factory provideViewModelFactory(RecipesRepository recipesRepository) {
    return new ViewModelFactory(recipesRepository);
  }
}
