package com.iskae.bakingtime.details;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iskae.bakingtime.R;
import com.iskae.bakingtime.data.model.Ingredient;
import com.iskae.bakingtime.data.model.Recipe;
import com.iskae.bakingtime.data.model.Step;
import com.iskae.bakingtime.step.StepActivity;
import com.iskae.bakingtime.util.Constants;
import com.iskae.bakingtime.util.IngredientUtils;
import com.iskae.bakingtime.viewmodel.SharedRecipeViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

/**
 * Created by iskae on 08.02.18.
 */

public class DetailsFragment extends Fragment {
  @BindView(R.id.ingredientsView)
  TextView ingredientsView;
  @BindView(R.id.stepsListView)
  RecyclerView stepsListView;
  @BindView(R.id.emptyListView)
  View emptyListView;
  @Inject
  ViewModelProvider.Factory viewModelFactory;
  SharedRecipeViewModel sharedRecipeViewModel;
  private RecipeStepsAdapter adapter;
  private long recipeId;
  private boolean twoPane;

  public DetailsFragment() {

  }

  public static DetailsFragment newInstance(long recipeId) {
    DetailsFragment fragment = new DetailsFragment();
    Bundle args = new Bundle();
    args.putLong(Constants.EXTRA_RECIPE_ID, recipeId);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onAttach(Context context) {
    AndroidSupportInjection.inject(this);
    super.onAttach(context);
    twoPane = getResources().getBoolean(R.bool.twoPane);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Bundle args = getArguments();
    if (args != null)
      recipeId = args.getLong(Constants.EXTRA_RECIPE_ID);
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    observeRecipeResponse();
    observeCurrentStep();
    observeError();
  }

  private void observeRecipeResponse() {
    sharedRecipeViewModel.getRecipe().observe(this, this::processRecipe);
  }

  private void processRecipe(Recipe recipe) {
    if (recipe != null) {
      processIngredientsList(recipe.getIngredients());
      processStepsList(recipe.getSteps());
    }
  }

  private void observeCurrentStep() {
    if (!twoPane) {
      sharedRecipeViewModel.getCurrentStep().observe(this, stepIndex -> {
        if (stepIndex != null)
          StepActivity.showRecipeStep(getContext(), recipeId, stepIndex);
      });
    }
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_recipe_details, container, false);
    ButterKnife.bind(this, rootView);
    sharedRecipeViewModel = ViewModelProviders.of(getActivity(), viewModelFactory)
        .get(SharedRecipeViewModel.class);
    sharedRecipeViewModel.loadRecipeById(recipeId);

    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
    stepsListView.setLayoutManager(layoutManager);
    stepsListView.setItemAnimator(new DefaultItemAnimator());
    adapter = new RecipeStepsAdapter(getContext(), new ArrayList<>(), sharedRecipeViewModel);
    stepsListView.setAdapter(adapter);
    return rootView;
  }

  private void observeError() {
    sharedRecipeViewModel.getError().observe(this, this::processError);
  }

  private void processIngredientsList(List<Ingredient> ingredients) {
    String ingredientsText = IngredientUtils.getIngredientsAsText(ingredients);
    if (ingredientsText != null) {
      ingredientsView.setText(ingredientsText);
    } else {
      ingredientsView.setText(R.string.no_ingredients_found);
    }
  }

  private void processStepsList(List<Step> steps) {
    adapter.setStepsList(steps);
    if (steps != null && steps.size() > 0) {
      stepsListView.setVisibility(View.VISIBLE);
      emptyListView.setVisibility(View.GONE);
    } else {
      stepsListView.setVisibility(View.GONE);
      emptyListView.setVisibility(View.VISIBLE);
    }
  }

  private void processError(Throwable error) {
  }
}
