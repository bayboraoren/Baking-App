package com.iskae.bakingtime.adapters;

import android.content.*;
import android.graphics.drawable.*;
import android.support.v4.graphics.drawable.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;

import com.iskae.bakingtime.R;
import com.iskae.bakingtime.listeners.*;
import com.iskae.bakingtime.data.model.*;

import java.util.*;

import butterknife.*;

/**
 * Created by iskae on 08.02.18.
 */

public class RecipesListAdapter extends RecyclerView.Adapter<RecipesListAdapter.ViewHolder> {

    private Context context;
    private List<Recipe> recipesList;
    private OnRecipeClickListener listener;

    public RecipesListAdapter(Context context, List<Recipe> recipes, OnRecipeClickListener listener) {
        this.context = context;
        this.recipesList = recipes;
        this.listener = listener;
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
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onRecipeClick(recipe.getId());
                }
            });
        }
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