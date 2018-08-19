package com.example.android.baking_app;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class StepDetailFragment extends Fragment implements ExoPlayer.EventListener {

    private static final String TAG = StepDetailActivity.class.getSimpleName();
    private static Recipe mRecipe;
    private static int mCurrentStepId;
    private static Step mStep;
    private SimpleExoPlayer mSimpleExoPlayer;
    private Button mNextStepButton;
    private Button mPrevStepButton;
    private TextView mDescriptionTextView;
    private SimpleExoPlayerView mSimpleExoPlayerView;

    public StepDetailFragment(){};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup conatiner, Bundle savedInstanceSate) {
        final View rootView = inflater.inflate(R.layout.step_detail_fragment, conatiner, false);

        mNextStepButton = rootView.findViewById(R.id.button_next_step);
        mPrevStepButton = rootView.findViewById(R.id.button_previous_step);
        mDescriptionTextView = rootView.findViewById(R.id.textview_step_description);
        mSimpleExoPlayerView = rootView.findViewById(R.id.simple_exo_player_view_step);


        //If the activity has a recipe extra, it was started as an activity.
        if (getActivity().getIntent().hasExtra(getString(R.string.recipe_extra_key)) &&
                getActivity().getIntent().hasExtra(getString(R.string.step_id_extra_key))) {
            mRecipe = getActivity().getIntent().getExtras().getParcelable(getString(R.string.recipe_extra_key));
            mCurrentStepId = getActivity().getIntent().getExtras().getInt(getString(R.string.step_id_extra_key));
        }

        if (mCurrentStepId == mRecipe.getmStepsList().size() - 1) {
            mNextStepButton.setVisibility(View.GONE);
        }
        if (mCurrentStepId == 0) {
            mPrevStepButton.setVisibility(View.GONE);
            Log.e(TAG, "mBinding is working properly here");
        }



        //Handle the ExoPlayerView.  Hide it if there is no video.
        if(mStep.getmVideoUrl() != null && !mStep.getmVideoUrl().equalsIgnoreCase("")) {
            //Initialize the ExoPlayer
            Log.e(TAG, mStep.getmVideoUrl());
            Uri videoUri = Uri.parse(mStep.getmVideoUrl());
            initializePlayer(videoUri);
        } else{
            mSimpleExoPlayerView.setVisibility(View.GONE);
        }



        //Set the Description Text
        mDescriptionTextView.setText(mStep.getmDescription());

        return rootView;
    }


    //Release the ExoPlayer
    @Override
    public void onDestroyView(){
        super.onDestroyView();
        if(mSimpleExoPlayer!=null) {
            releasePlayer();
        }
    }

    //Method for initializing the player
    private void initializePlayer(Uri mediaUri) {
        if (mSimpleExoPlayer == null) {
            Log.e(TAG, "initializePlayer with URL: " + mStep.getmVideoUrl());

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
            mSimpleExoPlayer.prepare(mediaSource);
            mSimpleExoPlayer.setPlayWhenReady(true);
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
        Log.e(TAG, "setmStep. mCurrentStepId: " + String.valueOf(mCurrentStepId));
    }

    public void setmRecipe(Recipe recipe){
        mRecipe = recipe;
    }

    public int getmCurrentStepId(){
        return mCurrentStepId;
    }

}
