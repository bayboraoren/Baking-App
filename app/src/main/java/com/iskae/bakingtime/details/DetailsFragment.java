package com.iskae.bakingtime.details;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
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
import com.iskae.bakingtime.data.model.Step;
import com.iskae.bakingtime.di.BakingTimeApplication;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by iskae on 08.02.18.
 */

public class DetailsFragment extends Fragment {
  private static final String RECIPE_ID = "RECIPE_ID";

  @BindView(R.id.ingredientsView)
  TextView ingredientsView;
  @BindView(R.id.stepsListView)
  RecyclerView stepsListView;
  @BindView(R.id.emptyListView)
  View emptyListView;

  private RecipeStepsAdapter adapter;

  @Inject
  ViewModelProvider.Factory viewModelFactory;

  DetailsViewModel detailsViewModel;

  private long recipeId;

  public DetailsFragment() {

  }

  public static DetailsFragment newInstance(long recipeId) {
    DetailsFragment fragment = new DetailsFragment();
    Bundle args = new Bundle();
    args.putLong(RECIPE_ID, recipeId);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ((BakingTimeApplication) getActivity().getApplication())
        .getApplicationComponent()
        .inject(this);

    Bundle args = getArguments();
    recipeId = args.getLong(RECIPE_ID);
    adapter = new RecipeStepsAdapter(getContext(), new ArrayList<>());
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    detailsViewModel = ViewModelProviders.of(this, viewModelFactory)
        .get(DetailsViewModel.class);
    detailsViewModel.loadRecipeIngredients(recipeId);
    detailsViewModel.loadRecipeSteps(recipeId);
    observeIngredientsResponse();
    observeStepsResponse();
    observeError();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_recipe_details, container, false);
    ButterKnife.bind(this, rootView);

    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
    stepsListView.setLayoutManager(layoutManager);
    stepsListView.setItemAnimator(new DefaultItemAnimator());
    stepsListView.setAdapter(adapter);
    return rootView;
  }

  private void observeIngredientsResponse() {
    detailsViewModel.getIngredientsList().observe(this, this::processIngredientsList);
  }

  private void observeStepsResponse() {
    detailsViewModel.getStepsList().observe(this, this::processStepsList);
  }

  private void observeError() {
    detailsViewModel.getError().observe(this, this::processError);
  }

  private void processIngredientsList(List<Ingredient> ingredients) {
    if (ingredients != null && ingredients.size() > 0) {
      StringBuilder builder = new StringBuilder();
      for (int i = 0; i < ingredients.size(); i++) {
        Ingredient ingredient = ingredients.get(i);
        builder.append("* ");
        builder.append(ingredient.getIngredient() + " ");
        builder.append(String.valueOf(ingredient.getQuantity()) + " ");
        builder.append(ingredient.getMeasure() + System.lineSeparator());
      }
      ingredientsView.setText(builder.toString());
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
