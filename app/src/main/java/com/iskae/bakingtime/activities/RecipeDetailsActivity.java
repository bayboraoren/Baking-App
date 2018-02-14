package com.iskae.bakingtime.activities;

import android.content.*;
import android.os.*;
import android.support.annotation.*;
import android.support.v7.app.*;
import android.widget.*;

import com.iskae.bakingtime.R;
import com.iskae.bakingtime.fragments.*;
import com.iskae.bakingtime.listeners.*;
import com.iskae.bakingtime.data.model.*;

import java.util.*;

import butterknife.*;

/**
 * Created by iskae on 08.02.18.
 */

public class RecipeDetailsActivity extends AppCompatActivity implements OnStepClickListener {
    private static final String EXTRA_RECIPE = "EXTRA_RECIPE";
    private static final String CURRENT_STEP = "CURRENT_STEP";
    @BindView(R.id.recipeDetailsFragmentContainer)
    FrameLayout recipeDetailsFragmentContainer;
    @Nullable
    @BindView(R.id.stepFragmentContainer)
    FrameLayout stepFragmentContainer;
    private Recipe recipe;
    private int currentStep;
    private boolean twoPane;

    public static void showRecipeDetails(Context context, Recipe recipe) {
        Intent intent = new Intent(context, RecipeDetailsActivity.class);
        intent.putExtra(EXTRA_RECIPE, recipe);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        ButterKnife.bind(this);

        twoPane = getResources().getBoolean(R.bool.twoPane);
        recipe = getIntent().getParcelableExtra(EXTRA_RECIPE);
        currentStep = getIntent().getIntExtra(CURRENT_STEP, 0);
        if (savedInstanceState == null) {
            if (recipe != null) {
                getSupportActionBar().setTitle(recipe.getName());
                RecipeDetailsFragment recipeDetailsFragment = new RecipeDetailsFragment();
                recipeDetailsFragment.setRecipe(recipe);
                recipeDetailsFragment.setOnStepClickListener(this);
                getSupportFragmentManager().beginTransaction().add(R.id.recipeDetailsFragmentContainer, recipeDetailsFragment).commit();
                if (twoPane) {
                    RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
                    recipeStepFragment.setCurrentStepIndex(currentStep);
                    recipeStepFragment.setRecipeSteps(recipe.getSteps());
                    getSupportFragmentManager().beginTransaction().add(R.id.stepFragmentContainer, recipeStepFragment).commit();
                }
            }
        } else {
            RecipeDetailsFragment recipeDetailsFragment = (RecipeDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.recipeDetailsFragmentContainer);
            if (recipeDetailsFragment != null) {
                recipeDetailsFragment.setOnStepClickListener(this);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_RECIPE, recipe);
        outState.putInt(CURRENT_STEP, currentStep);
    }

    @Override
    public void onStepClick(int stepIndex) {
        if (recipe != null) {
            Step step = recipe.getSteps().get(stepIndex);
            if (step != null) {
                currentStep = stepIndex;
                RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
                recipeStepFragment.setRecipeSteps(recipe.getSteps());
                recipeStepFragment.setCurrentStepIndex(stepIndex);
                if (twoPane) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.stepFragmentContainer, recipeStepFragment).commit();
                } else {
                    RecipeStepsActivity.showRecipeSteps(this, new ArrayList<>(recipe.getSteps()), stepIndex);
                }
            }
        }
    }
}
