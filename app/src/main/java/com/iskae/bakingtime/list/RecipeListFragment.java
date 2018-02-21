package com.iskae.bakingtime.list;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iskae.bakingtime.R;
import com.iskae.bakingtime.data.model.Recipe;
import com.iskae.bakingtime.di.BakingTimeApplication;
import com.iskae.bakingtime.util.Constants;
import com.iskae.bakingtime.viewmodel.RecipeListViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

public class RecipeListFragment extends Fragment {



  @BindView(R.id.recipesListView)
  RecyclerView recipesListView;
  @BindView(R.id.emptyListView)
  View emptyListView;
  @BindView(R.id.swipeRefreshLayout)
  SwipeRefreshLayout swipeRefreshLayout;
  @BindView(R.id.loadingProgressView)
  View loadingProgressView;

  @Inject
  ViewModelProvider.Factory viewModelFactory;

  RecipeListViewModel listViewModel;

  private RecipesListAdapter adapter;

  public RecipeListFragment() {
  }

  @Override
  public void onAttach(Context context) {
    AndroidSupportInjection.inject(this);
    super.onAttach(context);
  }

  public static RecipeListFragment newInstance(boolean isPickRecipe) {
    RecipeListFragment fragment = new RecipeListFragment();
    Bundle args = new Bundle();
    args.putBoolean(Constants.EXTRA_IS_PICK_RECIPE, isPickRecipe);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Bundle args = getArguments();
    boolean isPickRecipe = false;
    if (args != null) {
      isPickRecipe = args.getBoolean(Constants.EXTRA_IS_PICK_RECIPE);
    }
    adapter = new RecipesListAdapter(getContext(), new ArrayList<>(), isPickRecipe);

  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    listViewModel = ViewModelProviders.of(getActivity(), viewModelFactory)
        .get(RecipeListViewModel.class);
    listViewModel.loadRecipesList();
    observeLoadingStatus();
    observeResponse();
    observeError();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_list, container, false);
    ButterKnife.bind(this, rootView);

    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),
        getResources().getInteger(R.integer.column_count));
    recipesListView.setLayoutManager(layoutManager);
    recipesListView.setItemAnimator(new DefaultItemAnimator());
    recipesListView.setAdapter(adapter);
    swipeRefreshLayout.setOnRefreshListener(() -> listViewModel.refreshRecipes());

    return rootView;
  }

  private void observeLoadingStatus() {
    listViewModel.getLoadingStatus().observe(this, this::processLoadingStatus);
  }

  private void observeResponse() {
    listViewModel.getRecipesList().observe(this, this::processList);
  }

  private void observeError() {
    listViewModel.getError().observe(this, this::processError);
  }

  private void processLoadingStatus(boolean isLoading) {
    loadingProgressView.setVisibility(isLoading ? View.VISIBLE : View.GONE);
  }

  private void processList(List<Recipe> list) {
    swipeRefreshLayout.setRefreshing(false);
    adapter.setRecipesList(list);
    if (list != null && list.size() > 0) {
      recipesListView.setVisibility(View.VISIBLE);
      emptyListView.setVisibility(View.GONE);
    } else {
      recipesListView.setVisibility(View.GONE);
      emptyListView.setVisibility(View.VISIBLE);
    }
  }

  private void processError(Throwable error) {
  }

}
