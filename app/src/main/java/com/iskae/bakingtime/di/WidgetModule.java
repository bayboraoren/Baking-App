package com.iskae.bakingtime.di;

import com.iskae.bakingtime.widget.IngredientsWidgetProvider;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by iskae on 21.02.18.
 */

@Module
abstract class WidgetModule {

  @ContributesAndroidInjector
  abstract IngredientsWidgetProvider contributesIngredientsWidget();
}
