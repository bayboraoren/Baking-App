package com.iskae.bakingtime.details;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iskae.bakingtime.R;
import com.iskae.bakingtime.data.model.GlideApp;
import com.iskae.bakingtime.data.model.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by iskae on 08.02.18.
 */

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.ViewHolder> {

  private Context context;
  private List<Step> steps;

  public RecipeStepsAdapter(Context context, List<Step> steps) {
    this.context = context;
    this.steps = steps;
  }

  public void setStepsList(List<Step> steps) {
    this.steps = steps;
    notifyDataSetChanged();
  }

  @Override
  public RecipeStepsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.recipe_step_list_item, parent, false);
    return new RecipeStepsAdapter.ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(RecipeStepsAdapter.ViewHolder holder, final int position) {
    final Step step = steps.get(position);
    if (step != null) {
      Drawable playCircle = context.getResources().getDrawable(R.drawable.play_circle, context.getTheme());
      DrawableCompat.setTint(playCircle, context.getColor(R.color.colorPrimary));
      GlideApp.with(context).load(step.getThumbnailUrl())
          .fallback(playCircle)
          .error(playCircle)
          .placeholder(playCircle)
          .into(holder.thumbnailImage);
      holder.stepShortDescriptionView.setText(context.getString(R.string.short_description_text, position + 1, step.getShortDescription()));
      holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        }
      });
    }
  }

  @Override
  public int getItemCount() {
    if (steps == null) return 0;
    return steps.size();
  }


  public class ViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.thumbnailImage)
    ImageView thumbnailImage;
    @BindView(R.id.stepShortDescriptionView)
    TextView stepShortDescriptionView;

    public ViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }
}
