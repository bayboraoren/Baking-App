package com.iskae.bakingtime.step;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by iskae on 08.02.18.
 */

public class StepFragment extends Fragment implements Player.EventListener {
  private static final String TAG = StepFragment.class.getSimpleName();

  private static final String PLAYER_CURRENT_POSITION = "PLAYER_CURRENT_POSITION";
  private static final String STEPS = "STEPS";
  private static final String CURRENT_STEP = "CURRENT_STEP";
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
  private SimpleExoPlayer player;
  private MediaSessionCompat mediaSession;
  private PlaybackStateCompat.Builder stateBuilder;
  private ArrayList<Step> steps;
  private int currentStepIndex;
  private long playbackPosition;

  public StepFragment() {
  }


  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_recipe_step, container, false);
    ButterKnife.bind(this, rootView);
    if (steps != null) {
      boolean twoPane = getResources().getBoolean(R.bool.twoPane);
      boolean landscapeMode = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
      Step step = steps.get(currentStepIndex);
      if (step != null) {
        initializeBottomNavigation();
        if (step.getDescription() != null)
          descriptionTextView.setText(step.getDescription());
        if (step.getVideoUrl() != null && step.getVideoUrl().length() > 0) {
          initializeMediaSession();
          initializePlayer(Uri.parse(step.getVideoUrl()));
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
    return rootView;
  }

  private void hideStepDetails() {
    bottomNavigationView.setVisibility(View.GONE);
    stepDescriptionTitle.setVisibility(View.GONE);
    descriptionTextView.setVisibility(View.GONE);
  }

  private void initializeBottomNavigation() {
    currentStepTextView.setText(getContext().getString(R.string.currentStepText, currentStepIndex + 1, steps.size()));
    if (currentStepIndex == 0) {
      previousStepImageView.setVisibility(View.INVISIBLE);
    } else {
      previousStepImageView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        }
      });
    }
    if (currentStepIndex == steps.size() - 1) {
      nextStepImageView.setVisibility(View.INVISIBLE);
    } else {
      nextStepImageView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        }
      });
    }
  }

  public void setRecipeSteps(List<Step> steps) {
    this.steps = new ArrayList<>(steps);
  }

  public void setCurrentStepIndex(int index) {
    this.currentStepIndex = index;
  }

  private void initializePlayer(Uri mediaUri) {
    if (player == null) {
      TrackSelector trackSelector = new DefaultTrackSelector();
      LoadControl loadControl = new DefaultLoadControl();
      player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
      playerView.setPlayer(player);
      player.addListener(this);
      player.setPlayWhenReady(true);
      player.seekTo(playbackPosition);
    }
    String userAgent = Util.getUserAgent(getContext(), getContext().getString(R.string.app_name));
    MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(getContext(), userAgent),
        new DefaultExtractorsFactory(), null, null);
    player.prepare(mediaSource, true, false);
  }

  private void initializeMediaSession() {
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

  private void releasePlayer() {
    if (player != null) {
      playbackPosition = player.getCurrentPosition();
      player.stop();
      player.release();
      player = null;
    }
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
    if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
      stateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, player.getCurrentPosition(), 1f);
    } else if ((playbackState == ExoPlayer.STATE_READY)) {
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
