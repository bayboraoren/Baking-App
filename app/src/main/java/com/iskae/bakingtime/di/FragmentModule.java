package com.iskae.bakingtime.di;

import com.iskae.bakingtime.details.DetailsFragment;
import com.iskae.bakingtime.list.RecipeListFragment;
import com.iskae.bakingtime.step.StepFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by iskae on 21.02.18.
 */

@Module
abstract class FragmentModule {

  @ContributesAndroidInjector
  abstract RecipeListFragment contributesRecipeListFragment();

  @ContributesAndroidInjector
  abstract DetailsFragment contributesDetailsFragment();

  @ContributesAndroidInjector
  abstract StepFragment contributesStepFragment();

}
