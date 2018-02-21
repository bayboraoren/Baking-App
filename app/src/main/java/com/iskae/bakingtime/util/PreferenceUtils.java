package com.iskae.bakingtime.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.iskae.bakingtime.R;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by iskae on 16.02.18.
 */
@Singleton
public class PreferenceUtils {
  private final static long SYNC_INTERVAL_MINUTES = 3600 * 3600 * 3;

  private SharedPreferences sharedPreferences;
  private Context context;

  @Inject
  public PreferenceUtils(Context context) {
    this.context = context;
    this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
  }

  public boolean shouldUpdateRepository() {
    long lastUpdateTime = sharedPreferences.getLong(context.getString(R.string.last_sync_time_key), 0);
    long difference = System.currentTimeMillis() - lastUpdateTime;
    return difference > SYNC_INTERVAL_MINUTES;
  }

  public void setLastSyncTime(long time) {
    sharedPreferences.edit().putLong(context.getString(R.string.last_sync_time_key), time).apply();
  }

  void setAppWidgetRecipeId(int appWidgetId, long recipeId) {
    sharedPreferences.edit().putLong(String.valueOf(appWidgetId), recipeId).apply();
  }

  long getAppWidgetRecipeId(int appWidgetId) {
    return sharedPreferences.getLong(String.valueOf(appWidgetId), -1);
  }

  void removeAppWidget(int appWidgetId) {
    sharedPreferences.edit().remove(String.valueOf(appWidgetId)).apply();
  }
}
