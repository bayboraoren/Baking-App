package com.iskae.bakingtime.step;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.iskae.bakingtime.R;
import com.iskae.bakingtime.data.model.Step;
import com.iskae.bakingtime.util.BaseActivity;

import java.util.ArrayList;

/**
 * Created by iskae on 08.02.18.
 */

public class StepActivity extends BaseActivity {
  private static final String EXTRA_STEPS = "EXTRA_STEPS";
  private static final String EXTRA_CURRENT_STEP_INDEX = "EXTRA_CURRENT_STEP_INDEX";
  private ArrayList<Step> steps;
  private int currentStepIndex;

  public static void showRecipeSteps(Context context, ArrayList<Step> steps, int currentStepIndex) {
    Intent intent = new Intent(context, StepActivity.class);
    intent.putExtra(EXTRA_CURRENT_STEP_INDEX, currentStepIndex);
    context.startActivity(intent);
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipe_step);
    currentStepIndex = getIntent().getIntExtra(EXTRA_CURRENT_STEP_INDEX, 0);
    if (steps != null && savedInstanceState == null) {
      Step step = steps.get(currentStepIndex);
      if (step != null) {
        StepFragment stepFragment = new StepFragment();
        stepFragment.setRecipeSteps(steps);
        stepFragment.setCurrentStepIndex(currentStepIndex);
        getSupportFragmentManager().beginTransaction().add(R.id.stepFragmentContainer, stepFragment).commit();
      }
    }
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt(EXTRA_CURRENT_STEP_INDEX, currentStepIndex);
  }
}
