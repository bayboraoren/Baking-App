package com.iskae.bakingtime.details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.iskae.bakingtime.R;
import com.iskae.bakingtime.util.BaseActivity;

import butterknife.ButterKnife;

/**
 * Created by iskae on 08.02.18.
 */

public class DetailsActivity extends BaseActivity {
  private static final String EXTRA_RECIPE_ID = "EXTRA_RECIPE_ID";
  private static final String DETAILS_FRAGMENT_TAG = "DETAILS_FRAGMENT_TAG";
  private static final String STEPS_FRAGMENT_TAG = "STEPS_FRAGMENT_TAG";
  private static final String CURRENT_STEP = "CURRENT_STEP";
  private int currentStep;
  private boolean twoPane;

  public static void showRecipeDetails(Context context, long recipeId) {
    Intent intent = new Intent(context, DetailsActivity.class);
    intent.putExtra(EXTRA_RECIPE_ID, recipeId);
    context.startActivity(intent);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipe_details);
    ButterKnife.bind(this);

    Intent intent = getIntent();
    long recipeId = intent.getLongExtra(EXTRA_RECIPE_ID, -1);
    twoPane = getResources().getBoolean(R.bool.twoPane);
    if (recipeId != -1) {
      FragmentManager manager = getSupportFragmentManager();
      DetailsFragment detailsFragment = (DetailsFragment) manager.findFragmentByTag(DETAILS_FRAGMENT_TAG);

      if (detailsFragment == null) {
        detailsFragment = DetailsFragment.newInstance(recipeId);
      }
      addFragmentToActivity(manager, detailsFragment, R.id.recipeDetailsFragmentContainer, DETAILS_FRAGMENT_TAG);
      if (twoPane) {
      }
    } else {
      Toast.makeText(this, R.string.details_activity_error, Toast.LENGTH_LONG).show();
    }
  }
}