package com.iskae.bakingtime.step;

import static com.google.android.exoplayer2.mediacodec.MediaCodecInfo.TAG;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iskae.bakingtime.R;
import com.iskae.bakingtime.data.model.Step;
import com.iskae.bakingtime.util.Constants;
import com.iskae.bakingtime.viewmodel.SharedRecipeViewModel;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

/**
 * Created by iskae on 08.02.18.
 */

public class StepFragment extends Fragment implements Player.EventListener {
  @BindView(R.id.playerView)
  SimpleExoPlayerView playerView;
  @BindView(R.id.descriptionTextView)
  TextView descriptionTextView;
  @BindView(R.id.stepDescriptionTitle)
  TextView stepDescriptionTitle;
  @BindView(R.id.nextStepImageView)
  ImageView nextStepImageView;
  @BindView(R.id.previousStepImageView)
  ImageView previousStepImageView;
  @BindView(R.id.currentStepTextView)
  TextView currentStepTextView;
  @BindView(R.id.emptyVideoTextView)
  TextView emptyVideoTextView;
  @BindView(R.id.bottomNavigationView)
  RelativeLayout bottomNavigationView;

  @Inject
  ViewModelProvider.Factory viewModelFactory;

  SharedRecipeViewModel sharedRecipeViewModel;

  private SimpleExoPlayer player;
  private MediaSessionCompat mediaSession;
  private PlaybackStateCompat.Builder stateBuilder;
  private List<Step> steps;
  private String videoUrl;

  private long recipeId;
  private int currentStepIndex;
  private long lastPosition;
  private boolean playWhenReady = true;

  public StepFragment() {
  }

  public static StepFragment newInstance(long recipeId, int stepIndex) {
    StepFragment fragment = new StepFragment();
    Bundle args = new Bundle();
    args.putLong(Constants.EXTRA_RECIPE_ID, recipeId);
    args.putInt(Constants.EXTRA_STEP_INDEX, stepIndex);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onAttach(Context context) {
    AndroidSupportInjection.inject(this);
    super.onAttach(context);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Bundle args = getArguments();
    recipeId = args.getLong(Constants.EXTRA_RECIPE_ID);
    currentStepIndex = args.getInt(Constants.EXTRA_STEP_INDEX);
    if (savedInstanceState != null) {
      playWhenReady = savedInstanceState.getBoolean(Constants.EXTRA_PLAY_WHEN_READY);
      lastPosition = savedInstanceState.getLong(Constants.EXTRA_LAST_POSITION);
    }
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    sharedRecipeViewModel = ViewModelProviders.of(getActivity(), viewModelFactory)
        .get(SharedRecipeViewModel.class);
    sharedRecipeViewModel.loadRecipeById(recipeId);
    observeRecipeResponse();
    observeCurrentStepIndex();
    observeError();
  }

  private void observeCurrentStepIndex() {
    sharedRecipeViewModel.getCurrentStep().observe(this, stepIndex -> {
      if (currentStepIndex != stepIndex) {
        StepFragment stepFragment = newInstance(recipeId, stepIndex);
        getActivity().getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.stepFragmentContainer, stepFragment)
            .commit();
      }
    });
  }

  private void observeError() {
  }

  private void observeRecipeResponse() {
    sharedRecipeViewModel.getRecipe().observe(this, recipe -> {
      if (recipe != null) {
        steps = recipe.getSteps();
        processStepsList();
      }
    });
  }

  private void processStepsList() {
    if (steps != null) {
      boolean twoPane = getResources().getBoolean(R.bool.twoPane);
      boolean landscapeMode = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
      Step step = steps.get(currentStepIndex);
      if (step != null) {
        initializeBottomNavigation(steps);
        if (step.getDescription() != null)
          descriptionTextView.setText(step.getDescription());
        if (step.getVideoUrl() != null && step.getVideoUrl().length() > 0) {
          videoUrl = step.getVideoUrl();
          initializeMediaSession();
          initializePlayer();
          playerView.setVisibility(View.VISIBLE);
          if (landscapeMode && !twoPane) {
            playerView.setLayoutParams(new
                ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            hideStepDetails();
          }
        } else {
          emptyVideoTextView.setVisibility(View.VISIBLE);
          playerView.setVisibility(View.GONE);
        }
      }
    }
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_recipe_step, container, false);
    ButterKnife.bind(this, rootView);
    return rootView;
  }

  private void hideStepDetails() {
    bottomNavigationView.setVisibility(View.GONE);
    stepDescriptionTitle.setVisibility(View.GONE);
    descriptionTextView.setVisibility(View.GONE);
  }

