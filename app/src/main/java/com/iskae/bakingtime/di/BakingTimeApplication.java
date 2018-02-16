package com.iskae.bakingtime.di;

import android.app.Application;

/**
 * Created by iskae on 15.02.18.
 */

public class BakingTimeApplication extends Application {
  private ApplicationComponent applicationComponent;

  @Override
  public void onCreate() {
    super.onCreate();

    applicationComponent = DaggerApplicationComponent
        .builder()
        .applicationModule(new ApplicationModule(this))
        .repositoryModule(new RepositoryModule(this))
        .build();

  }

  public ApplicationComponent getApplicationComponent() {
    return applicationComponent;
  }
}
