package com.iskae.bakingtime.widget;

import android.os.*;
import android.support.v7.app.*;

import com.iskae.bakingtime.R;
import com.iskae.bakingtime.api.*;
import com.iskae.bakingtime.data.model.*;

import java.util.*;

import butterknife.*;
import retrofit2.*;

/**
 * Created by iskae on 12.02.18.
 */

public class ConfigureIngredientsWidgetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        BakingTimeService service = BakingTimeClient.getBakingTimeClient().create(BakingTimeService.class);
        Call<List<Recipe>> recipesCall = service.getRecipes();
        recipesCall.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful()) {
                    List<Recipe> recipes = response.body();
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
            }
        });
    }


}
