package com.iskae.bakingtime.data.model;

import com.google.common.base.Objects;
import com.google.gson.annotations.SerializedName;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

/**
 * Created by iskae on 08.02.18.
 */
@Entity(indices = {@Index("id")})
public class Recipe {
  @PrimaryKey
  @SerializedName("id")
  private long id;
  @SerializedName("name")
  private String name;
  @SerializedName("ingredients")
  private List<Ingredient> ingredients;
  @SerializedName("steps")
  private List<Step> steps;
  @SerializedName("servings")
  private int servings;
  @SerializedName("image")
  private String imagePath;

  public Recipe() {
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Ingredient> getIngredients() {
    return ingredients;
  }

  public void setIngredients(List<Ingredient> ingredients) {
    this.ingredients = ingredients;
  }

  public List<Step> getSteps() {
    return steps;
  }

  public void setSteps(List<Step> steps) {
    this.steps = steps;
  }

  public int getServings() {
    return servings;
  }

  public void setServings(int servings) {
    this.servings = servings;
  }

  public String getImagePath() {
    return imagePath;
  }

  public void setImagePath(String imagePath) {
    this.imagePath = imagePath;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Recipe recipe = (Recipe) o;
    return Objects.equal(id, recipe.id) &&
        Objects.equal(name, recipe.name);
  }

  @Override
  public int hashCode() {
    return com.google.common.base.Objects.hashCode(id, name);
  }

}
