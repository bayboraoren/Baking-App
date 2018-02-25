package com.iskae.bakingtime.list;

import static com.iskae.bakingtime.util.Constants.LIST_FRAGMENT_TAG;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.iskae.bakingtime.R;
import com.iskae.bakingtime.util.BaseActivity;
import com.iskae.bakingtime.util.Constants;

import dagger.android.AndroidInjection;

public class RecipeListActivity extends BaseActivity {

  public static void startPicker(Activity parent) {
    Intent intent = new Intent(parent, RecipeListActivity.class);
    intent.setAction(Intent.ACTION_PICK);
    parent.startActivityForResult(intent, Constants.REQUEST_CODE_PICKER);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    AndroidInjection.inject(this);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_list);
    FragmentManager manager = getSupportFragmentManager();
    RecipeListFragment fragment = (RecipeListFragment) manager.findFragmentByTag(LIST_FRAGMENT_TAG);
    boolean isPick = Intent.ACTION_PICK.equals(getIntent().getAction());
    if (isPick) {
      Toast.makeText(this, R.string.widget_pick_recipe_toast, Toast.LENGTH_LONG).show();
    }
    if (fragment == null) {
      fragment = RecipeListFragment.newInstance(isPick);
    }
    addFragmentToActivity(manager, fragment, R.id.list_fragment_container, LIST_FRAGMENT_TAG);
  }
}
