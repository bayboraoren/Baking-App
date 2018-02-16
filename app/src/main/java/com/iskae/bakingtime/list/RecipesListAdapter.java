package com.iskae.bakingtime.list;

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
import com.iskae.bakingtime.data.model.Recipe;
import com.iskae.bakingtime.details.DetailsActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by iskae on 08.02.18.
 */

public class RecipesListAdapter extends RecyclerView.Adapter<RecipesListAdapter.ViewHolder> {

  private Context context;
  private List<Recipe> recipesList;

  public RecipesListAdapter(Context context, List<Recipe> recipes) {
    this.context = context;
    this.recipesList = recipes;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.recipes_list_item, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, final int position) {
    final Recipe recipe = recipesList.get(position);
    if (recipe != null) {
      Drawable food = context.getResources().getDrawable(R.drawable.cupcake, context.getTheme());
      DrawableCompat.setTint(food, context.getColor(R.color.colorAccent));
      GlideApp.with(context).load(recipe.getImagePath())
          .fallback(food)
          .error(food)
          .placeholder(food)
          .into(holder.recipeImageView);
      holder.recipeServingsView.setText(context.getString(R.string.number_of_servings,
          String.valueOf(recipe.getServings())));
      holder.recipeNameView.setText(recipe.getName());
      holder.itemView.setOnClickListener(view -> {
        DetailsActivity.showRecipeDetails(context, recipe.getId());
      });
    }
  }

  public void setRecipesList(List<Recipe> items) {
    this.recipesList = items;
    notifyDataSetChanged();
  }

  @Override
  public int getItemCount() {
    if (recipesList == null) return 0;
    return recipesList.size();
  }


  public class ViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.recipeImageView)
    ImageView recipeImageView;
    @BindView(R.id.recipeNameView)
    TextView recipeNameView;
    @BindView(R.id.servingsTextView)
    TextView recipeServingsView;

    public ViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }
}