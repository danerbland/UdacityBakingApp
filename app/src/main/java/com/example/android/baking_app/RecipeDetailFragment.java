package com.example.android.baking_app;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import model.Recipe;

public class RecipeDetailFragment extends Fragment implements StepsListAdapter.StepOnClickHandler{

    private static final String TAG = RecipeDetailFragment.class.getSimpleName();
    private static IngredientListAdapter mIngredientListAdapter;
    private static StepsListAdapter mStepListAdapter;
    private Recipe mRecipe;
    private onStepClickListener mStepClickListener;

    public interface onStepClickListener{
        void onStepSelected(int stepIndex);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
    }

    public RecipeDetailFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSate){
        final View rootView = inflater.inflate(R.layout.recipe_detail_fragment, container, false);

        RecyclerView mIngredientsRecyclerView = rootView.findViewById(R.id.recyclerview_ingredients_list);
        RecyclerView mStepRecyclerView = rootView.findViewById(R.id.recyclerview_steps_list);

        if(getActivity().getIntent().hasExtra(getString(R.string.recipe_extra_key))) {
            mRecipe = getActivity().getIntent().getExtras().getParcelable(getString(R.string.recipe_extra_key));
        }

        //Initialize our Ingredients RecyclerView

        LinearLayoutManager ingredientLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mIngredientsRecyclerView.setLayoutManager(ingredientLayoutManager);
        mIngredientListAdapter = new IngredientListAdapter(getActivity(), mRecipe.getmIngredientsList());
        mIngredientsRecyclerView.setAdapter(mIngredientListAdapter);

        //Initialize our Steps RecyclerView

        LinearLayoutManager stepLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mStepRecyclerView.setLayoutManager(stepLayoutManager);
        mStepListAdapter = new StepsListAdapter(getActivity(), this);
        mStepListAdapter.setmStepArrayList(mRecipe.getmStepsList());
        mStepRecyclerView.setAdapter(mStepListAdapter);

        return rootView;
    }


    @Override
    public void onClick(int index) {
        mStepClickListener.onStepSelected(index);
    }

    public void setmStepClickListener(onStepClickListener listener){
        mStepClickListener = listener;
    }
}
