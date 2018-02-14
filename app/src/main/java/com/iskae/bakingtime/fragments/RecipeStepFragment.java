package com.iskae.bakingtime.fragments;

import android.content.*;
import android.content.res.*;
import android.net.*;
import android.os.*;
import android.support.annotation.*;
import android.support.constraint.*;
import android.support.v4.app.*;
import android.support.v4.media.session.*;
import android.view.*;
import android.widget.*;

import com.google.android.exoplayer2.*;
import com.google.android.exoplayer2.extractor.*;
import com.google.android.exoplayer2.source.*;
import com.google.android.exoplayer2.trackselection.*;
import com.google.android.exoplayer2.ui.*;
import com.google.android.exoplayer2.upstream.*;
import com.google.android.exoplayer2.util.*;
import com.iskae.bakingtime.R;
import com.iskae.bakingtime.listeners.*;
import com.iskae.bakingtime.data.model.*;

import java.util.*;

import butterknife.*;

/**
 * Created by iskae on 08.02.18.
 */

public class RecipeStepFragment extends Fragment implements ExoPlayer.EventListener {
    private static final String TAG = RecipeStepFragment.class.getSimpleName();

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
    private OnStepClickListener listener;
    private SimpleExoPlayer player;
    private MediaSessionCompat mediaSession;
    private PlaybackStateCompat.Builder stateBuilder;
    private ArrayList<Step> steps;
    private int currentStepIndex;
    private long playbackPosition;

    public RecipeStepFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnNavigationClickListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            playbackPosition = savedInstanceState.getLong(PLAYER_CURRENT_POSITION);
            steps = savedInstanceState.getParcelableArrayList(STEPS);
            currentStepIndex = savedInstanceState.getInt(CURRENT_STEP);
        }
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
                    if (listener != null) {
                        listener.onStepClick(--currentStepIndex);
                    }
                }
            });
        }
        if (currentStepIndex == steps.size() - 1) {
            nextStepImageView.setVisibility(View.INVISIBLE);
        } else {
            nextStepImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onStepClick(++currentStepIndex);
                    }
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
    public void onSaveInstanceState(Bundle outState) {
        outState.putLong(PLAYER_CURRENT_POSITION, playbackPosition);
        outState.putParcelableArrayList(STEPS, steps);
        outState.putInt(CURRENT_STEP, currentStepIndex);
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
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

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
