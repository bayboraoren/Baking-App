package com.iskae.bakingtime.fragments;

import android.os.*;
import android.support.annotation.*;
import android.support.v4.app.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;

import com.iskae.bakingtime.R;
import com.iskae.bakingtime.adapters.*;
import com.iskae.bakingtime.listeners.*;
import com.iskae.bakingtime.data.model.*;

import java.util.*;

import butterknife.*;

/**
 * Created by iskae on 08.02.18.
 */

public class RecipeDetailsFragment extends Fragment {
    private static final String RECIPE = "RECIPE";
    private static final String SCROLL_POSITION = "SCROLL_POSITION";
    private static final String STEPS_STATE = "STEPS_STATE";

    @BindView(R.id.ingredientsView)
    TextView ingredientsView;
    @BindView(R.id.stepsListView)
    RecyclerView stepsListView;
    @BindView(R.id.emptyStepsListView)
    RelativeLayout emptyStepsListView;
    @BindView(R.id.detailsScrollView)
    ScrollView detailsScrollView;

    private Recipe recipe;
    private Parcelable stepsState;
    private int scrollPosition;
    private OnStepClickListener listener;

    public RecipeDetailsFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            recipe = savedInstanceState.getParcelable(RECIPE);
            stepsState = savedInstanceState.getParcelable(STEPS_STATE);
            scrollPosition = savedInstanceState.getInt(SCROLL_POSITION);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_details, container, false);
        ButterKnife.bind(this, rootView);

        if (recipe != null) {
            List<Step> steps = recipe.getSteps();
            List<Ingredient> ingredients = recipe.getIngredients();
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

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            stepsListView.setLayoutManager(layoutManager);
            stepsListView.setItemAnimator(new DefaultItemAnimator());
            stepsListView.setAdapter(new RecipeStepsAdapter(getContext(), steps, listener));
            if (steps == null || steps.size() == 0)
                emptyStepsListView.setVisibility(View.VISIBLE);
            if (savedInstanceState != null) {
                stepsListView.getLayoutManager().onRestoreInstanceState(stepsState);
                detailsScrollView.scrollTo(0, scrollPosition);
            }
        }
        return rootView;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public void setOnStepClickListener(OnStepClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(RECIPE, recipe);
        outState.putParcelable(STEPS_STATE, stepsListView.getLayoutManager().onSaveInstanceState());
        outState.putInt(SCROLL_POSITION, detailsScrollView.getScrollY());
    }
}
