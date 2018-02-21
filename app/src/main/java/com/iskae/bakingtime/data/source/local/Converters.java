package com.iskae.bakingtime.data.source.local;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.arch.persistence.room.TypeConverter;

import com.iskae.bakingtime.data.model.Ingredient;
import com.iskae.bakingtime.data.model.Step;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

/**
 * Created by iskae on 16.02.18.
 */

public class Converters {
  private static Gson gson = new Gson();

  @TypeConverter
  static List<Ingredient> stringToIngredientList(String data) {
    if (data == null) {
      return Collections.emptyList();
    }
    Type listType = new TypeToken<List<Ingredient>>() {
    }.getType();

    return gson.fromJson(data, listType);
  }

  @TypeConverter
  static String ingredientListToString(List<Ingredient> ingredients) {
    return gson.toJson(ingredients);
  }

  @TypeConverter
  static List<Step> stringToStepList(String data) {
    if (data == null) {
      return Collections.emptyList();
    }
    Type listType = new TypeToken<List<Step>>() {
    }.getType();

    return gson.fromJson(data, listType);
  }

  @TypeConverter
  static String stepListToString(List<Step> steps) {
    return gson.toJson(steps);
  }
}
