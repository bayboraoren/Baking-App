package com.iskae.bakingtime.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.iskae.bakingtime.R;
import com.iskae.bakingtime.data.model.Recipe;
import com.iskae.bakingtime.util.IngredientUtils;
import com.iskae.bakingtime.util.WidgetUtils;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidgetProvider extends AppWidgetProvider {

  @Inject
  WidgetUtils widgetUtils;

  @Override
  public void onReceive(Context context, Intent intent) {
    AndroidInjection.inject(this, context);
    super.onReceive(context, intent);
  }

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    for (int appWidgetId : appWidgetIds) {
      long recipeId = widgetUtils.getAppWidgetRecipeId(appWidgetId);
      widgetUtils.getRecipeById(recipeId)
          .take(1)
          .subscribe(recipe -> updateAppWidget(context, appWidgetManager, appWidgetId, recipe));
    }
  }

  @Override
  public void onDeleted(Context context, int[] appWidgetIds) {
    super.onDeleted(context, appWidgetIds);
    for (int appWidgetId : appWidgetIds) {
      widgetUtils.removeAppWidget(appWidgetId);
    }
  }

  public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                     int appWidgetId, Recipe recipe) {
    if (recipe != null) {
      RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_ingredients_list);
      views.setTextViewText(R.id.recipeNameTextView, recipe.getName());
      String ingredientsText = IngredientUtils.getIngredientsAsText(recipe.getIngredients());
      if (ingredientsText != null) {
        views.setTextViewText(R.id.ingredientsTextView, ingredientsText);
      } else {
        views.setTextViewText(R.id.ingredientsTextView, context.getString(R.string.no_ingredients_found));
      }
      appWidgetManager.updateAppWidget(appWidgetId, views);
    }
  }
}

