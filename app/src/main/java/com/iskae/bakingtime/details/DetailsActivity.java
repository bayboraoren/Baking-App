package com.iskae.bakingtime.details;

import static com.iskae.bakingtime.util.Constants.DETAILS_FRAGMENT_TAG;
import static com.iskae.bakingtime.util.Constants.EXTRA_RECIPE_ID;
import static com.iskae.bakingtime.util.Constants.STEP_FRAGMENT_TAG;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.iskae.bakingtime.R;
import com.iskae.bakingtime.step.StepFragment;
import com.iskae.bakingtime.util.BaseActivity;
import com.iskae.bakingtime.viewmodel.SharedRecipeViewModel;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

/**
 * Created by iskae on 08.02.18.
 */

public class DetailsActivity extends BaseActivity {
  @Inject
  ViewModelProvider.Factory viewModelFactory;
  SharedRecipeViewModel sharedRecipeViewModel;
  private StepFragment stepFragment;

  public static void showRecipeDetails(Context context, long recipeId) {
    Intent intent = new Intent(context, DetailsActivity.class);
    intent.putExtra(EXTRA_RECIPE_ID, recipeId);
    context.startActivity(intent);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    AndroidInjection.inject(this);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipe_details);
    sharedRecipeViewModel = ViewModelProviders.of(this, viewModelFactory)
        .get(SharedRecipeViewModel.class);
    Intent intent = getIntent();
    long recipeId = intent.getLongExtra(EXTRA_RECIPE_ID, -1);
    boolean twoPane = getResources().getBoolean(R.bool.twoPane);
    if (recipeId != -1) {
      FragmentManager manager = getSupportFragmentManager();
      DetailsFragment detailsFragment = (DetailsFragment) manager.findFragmentByTag(DETAILS_FRAGMENT_TAG);
      if (detailsFragment == null) {
        detailsFragment = DetailsFragment.newInstance(recipeId);
      }
      addFragmentToActivity(manager, detailsFragment, R.id.recipeDetailsFragmentContainer, DETAILS_FRAGMENT_TAG);
      if (twoPane) {
        stepFragment = (StepFragment) manager.findFragmentByTag(STEP_FRAGMENT_TAG);
        if (stepFragment == null) {
          stepFragment = StepFragment.newInstance(recipeId, 0);
        }
        addFragmentToActivity(manager, stepFragment, R.id.stepFragmentContainer, STEP_FRAGMENT_TAG);
        sharedRecipeViewModel.getCurrentStep().observe(this, stepIndex -> {
          if (stepIndex != null) {
            stepFragment.setCurrentStepIndex(stepIndex);
            addFragmentToActivity(manager, stepFragment, R.id.stepFragmentContainer, STEP_FRAGMENT_TAG);
          }
        });
      }
    } else {
      Toast.makeText(this, R.string.details_activity_error, Toast.LENGTH_LONG).show();
    }
  }
}