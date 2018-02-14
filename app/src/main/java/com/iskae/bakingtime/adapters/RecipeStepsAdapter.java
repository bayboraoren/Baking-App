package com.iskae.bakingtime.adapters;

import android.content.*;
import android.graphics.drawable.*;
import android.support.v4.graphics.drawable.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;

import com.iskae.bakingtime.R;
import com.iskae.bakingtime.listeners.*;
import com.iskae.bakingtime.data.model.*;

import java.util.*;

import butterknife.*;

/**
 * Created by iskae on 08.02.18.
 */

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.ViewHolder> {

    private Context context;
    private List<Step> steps;
    private OnStepClickListener listener;

    public RecipeStepsAdapter(Context context, List<Step> steps, OnStepClickListener listener) {
        this.context = context;
        this.steps = steps;
        this.listener = listener;
    }

    @Override
    public RecipeStepsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recipe_step_list_item, parent, false);
        return new RecipeStepsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeStepsAdapter.ViewHolder holder, final int position) {
        final Step step = steps.get(position);
        if (step != null) {
            Drawable playCircle = context.getResources().getDrawable(R.drawable.play_circle, context.getTheme());
            DrawableCompat.setTint(playCircle, context.getColor(R.color.colorPrimary));
            GlideApp.with(context).load(step.getThumbnailUrl())
                    .fallback(playCircle)
                    .error(playCircle)
                    .placeholder(playCircle)
                    .into(holder.thumbnailImage);
            holder.stepShortDescriptionView.setText(context.getString(R.string.short_description_text, position + 1, step.getShortDescription()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onStepClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (steps == null) return 0;
        return steps.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.thumbnailImage)
        ImageView thumbnailImage;
        @BindView(R.id.stepShortDescriptionView)
        TextView stepShortDescriptionView;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
