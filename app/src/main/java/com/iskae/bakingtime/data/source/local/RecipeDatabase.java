package com.iskae.bakingtime.data.source.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.iskae.bakingtime.data.model.Ingredient;
import com.iskae.bakingtime.data.model.Recipe;
import com.iskae.bakingtime.data.model.Step;

/**
 * Created by iskae on 13.02.18.
 */

@Database(entities = {Recipe.class, Ingredient.class, Step.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class RecipeDatabase extends RoomDatabase {

  private static RecipeDatabase INSTANCE;

  public static RecipeDatabase getInMemoryDatabase(Context context) {
    if (INSTANCE == null) {
      INSTANCE = Room.inMemoryDatabaseBuilder(context.getApplicationContext(), RecipeDatabase.class)
          .build();
    }
    return INSTANCE;
  }

  public static void destroyInstance() {
    INSTANCE = null;
  }

  public abstract RecipeDao recipeDao();

}