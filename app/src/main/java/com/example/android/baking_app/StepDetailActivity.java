package com.example.android.baking_app;

import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.databinding.DataBindingUtil;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

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

import model.Recipe;
import model.Step;

public class StepDetailActivity extends AppCompatActivity implements ExoPlayer.EventListener{

    private static final String TAG = StepDetailActivity.class.getSimpleName();
    private static Recipe mRecipe;
    private static int mCurrentStepId;
    private static Step mStep;
    private SimpleExoPlayer mSimpleExoPlayer;
    private ActivityStepDetailBinding mBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_step_detail);

        if(getIntent().hasExtra(getString(R.string.step_extra_key))){
            Bundle bundle = getIntent().getExtras();
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
        if(mCurrentStepId == mRecipe.getmStepsList().size()-1){
            mBinding.buttonNextStep.setVisibility(View.GONE);
        }
        if(mCurrentStepId == 0){
            mBinding.buttonPreviousStep.setVisibility(View.GONE);
        }


        //Handle the ExoPlayerView.  Hide it if there is no video.
        if(mStep.getmVideoUrl() != null && !mStep.getmVideoUrl().equalsIgnoreCase("")) {
            //Initialize the ExoPlayer
            Uri videoUri = Uri.parse(mStep.getmVideoUrl());
            initializePlayer(videoUri);
        } else{
            mBinding.simpleExoPlayerViewStep.setVisibility(View.GONE);
        }



        //Set the Description Text
        mBinding.textviewStepDescription.setText(mStep.getmDescription());

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mSimpleExoPlayer!=null) {
            releasePlayer();
        }
    }

    private void initializePlayer(Uri mediaUri) {
        if (mSimpleExoPlayer == null) {
            Log.e(TAG, "initializePlayer with URL: " + mStep.getmVideoUrl());

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
            mSimpleExoPlayer.setPlayWhenReady(true);
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
}
