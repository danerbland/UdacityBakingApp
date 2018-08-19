package com.example.android.baking_app;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import SimpleIdlingResource.SimpleIdlingResource;
import data.AppDatabase;
import data.RecipeEntry;
import model.Recipe;
import utils.NetworkUtils;
import utils.OpenRecipeJsonUtils;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<Recipe>>,
        RecipeMenuAdapter.RecipeClickHandler{

    private static final int RECIPE_LOADER_ID = 33;
    private ArrayList<Recipe> mRecipeArrayList;

    //Instantiate Database
    private AppDatabase mDb;

    //Handle Idling Resource
    // The Idling Resource which will be null in production.
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }


    private static final String TAG = MainActivity.class.getSimpleName();
    GridView mRecipeGridview;
    RecipeMenuAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getIdlingResource();

        //Initialize the Database
        mDb = AppDatabase.getInstance(getApplicationContext());

        mAdapter = new RecipeMenuAdapter(this, this);

        LoaderManager.LoaderCallbacks<ArrayList<Recipe>> callback = MainActivity.this;
        getSupportLoaderManager().initLoader(RECIPE_LOADER_ID, null, callback);

        mRecipeGridview = (GridView) findViewById(R.id.gridview_recipe_list);


        mRecipeGridview.setNumColumns(GridView.AUTO_FIT);
        mRecipeGridview.setAdapter(mAdapter);



    }

    @NonNull
    @Override
    public Loader<ArrayList<Recipe>> onCreateLoader(int id, @Nullable Bundle args) {

        return new AsyncTaskLoader<ArrayList<Recipe>>(this) {

            @Override
            protected void onStartLoading(){
                if (mIdlingResource != null) {
                    mIdlingResource.setIdleState(false);
                }
                forceLoad();
            }

            @Nullable
            @Override
            public ArrayList<Recipe> loadInBackground() {
                URL jsonURL = NetworkUtils.buildGenericJSONURL(getApplicationContext());
                if(jsonURL != null){
                    try{
                        String jsonRespose = NetworkUtils.getResponseFromHttpUrl(jsonURL);
                        //TODO - sync Database with json response?
                        //addRecipesToDatabase(jsonRespose);
                        return OpenRecipeJsonUtils.getRecipesFromJSON(jsonRespose);

                    } catch (Exception e){
                        e.printStackTrace();
                        return null;
                    }
                }
                return null;
            }


        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Recipe>> loader, ArrayList<Recipe> data) {
        mRecipeArrayList = data;
        mAdapter.setmRecipeArrayList(data);
        mAdapter.notifyDataSetChanged();

        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(true);
        }

        if(data == null){
            Toast.makeText(this, "Unable to load data from network", Toast.LENGTH_SHORT);
            Log.e(TAG, "onLoadFinished: Null Data Returned");
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Recipe>> loader) {
        mAdapter.setmRecipeArrayList(null);
    }

    @Override
    public void onClick(int index) {
        Intent intentToStartDetailActivity = new Intent(MainActivity.this, RecipeDetailActivity.class);

        if(mRecipeArrayList != null)
        intentToStartDetailActivity.putExtra(getString(R.string.recipe_extra_key), mRecipeArrayList.get(index));
        startActivity(intentToStartDetailActivity);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }




    //TODO fix this so that the recipes are inserted correctly and don't fuck up.
    public void addRecipesToDatabase (String jsonResponse){
        ArrayList<Recipe> recipes = OpenRecipeJsonUtils.getRecipesFromJSON(jsonResponse);
        List<RecipeEntry> recipeEntries = mDb.RecipeDao().loadAllRecipes();

        for(Recipe recipe: recipes){
            RecipeEntry newRecipeEntry = new RecipeEntry(recipe.getmId(), recipe.getmName(),
                    recipe.getmServings(), recipe.getmIngredientsList(), recipe.getmStepsList());
            if(!recipeEntries.contains(newRecipeEntry)){
                Log.e(TAG, "inserted recipe to database: " + newRecipeEntry.getName());
                mDb.RecipeDao().insertRecipe(newRecipeEntry);
            }
        }
    }

}
