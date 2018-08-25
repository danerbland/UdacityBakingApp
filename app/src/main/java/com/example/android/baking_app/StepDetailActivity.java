package com.example.android.baking_app;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.databinding.DataBindingUtil;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.android.baking_app.databinding.ActivityStepDetailBinding;
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

public class StepDetailActivity extends AppCompatActivity implements ExoPlayer.EventListener{

    private static final String TAG = StepDetailActivity.class.getSimpleName();
    private static Recipe mRecipe;
    private static int mCurrentStepId;
    private static Step mStep;
    private static String mThumbnailUrl;
    private static SimpleExoPlayer mSimpleExoPlayer;
    private ActivityStepDetailBinding mBinding;

    private Long mPlaybackPositon = Long.valueOf(0);
    private boolean mPlaybackState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_step_detail);

        if(getIntent().hasExtra(getString(R.string.step_extra_key))){
            Bundle bundle = getIntent().getExtras();
            //noinspection ConstantConditions
            mStep = bundle.getParcelable(getString(R.string.step_extra_key));
        } else {
            mStep = null;
        }

        //Set the screen title
        getSupportActionBar().setTitle(mStep.getmShortDescription());

        //Assign our mRecipe and mCurrentStepId member variables, and hide Next/Prev buttons as needed
        if(getIntent().hasExtra(getString(R.string.recipe_extra_key)) &&
                getIntent().hasExtra(getString(R.string.step_id_extra_key))){
            mRecipe = getIntent().getExtras().getParcelable(getString(R.string.recipe_extra_key));
            mCurrentStepId = getIntent().getExtras().getInt(getString(R.string.step_id_extra_key));
        }


        //Hide Next button if the list doesn't have a next step.
        //TODO figure out a way to handle bad json here - Yellowcake example.
        try{
            mRecipe.getmStepsList().get(mCurrentStepId+1);
        } catch(Exception e){
            mBinding.buttonPreviousStep.setVisibility(View.GONE);
        }

        if(mCurrentStepId == 0){
            mBinding.buttonPreviousStep.setVisibility(View.GONE);
        }


        //Handle the ExoPlayerView.  Hide it if there is no video.
        if(mStep.getmVideoUrl() != null && !mStep.getmVideoUrl().equalsIgnoreCase("")) {
            //Initialize the ExoPlayer
            Uri videoUri = Uri.parse(mStep.getmVideoUrl());
            //if the savedinstancestate is not null, seek to the previous position of the player
            if(savedInstanceState != null){
                mPlaybackPositon = savedInstanceState.getLong(getString(R.string.exoplayer_position_instance_state_key));
                mPlaybackState = savedInstanceState.getBoolean(getString(R.string.exoplayer_play_state_instance_state_key));
                //Log.e(TAG, Boolean.toString(mPlaybackState));

            }
            initializePlayer(videoUri, mPlaybackPositon, mPlaybackState);
        } else{
            mBinding.simpleExoPlayerViewStep.setVisibility(View.GONE);
        }

        if(mStep.getmThumbnailUrl() != null && !mStep.getmThumbnailUrl().equalsIgnoreCase("")){
            Picasso.with(this).load(mStep.getmThumbnailUrl()).into(mBinding.imageviewStepThumbnail);
        } else{
            mBinding.imageviewStepThumbnail.setVisibility(View.GONE);
        }

        //Set the Description Text
        mBinding.textviewStepDescription.setText(mStep.getmDescription());

    }

    @Override
    public void onResume(){
        Uri videoUri = Uri.parse(mStep.getmVideoUrl());
        initializePlayer(videoUri, mPlaybackPositon, mPlaybackState);
        super.onResume();
    }

    @Override
    public void onPause(){
        if(Util.SDK_INT <=23 && mSimpleExoPlayer != null){
            mPlaybackState = mSimpleExoPlayer.getPlayWhenReady();
            mPlaybackPositon = mSimpleExoPlayer.getCurrentPosition();
            releasePlayer();
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        if(Util.SDK_INT >23 && mSimpleExoPlayer!= null){
            mPlaybackState = mSimpleExoPlayer.getPlayWhenReady();
            mPlaybackPositon = mSimpleExoPlayer.getCurrentPosition();
            releasePlayer();
        }
        super.onStop();
    }

    private void initializePlayer(Uri mediaUri, Long position, Boolean playWhenReady) {
        if (mSimpleExoPlayer == null) {

            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mSimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
            mBinding.simpleExoPlayerViewStep.setPlayer(mSimpleExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            mSimpleExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(this, "BakingApp");
            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                    this, userAgent, bandwidthMeter);
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, dataSourceFactory
                    , new DefaultExtractorsFactory(), null, null);
            mSimpleExoPlayer.prepare(mediaSource);
            mSimpleExoPlayer.seekTo(position);
            mSimpleExoPlayer.setPlayWhenReady(playWhenReady);
        }
    }

    private void releasePlayer() {
        mSimpleExoPlayer.stop();
        mSimpleExoPlayer.release();
        mSimpleExoPlayer = null;
    }

    //If the "Next" or "Prev" button is clicked, open the next or previous step.
    public void onStepChangeButtonClick(View view){
        if(view == findViewById(R.id.button_next_step)){
            Intent intentToStartStepActivity = new Intent(this, StepDetailActivity.class);
            intentToStartStepActivity.putExtra(getString(R.string.step_extra_key), mRecipe.getmStepsList().get(mCurrentStepId + 1));
            intentToStartStepActivity.putExtra(getString(R.string.recipe_extra_key), mRecipe);
            intentToStartStepActivity.putExtra(getString(R.string.step_id_extra_key), mCurrentStepId + 1);
            startActivity(intentToStartStepActivity);

        }
        if(view == findViewById(R.id.button_previous_step)){
            Intent intentToStartStepActivity = new Intent(this, StepDetailActivity.class);
            intentToStartStepActivity.putExtra(getString(R.string.step_extra_key), mRecipe.getmStepsList().get(mCurrentStepId - 1));
            intentToStartStepActivity.putExtra(getString(R.string.recipe_extra_key), mRecipe);
            intentToStartStepActivity.putExtra(getString(R.string.step_id_extra_key), mCurrentStepId - 1);
            startActivity(intentToStartStepActivity);
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

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        Log.e(TAG, error.toString());
    }

    @Override
    public void onPositionDiscontinuity() {

    }

    //Override the back button to return to the recipe screen
    @Override
    public void onBackPressed() {
        Intent intentToStartRecipeActivity = new Intent(this, RecipeDetailActivity.class);
        intentToStartRecipeActivity.putExtra(getString(R.string.recipe_extra_key), mRecipe);
        startActivity(intentToStartRecipeActivity);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home: {
                Intent intentToStartRecipeDetailActivity = new Intent(this, RecipeDetailActivity.class);
                intentToStartRecipeDetailActivity.putExtra(getString(R.string.recipe_extra_key), mRecipe);
                startActivity(intentToStartRecipeDetailActivity);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Save the player instance state. Credit for assist to:
    //https://stackoverflow.com/questions/45481775/exoplayer-restore-state-when-resumed
    @Override
    public void onSaveInstanceState(Bundle state){
        if(mSimpleExoPlayer != null) {
            state.putLong(getString(R.string.exoplayer_position_instance_state_key), mSimpleExoPlayer.getCurrentPosition());
            boolean playstate = mSimpleExoPlayer.getPlayWhenReady();
            state.putBoolean(getString(R.string.exoplayer_play_state_instance_state_key), playstate);
            super.onSaveInstanceState(state);
        }
    }

}



