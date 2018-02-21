package com.iskae.bakingtime.data.model;

import com.google.gson.annotations.SerializedName;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by iskae on 08.02.18.
 */
@Entity
public class Step {
  @PrimaryKey
  @SerializedName("id")
  private long id;
  @SerializedName("shortDescription")
  private String shortDescription;
  @SerializedName("description")
  private String description;
  @SerializedName("videoURL")
  private String videoUrl;
  @SerializedName("thumbnailURL")
  private String thumbnailUrl;

  public Step() {
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getShortDescription() {
    return shortDescription;
  }

  public void setShortDescription(String shortDescription) {
    this.shortDescription = shortDescription;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getVideoUrl() {
    return videoUrl;
  }

  public void setVideoUrl(String videoUrl) {
    this.videoUrl = videoUrl;
  }

  public String getThumbnailUrl() {
    return thumbnailUrl;
  }

  public void setThumbnailUrl(String thumbnailUrl) {
    this.thumbnailUrl = thumbnailUrl;
  }

}
