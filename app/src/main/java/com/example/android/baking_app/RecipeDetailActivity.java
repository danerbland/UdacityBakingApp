package com.example.android.baking_app;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import model.Recipe;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeDetailFragment.onStepClickListener{

    private static final String TAG = RecipeDetailActivity.class.getSimpleName();
    private static Recipe mRecipe;
    private IngredientListAdapter mIngredientListAdapter;
    private StepsListAdapter mStepListAdapter;
    private RecyclerView mIngredientsRecyclerView;
    private RecyclerView mStepRecyclerView;
    private Boolean mTwoPane;
    private int mStepId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        //Instantiate mRecipe for use.
        if(getIntent().hasExtra(getString(R.string.recipe_extra_key))) {
            mRecipe = getIntent().getExtras().getParcelable(getString(R.string.recipe_extra_key));
        }

        //Update the widget with Recipe Details.  Help from:
        //https://stackoverflow.com/questions/3455123/programmatically-update-widget-from-activity-service-receiver
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingWidgetProvider.class));
        BakingWidgetProvider.updateBakingWidgets(this, appWidgetManager, mRecipe, appWidgetIds);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_ingredients_list);



        FragmentManager fragmentManager = getSupportFragmentManager();
        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
        recipeDetailFragment.setmStepClickListener(this);
        fragmentManager.beginTransaction()
                .add(R.id.recipe_detail_fragment, recipeDetailFragment)
        .commit();

        //Check if we are two-pane
        if(findViewById(R.id.step_detail_fragment) != null){
            mTwoPane = true;
            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setmRecipe(mRecipe);

            //Load the step from the savedInstanceState if it is not null. If it is null, initialize to first step.
            if(savedInstanceState!=null){
                mStepId = savedInstanceState.getInt(getString(R.string.recipe_detail_saved_instance_state_step_id_key), 0);
                stepDetailFragment.setmStep(mRecipe.getmStepsList().get(mStepId));
                fragmentManager.beginTransaction()
                        .add(R.id.step_detail_fragment, stepDetailFragment)
                        .commit();
            } else {
                stepDetailFragment.setmStep(mRecipe.getmStepsList().get(0));
                mStepId = 0;
                fragmentManager.beginTransaction()
                        .add(R.id.step_detail_fragment, stepDetailFragment)
                        .commit();
            }

        } else {
            mTwoPane = false;
        }

        //Set the Action Bar title to the recipe name
        getSupportActionBar().setTitle(mRecipe.getmName());


    }

    @Override
    public void onBackPressed() {
        Intent intentToStartMainActivity = new Intent(this, MainActivity.class);
        startActivity(intentToStartMainActivity);
    }

    @Override
    public void onStepSelected(int stepIndex) {
        if(!mTwoPane){
            Intent intentToStartStepActivity = new Intent(RecipeDetailActivity.this, StepDetailActivity.class);
            intentToStartStepActivity.putExtra(getString(R.string.step_extra_key), mRecipe.getmStepsList().get(stepIndex));
            intentToStartStepActivity.putExtra(getString(R.string.recipe_extra_key), mRecipe);
            intentToStartStepActivity.putExtra(getString(R.string.step_id_extra_key), stepIndex);
            startActivity(intentToStartStepActivity);
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setmStep(mRecipe.getmStepsList().get(stepIndex));
            mStepId=stepIndex;
            fragmentManager.beginTransaction()
                    .replace(R.id.step_detail_fragment, stepDetailFragment)
                    .commit();
        }
    }

    //If the "Next" or "Prev" button is clicked, open the next or previous step.
    public void onStepChangeButtonClick(View view){
        if(view == findViewById(R.id.button_next_step)){
            FragmentManager fragmentManager = getSupportFragmentManager();
            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setmStep(mRecipe.getmStepsList().get(mStepId + 1));
            mStepId += 1;
            fragmentManager.beginTransaction()
                    .replace(R.id.step_detail_fragment, stepDetailFragment)
                    .commit();

        }
        if(view == findViewById(R.id.button_previous_step)){
            FragmentManager fragmentManager = getSupportFragmentManager();
            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setmStep(mRecipe.getmStepsList().get(mStepId - 1));
            mStepId -= 1;
            fragmentManager.beginTransaction()
                    .replace(R.id.step_detail_fragment, stepDetailFragment)
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState (Bundle outstate){
        if(mTwoPane){
            outstate.putInt(getString(R.string.recipe_detail_saved_instance_state_step_id_key), mStepId);
        }
        super.onSaveInstanceState(outstate);
    }

}
