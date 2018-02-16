package com.iskae.bakingtime.di;

import android.app.Application;

import com.iskae.bakingtime.details.DetailsFragment;
import com.iskae.bakingtime.list.RecipeListFragment;
import com.iskae.bakingtime.step.StepFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by iskae on 15.02.18.
 */
@Singleton
@Component(modules = {ApplicationModule.class, RepositoryModule.class})
public interface ApplicationComponent {

  void inject(RecipeListFragment recipeListFragment);

  void inject(DetailsFragment detailsFragment);

  void inject(StepFragment stepFragment);

  Application application();
}
