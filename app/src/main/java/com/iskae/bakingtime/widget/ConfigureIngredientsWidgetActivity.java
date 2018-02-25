package com.iskae.bakingtime.widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.iskae.bakingtime.list.RecipeListActivity;
import com.iskae.bakingtime.util.Constants;
import com.iskae.bakingtime.util.WidgetUtils;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

/**
 * Created by iskae on 12.02.18.
 */

public class ConfigureIngredientsWidgetActivity extends AppCompatActivity {

  @Inject
  WidgetUtils widgetUtils;

  private int appWidgetId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    AndroidInjection.inject(this);
    super.onCreate(savedInstanceState);
    RecipeListActivity.startPicker(this);
    Intent intent = getIntent();
    Bundle extras = intent.getExtras();
    if (extras != null) {
      appWidgetId = extras.getInt(
          AppWidgetManager.EXTRA_APPWIDGET_ID,
          AppWidgetManager.INVALID_APPWIDGET_ID);
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (Constants.REQUEST_CODE_PICKER == requestCode && RESULT_OK == resultCode) {
      long recipeId = data.getLongExtra(Constants.EXTRA_RECIPE_ID, -1);
      if (recipeId != -1) {
        widgetUtils.getRecipeById(recipeId)
            .take(1)
            .subscribe(recipe -> {
              AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
              IngredientsWidgetProvider.updateAppWidget(this, appWidgetManager, appWidgetId, recipe);
              Intent resultValue = new Intent();
              resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
              setResult(RESULT_OK, resultValue);
              finish();
            });
      }
    }
  }
}
