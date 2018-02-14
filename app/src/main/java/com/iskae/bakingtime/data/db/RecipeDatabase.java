package com.iskae.bakingtime.data.db;

import android.arch.persistence.room.*;
import android.content.*;

import com.iskae.bakingtime.data.model.*;

/**
 * Created by iskae on 13.02.18.
 */

@Database(entities = {Recipe.class, Ingredient.class, Step.class}, version = 1)
public abstract class RecipeDatabase extends RoomDatabase {

    private static RecipeDatabase INSTANCE;

    public abstract RecipeDao recipeModel();

    public abstract Ingredient ingredientModel();

    public abstract Step stepModel();

    public static RecipeDatabase getInMemoryDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.inMemoryDatabaseBuilder(context.getApplicationContext(), RecipeDatabase.class)
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}