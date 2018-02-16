package com.iskae.bakingtime.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by iskae on 14.02.18.
 */

public class BaseActivity extends AppCompatActivity {


  public static void addFragmentToActivity(FragmentManager fragmentManager, Fragment fragment, int containerId, String tag) {
    FragmentTransaction transaction = fragmentManager.beginTransaction();
    transaction.replace(containerId, fragment, tag);
    transaction.commit();
  }
}