  private void initializeBottomNavigation(List<Step> steps) {
    currentStepTextView.setText(getContext().getString(R.string.currentStepText, currentStepIndex + 1, steps.size()));
    if (currentStepIndex == 0) {
      previousStepImageView.setVisibility(View.INVISIBLE);
    } else {
      previousStepImageView.setVisibility(View.VISIBLE);
      previousStepImageView.setOnClickListener(v -> {
        sharedRecipeViewModel.setCurrentStep(currentStepIndex - 1);
      });
    }
    if (currentStepIndex == steps.size() - 1) {
      nextStepImageView.setVisibility(View.INVISIBLE);
    } else {
      nextStepImageView.setVisibility(View.VISIBLE);
      nextStepImageView.setOnClickListener(v -> {
        sharedRecipeViewModel.setCurrentStep(currentStepIndex + 1);
      });
    }
  }

  private void initializePlayer() {
    if (player == null) {
      TrackSelector trackSelector = new DefaultTrackSelector();
      LoadControl loadControl = new DefaultLoadControl();
      RenderersFactory renderersFactory = new DefaultRenderersFactory(getContext());
      player = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);
      playerView.setPlayer(player);
      player.addListener(this);
      player.seekTo(lastPosition);
      player.setPlayWhenReady(playWhenReady);
      String userAgent = Util.getUserAgent(getContext(), getContext().getString(R.string.app_name));
      ExtractorMediaSource.Factory factory = new ExtractorMediaSource.Factory(new DefaultDataSourceFactory(getContext(), userAgent));
      MediaSource mediaSource = factory.createMediaSource(Uri.parse(videoUrl));
      player.prepare(mediaSource, true, false);
    }
  }

  private void initializeMediaSession() {
    if (mediaSession == null) {
      mediaSession = new MediaSessionCompat(getContext(), TAG);
      mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
          MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
      mediaSession.setMediaButtonReceiver(null);
      stateBuilder = new PlaybackStateCompat.Builder()
          .setActions(PlaybackStateCompat.ACTION_PLAY |
              PlaybackStateCompat.ACTION_PAUSE |
              PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
              PlaybackStateCompat.ACTION_PLAY_PAUSE);
      mediaSession.setPlaybackState(stateBuilder.build());
      mediaSession.setCallback(new RecipeStepSessionCallback());
      mediaSession.setActive(true);
    }
  }

  @Override
  public void onPause() {
    super.onPause();
    releasePlayer();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    releasePlayer();
    if (mediaSession != null)
      mediaSession.setActive(false);
  }

  @Override
  public void onStop() {
    super.onStop();
    releasePlayer();
  }

  private void releasePlayer() {
    if (player != null) {
      lastPosition = player.getCurrentPosition();
      playWhenReady = player.getPlayWhenReady();
      player.stop();
      player.release();
      player = null;
    }
  }

  @Override
  public void onSaveInstanceState(@NonNull Bundle outState) {
    outState.putLong(Constants.EXTRA_LAST_POSITION, lastPosition);
    outState.putBoolean(Constants.EXTRA_PLAY_WHEN_READY, playWhenReady);
    super.onSaveInstanceState(outState);
  }

  public void setCurrentStepIndex(int stepIndex) {
    this.currentStepIndex = stepIndex;
  }

  @Override
  public void onTimelineChanged(Timeline timeline, Object manifest) {

  }

  @Override
  public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

  }

  @Override
  public void onLoadingChanged(boolean isLoading) {

  }

  @Override
  public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
    if ((playbackState == Player.STATE_READY) && playWhenReady) {
      stateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, player.getCurrentPosition(), 1f);
    } else if ((playbackState == Player.STATE_READY)) {
      stateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, player.getCurrentPosition(), 1f);
    }
    mediaSession.setPlaybackState(stateBuilder.build());
  }

  @Override
  public void onRepeatModeChanged(int repeatMode) {

  }

  @Override
  public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

  }

  @Override
  public void onPlayerError(ExoPlaybackException error) {

  }

  @Override
  public void onPositionDiscontinuity(int reason) {

  }

  @Override
  public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

  }

  @Override
  public void onSeekProcessed() {

  }

  private class RecipeStepSessionCallback extends MediaSessionCompat.Callback {
    @Override
    public void onPlay() {
      player.setPlayWhenReady(true);
    }

    @Override
    public void onPause() {
      player.setPlayWhenReady(false);
    }

    @Override
    public void onSkipToPrevious() {
      player.seekTo(0);
    }
  }
}
