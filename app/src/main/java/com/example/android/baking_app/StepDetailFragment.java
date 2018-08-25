package com.example.android.baking_app;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
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
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import model.Recipe;
import model.Step;

public class StepDetailFragment extends Fragment implements ExoPlayer.EventListener {

    private static final String TAG = StepDetailFragment.class.getSimpleName();
    private static Recipe mRecipe;
    private static int mCurrentStepId;
    private static Step mStep;
    public static SimpleExoPlayer mSimpleExoPlayer;
    private Button mNextStepButton;
    private Button mPrevStepButton;
    private TextView mDescriptionTextView;
    private ImageView mThumbnailImageView;
    private SimpleExoPlayerView mSimpleExoPlayerView;

    private static boolean mPlaybackState = false;
    private static Long mPlaybackPosition = Long.valueOf(0);

    public StepDetailFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.step_detail_fragment, container, false);

        Log.e(TAG, "onCreateView Called");

        mNextStepButton = rootView.findViewById(R.id.button_next_step);
        mPrevStepButton = rootView.findViewById(R.id.button_previous_step);
        mDescriptionTextView = rootView.findViewById(R.id.textview_step_description);
        mThumbnailImageView = rootView.findViewById(R.id.imageview_step_thumbnail);
        mSimpleExoPlayerView = rootView.findViewById(R.id.simple_exo_player_view_step);


        //If the activity has a recipe extra, it was started as an activity.
        if (getActivity().getIntent().hasExtra(getString(R.string.recipe_extra_key)) &&
                getActivity().getIntent().hasExtra(getString(R.string.step_id_extra_key))) {
            mRecipe = getActivity().getIntent().getExtras().getParcelable(getString(R.string.recipe_extra_key));
            mCurrentStepId = getActivity().getIntent().getExtras().getInt(getString(R.string.step_id_extra_key));
        }

        //Hide Next button if the list doesn't have a next step.
        //TODO figure out a way to handle bad json here - Yellocake example.
        try{
            mRecipe.getmStepsList().get(mCurrentStepId + 1);
        } catch(Exception e){
            //Log.e(TAG, "hiding next button. Step: " + Integer.toString(mCurrentStepId));
            mNextStepButton.setVisibility(View.GONE);
        }

        if (mCurrentStepId == 0) {
            mPrevStepButton.setVisibility(View.GONE);
        }

        //Handle the ExoPlayerView.  Hide it if there is no video.
        if(mStep.getmVideoUrl() != null && !mStep.getmVideoUrl().equalsIgnoreCase("")) {
            //Initialize the ExoPlayer
            Uri videoUri = Uri.parse(mStep.getmVideoUrl());
            if(mSimpleExoPlayer == null && savedInstanceState != null){
                mPlaybackPosition = savedInstanceState.getLong(getString(R.string.exoplayer_position_instance_state_key));
                mPlaybackState = savedInstanceState.getBoolean(getString(R.string.exoplayer_play_state_instance_state_key));
                initializePlayer(videoUri, mPlaybackPosition, mPlaybackState);
            } else if(mSimpleExoPlayer == null){
                initializePlayer(videoUri, Long.valueOf(0), false);
            }
        } else{
            mSimpleExoPlayerView.setVisibility(View.GONE);
        }


        //Load the image thumbnail if available
        if(mStep.getmThumbnailUrl() != null && !mStep.getmThumbnailUrl().equalsIgnoreCase("")){
            Picasso.with(getActivity()).load(mStep.getmThumbnailUrl()).into(mThumbnailImageView);
        } else{
            mThumbnailImageView.setVisibility(View.GONE);
        }

        //Set the Description Text
        mDescriptionTextView.setText(mStep.getmDescription());

        return rootView;
    }


    //Method for initializing the player
    private void initializePlayer(Uri mediaUri, Long position, boolean playWhenReady) {
        //Log.e(TAG, "initializePlayer called");

        if (mSimpleExoPlayer == null) {

            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mSimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            mSimpleExoPlayerView.setPlayer(mSimpleExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            mSimpleExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getActivity(), "BakingApp");
            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                    getActivity(), userAgent, bandwidthMeter);
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, dataSourceFactory
                    , new DefaultExtractorsFactory(), null, null);
            mSimpleExoPlayer.setPlayWhenReady(playWhenReady);
            mSimpleExoPlayer.prepare(mediaSource);
            mSimpleExoPlayer.seekTo(mSimpleExoPlayer.getCurrentWindowIndex(), position);
            //Log.e(TAG, "Player initialized with playWhenReady: " + Boolean.toString(playWhenReady) +" and Position: " + Long.toString(position));
        }
    }

    private void releasePlayer() {
        mSimpleExoPlayer.stop();
        mSimpleExoPlayer.release();
        mSimpleExoPlayer = null;
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
        collectExoPlayerInfo();
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    public void setmStep(Step step){
        mStep = step;
        mCurrentStepId = step.getmId();
    }

    public void setmRecipe(Recipe recipe){
        mRecipe = recipe;
    }

    public int getmCurrentStepId(){
        return mCurrentStepId;
    }

    @Override
    public void onPause() {
        if(Util.SDK_INT <= 23 && mSimpleExoPlayer != null){
            collectExoPlayerInfo();
            releasePlayer();
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        if(Util.SDK_INT >23 && mSimpleExoPlayer!=null) {
            collectExoPlayerInfo();
            releasePlayer();
        }
        super.onStop();
    }

    //Save the player instance state. Credit for assist to:
    //https://stackoverflow.com/questions/45481775/exoplayer-restore-state-when-resumed
    @Override
    public void onSaveInstanceState(Bundle state){
        //Log.e(TAG, "onSaveInstanceState Called");
        if(mSimpleExoPlayer!=null) {
            state.putLong(getString(R.string.exoplayer_position_instance_state_key), mSimpleExoPlayer.getCurrentPosition());
            state.putBoolean(getString(R.string.exoplayer_play_state_instance_state_key), mSimpleExoPlayer.getPlayWhenReady());
        }
        super.onSaveInstanceState(state);
    }

    public static boolean getmPlaybackState() {
        return mPlaybackState;
    }

    public static Long getmPlaybackPosition() {
        return mPlaybackPosition;
    }

    public static void setmPlaybackState (boolean bool){
        mPlaybackState = bool;
    }

    public static void setmPlaybackPosition (long l){
        mPlaybackPosition = l;
    }

    public static void collectExoPlayerInfo(){
        if(mSimpleExoPlayer!=null) {
            //Log.e(TAG, "collectExoPlayerInfo called");
            mPlaybackState = mSimpleExoPlayer.getPlayWhenReady();
            mPlaybackPosition = mSimpleExoPlayer.getCurrentPosition();
        }
    }
}
