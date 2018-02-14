package com.iskae.bakingtime.activities;

import android.content.*;
import android.os.*;
import android.support.annotation.*;
import android.support.v7.app.*;

import com.iskae.bakingtime.*;
import com.iskae.bakingtime.fragments.*;
import com.iskae.bakingtime.listeners.*;
import com.iskae.bakingtime.data.model.*;

import java.util.*;

/**
 * Created by iskae on 08.02.18.
 */

public class RecipeStepsActivity extends AppCompatActivity implements OnStepClickListener {
    private static final String EXTRA_STEPS = "EXTRA_STEPS";
    private static final String EXTRA_CURRENT_STEP_INDEX = "EXTRA_CURRENT_STEP_INDEX";
    private ArrayList<Step> steps;
    private int currentStepIndex;

    public static void showRecipeSteps(Context context, ArrayList<Step> steps, int currentStepIndex) {
        Intent intent = new Intent(context, RecipeStepsActivity.class);
        intent.putParcelableArrayListExtra(EXTRA_STEPS, steps);
        intent.putExtra(EXTRA_CURRENT_STEP_INDEX, currentStepIndex);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);
        steps = getIntent().getParcelableArrayListExtra(EXTRA_STEPS);
        currentStepIndex = getIntent().getIntExtra(EXTRA_CURRENT_STEP_INDEX, 0);
        if (steps != null && savedInstanceState == null) {
            Step step = steps.get(currentStepIndex);
            if (step != null) {
                RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
                recipeStepFragment.setRecipeSteps(steps);
                recipeStepFragment.setCurrentStepIndex(currentStepIndex);
                getSupportFragmentManager().beginTransaction().add(R.id.stepFragmentContainer, recipeStepFragment).commit();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STEPS, steps);
        outState.putInt(EXTRA_CURRENT_STEP_INDEX, currentStepIndex);
    }

    @Override
    public void onStepClick(int stepIndex) {
        if (steps != null) {
            Step step = steps.get(stepIndex);
            if (step != null) {
                currentStepIndex = stepIndex;
                RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
                recipeStepFragment.setRecipeSteps(steps);
                recipeStepFragment.setCurrentStepIndex(stepIndex);
                getSupportFragmentManager().beginTransaction().replace(R.id.stepFragmentContainer, recipeStepFragment).commit();
            }
        }
    }
}
