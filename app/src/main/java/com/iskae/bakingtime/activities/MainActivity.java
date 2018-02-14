package com.iskae.bakingtime.activities;

import android.os.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;

import com.iskae.bakingtime.R;
import com.iskae.bakingtime.adapters.*;
import com.iskae.bakingtime.api.*;
import com.iskae.bakingtime.listeners.*;
import com.iskae.bakingtime.data.model.*;

import java.util.*;

import butterknife.*;
import retrofit2.*;

public class MainActivity extends AppCompatActivity implements OnRecipeClickListener {
    private static final String RECIPES = "RECIPES";

    @BindView(R.id.recipesListView)
    RecyclerView recipesListView;
    @BindView(R.id.emptyRecipesListView)
    RelativeLayout emptyRecipesListView;

    private ArrayList<Recipe> recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.column_count));
        recipesListView.setLayoutManager(layoutManager);
        recipesListView.setItemAnimator(new DefaultItemAnimator());
        if (savedInstanceState != null) {
            recipes = savedInstanceState.getParcelableArrayList(RECIPES);
            updateViews();
        } else {
            BakingTimeService service = BakingTimeClient.getBakingTimeClient().create(BakingTimeService.class);
            Call<List<Recipe>> recipesCall = service.getRecipes();
            recipesCall.enqueue(new Callback<List<Recipe>>() {
                @Override
                public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                    if (response.isSuccessful()) {
                        recipes = new ArrayList<>(response.body());
                        updateViews();
                    }
                }

                @Override
                public void onFailure(Call<List<Recipe>> call, Throwable t) {
                }
            });
        }


    }

    @Override
    public void onRecipeClick(long recipeId) {
        Recipe recipe = getRecipeById(recipeId);
        if (recipe != null) {
            RecipeDetailsActivity.showRecipeDetails(this, recipe);
        }

    }

    private Recipe getRecipeById(long recipeId) {
        if (recipes != null) {
            for (Recipe recipe : recipes) {
                if (recipe.getId() == recipeId) {
                    return recipe;
                }
            }
        }
        return null;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(RECIPES, recipes);
        super.onSaveInstanceState(outState);
    }

    private void updateViews() {
        recipesListView.setAdapter(new RecipesListAdapter(this, recipes, this));
        if (recipes == null || recipes.size() == 0) {
            emptyRecipesListView.setVisibility(View.VISIBLE);
        }
    }
}
