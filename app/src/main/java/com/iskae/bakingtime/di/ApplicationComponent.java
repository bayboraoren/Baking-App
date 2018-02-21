package com.iskae.bakingtime.di;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by iskae on 15.02.18.
 */
@Singleton
@Component(modules = {ApplicationModule.class, RepositoryModule.class,
    FragmentModule.class, ActivitiesModule.class, WidgetModule.class})
public interface ApplicationComponent {
  void inject(BakingTimeApplication bakingTimeApplication);
}
