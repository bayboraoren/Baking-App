package com.iskae.bakingtime.list;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.iskae.bakingtime.R;
import com.iskae.bakingtime.util.BaseActivity;

public class RecipeListActivity extends BaseActivity {
  private static final String LIST_FRAGMENT_TAG = "LIST_FRAGMENT_TAG";


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_list);
    FragmentManager manager = getSupportFragmentManager();
    RecipeListFragment fragment = (RecipeListFragment) manager.findFragmentByTag(LIST_FRAGMENT_TAG);
    if (fragment == null) {
      fragment = RecipeListFragment.newInstance();
    }
    addFragmentToActivity(manager, fragment, R.id.list_fragment_container, LIST_FRAGMENT_TAG);
  }
}
