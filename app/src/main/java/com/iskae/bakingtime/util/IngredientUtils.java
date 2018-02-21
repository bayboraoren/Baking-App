package com.iskae.bakingtime.util;

import com.iskae.bakingtime.data.model.Ingredient;

import java.util.List;

/**
 * Created by iskae on 21.02.18.
 */

public class IngredientUtils {

  public static String getIngredientsAsText(List<Ingredient> ingredients) {
    if (ingredients != null && ingredients.size() > 0) {
      StringBuilder builder = new StringBuilder();
      for (int i = 0; i < ingredients.size(); i++) {
        Ingredient ingredient = ingredients.get(i);
        builder.append("* ");
        builder.append(ingredient.getIngredient() + " ");
        builder.append(String.valueOf(ingredient.getQuantity()) + " ");
        builder.append(ingredient.getMeasure() + System.lineSeparator());
      }
      return builder.toString();
    }
    return null;
  }
}
