package com.iskae.bakingtime.widget;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.iskae.bakingtime.R;

import butterknife.ButterKnife;

/**
 * Created by iskae on 12.02.18.
 */

public class ConfigureIngredientsWidgetActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_list);
    ButterKnife.bind(this);

  }


}
