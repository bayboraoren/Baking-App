package com.iskae.bakingtime.di;

import com.iskae.bakingtime.details.DetailsActivity;
import com.iskae.bakingtime.list.RecipeListActivity;
import com.iskae.bakingtime.step.StepActivity;
import com.iskae.bakingtime.widget.ConfigureIngredientsWidgetActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by iskae on 21.02.18.
 */

@Module
abstract class ActivitiesModule {

  @ContributesAndroidInjector
  abstract RecipeListActivity contributesRecipeListActivity();

  @ContributesAndroidInjector
  abstract DetailsActivity contributesDetailsActivity();

  @ContributesAndroidInjector
  abstract StepActivity contributesStepActivity();

  @ContributesAndroidInjector
  abstract ConfigureIngredientsWidgetActivity contributesConfigureIngredientsWidgetActivity();
}
