package com.iskae.bakingtime;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.iskae.bakingtime.details.DetailsActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by iskae on 28.02.18.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RecipeDetailsActivityTest {

  @Rule
  public ActivityTestRule<DetailsActivity> detailsActivityTestRule = new ActivityTestRule<>(DetailsActivity.class);

  @Test
  public void stepClickOpensStepActivityTest() {
    ViewInteraction recipeStepsList = onView(allOf(
        withId(R.id.stepsListView),
        isDisplayed(),
        withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))
    );
    recipeStepsList.check(matches(isDisplayed()));
    recipeStepsList.perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    onView(withId(R.id.playerView)).check(matches(isDisplayed()));
  }
}
