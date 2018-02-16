package com.iskae.bakingtime.data.model;

import com.google.gson.annotations.SerializedName;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by iskae on 08.02.18.
 */

@Entity(foreignKeys = {@ForeignKey(entity = Recipe.class,
    parentColumns = "id",
    childColumns = "recipe_id")}, indices = {@Index("recipe_id")})
public class Ingredient {
  @PrimaryKey(autoGenerate = true)
  private long id;
  @ColumnInfo(name = "recipe_id")
  private long recipeId;
  @SerializedName("quantity")
  private double quantity;
  @SerializedName("measure")
  private String measure;
  @SerializedName("ingredient")
  private String ingredient;

  public Ingredient() {
  }

  public double getQuantity() {
    return quantity;
  }

  public void setQuantity(double quantity) {
    this.quantity = quantity;
  }

  public String getMeasure() {
    return measure;
  }

  public void setMeasure(String measure) {
    this.measure = measure;
  }

  public String getIngredient() {
    return ingredient;
  }

  public void setIngredient(String ingredient) {
    this.ingredient = ingredient;
  }

  public long getRecipeId() {
    return recipeId;
  }

  public void setRecipeId(long recipeId) {
    this.recipeId = recipeId;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }
}
