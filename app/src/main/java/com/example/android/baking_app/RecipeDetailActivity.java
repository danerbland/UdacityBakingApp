package com.example.android.baking_app;

import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import model.Recipe;

public class RecipeDetailActivity extends AppCompatActivity implements StepsListAdapter.StepOnClickHandler{

    private static final String TAG = RecipeDetailActivity.class.getSimpleName();
    private static Recipe mRecipe;
    private IngredientListAdapter mIngredientListAdapter;
    private StepsListAdapter mStepListAdapter;
    private RecyclerView mIngredientsRecyclerView;
    private RecyclerView mStepRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        //Get Recipe Extra from intent.
        if(getIntent().hasExtra(getString(R.string.recipe_extra_key))) {
            mRecipe = getIntent().getExtras().getParcelable(getString(R.string.recipe_extra_key));
            Log.e(TAG, mRecipe.getmName());
        }

        mIngredientsRecyclerView = findViewById(R.id.recyclerview_ingredients_list);
        mStepRecyclerView = findViewById(R.id.recyclerview_steps_list);

        //Set the Action Bar title to the recipe name
        getSupportActionBar().setTitle(mRecipe.getmName());


        //Initialize our Ingredients RecyclerView

        LinearLayoutManager ingredientLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mIngredientsRecyclerView.setLayoutManager(ingredientLayoutManager);
        mIngredientListAdapter = new IngredientListAdapter(this, mRecipe.getmIngredientsList());
        mIngredientsRecyclerView.setAdapter(mIngredientListAdapter);

        //Initialize our Steps RecyclerView

        LinearLayoutManager stepLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mStepRecyclerView.setLayoutManager(stepLayoutManager);
        mStepListAdapter = new StepsListAdapter(this, this);
        mStepListAdapter.setmStepArrayList(mRecipe.getmStepsList());
        mStepRecyclerView.setAdapter(mStepListAdapter);


    }

    @Override
    public void onClick(int index) {
        Intent intentToStartStepActivity = new Intent(RecipeDetailActivity.this, StepDetailActivity.class);
        intentToStartStepActivity.putExtra(getString(R.string.step_extra_key), mRecipe.getmStepsList().get(index));
        intentToStartStepActivity.putExtra(getString(R.string.recipe_extra_key), mRecipe);
        intentToStartStepActivity.putExtra(getString(R.string.step_id_extra_key), index);
        startActivity(intentToStartStepActivity);
    }

    @Override
    public void onBackPressed() {
        Intent intentToStartMainActivity = new Intent(this, MainActivity.class);
        startActivity(intentToStartMainActivity);
    }
}
