package com.iskae.bakingtime.di;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasBroadcastReceiverInjector;

/**
 * Created by iskae on 15.02.18.
 */

public class BakingTimeApplication extends Application implements HasActivityInjector,
    HasBroadcastReceiverInjector {

  @Inject
  DispatchingAndroidInjector<Activity> activityInjector;

  @Inject
  DispatchingAndroidInjector<BroadcastReceiver> receiverInjector;

  private ApplicationComponent applicationComponent;

  @Override
  public void onCreate() {
    super.onCreate();

    applicationComponent = DaggerApplicationComponent
        .builder()
        .applicationModule(new ApplicationModule(this))
        .repositoryModule(new RepositoryModule())
        .build();

    applicationComponent.inject(this);
  }

  public ApplicationComponent getApplicationComponent() {
    return applicationComponent;
  }

  @Override
  public AndroidInjector<Activity> activityInjector() {
    return activityInjector;
  }

  @Override
  public AndroidInjector<BroadcastReceiver> broadcastReceiverInjector() {
    return receiverInjector;
  }
}
