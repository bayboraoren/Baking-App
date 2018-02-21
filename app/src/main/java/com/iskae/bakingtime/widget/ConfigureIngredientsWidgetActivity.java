package com.iskae.bakingtime.widget;

import android.appwidget.AppWidgetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.iskae.bakingtime.list.RecipeListActivity;

import dagger.android.AndroidInjection;

/**
 * Created by iskae on 12.02.18.
 */

public class ConfigureIngredientsWidgetActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    AndroidInjection.inject(this);
    super.onCreate(savedInstanceState);
    RecipeListActivity.startPicker(this);
  }
}
