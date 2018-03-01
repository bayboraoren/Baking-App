package com.iskae.bakingtime.step;

import static com.iskae.bakingtime.util.Constants.EXTRA_CURRENT_STEP_INDEX;
import static com.iskae.bakingtime.util.Constants.EXTRA_RECIPE_ID;
import static com.iskae.bakingtime.util.Constants.STEP_FRAGMENT_TAG;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;

import com.iskae.bakingtime.R;
import com.iskae.bakingtime.util.BaseActivity;
import com.iskae.bakingtime.viewmodel.SharedRecipeViewModel;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

/**
 * Created by iskae on 08.02.18.
 */

public class StepActivity extends BaseActivity {
  @Inject
  ViewModelProvider.Factory viewModelFactory;
  SharedRecipeViewModel sharedRecipeViewModel;

  public static void showRecipeStep(Context context, long recipeId, int currentStepIndex) {
    Intent intent = new Intent(context, StepActivity.class);
    intent.putExtra(EXTRA_RECIPE_ID, recipeId);
    intent.putExtra(EXTRA_CURRENT_STEP_INDEX, currentStepIndex);
    context.startActivity(intent);
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    AndroidInjection.inject(this);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipe_step);
    sharedRecipeViewModel = ViewModelProviders.of(this, viewModelFactory)
        .get(SharedRecipeViewModel.class);
    Intent intent = getIntent();
    long recipeId = intent.getLongExtra(EXTRA_RECIPE_ID, -1);
    int currentStepIndex = intent.getIntExtra(EXTRA_CURRENT_STEP_INDEX, 0);
    if (savedInstanceState == null && recipeId != -1) {
      FragmentManager manager = getSupportFragmentManager();
      StepFragment stepFragment = (StepFragment) manager.findFragmentByTag(STEP_FRAGMENT_TAG);
      if (stepFragment == null) {
        stepFragment = StepFragment.newInstance(recipeId, currentStepIndex);
      }
      addFragmentToActivity(manager, stepFragment, R.id.stepFragmentContainer, STEP_FRAGMENT_TAG);
    }
  }
}
