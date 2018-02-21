package com.iskae.bakingtime.data.model;

import com.google.gson.annotations.SerializedName;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by iskae on 08.02.18.
 */

@Entity
public class Ingredient {
  @PrimaryKey(autoGenerate = true)
  private long id;
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

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

}
