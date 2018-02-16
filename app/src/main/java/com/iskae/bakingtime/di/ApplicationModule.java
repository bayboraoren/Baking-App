package com.iskae.bakingtime.di;

import android.app.Application;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Created by iskae on 15.02.18.
 */
@Module
public class ApplicationModule {
  private final BakingTimeApplication application;

  public ApplicationModule(BakingTimeApplication application) {
    this.application = application;
  }

  @Provides
  BakingTimeApplication provideBakingTimeApplication() {
    return application;
  }

  @Provides
  Application provideApplication() {
    return application;
  }

  @Provides
  Context provideContext() {
    return application.getApplicationContext();
  }
}
