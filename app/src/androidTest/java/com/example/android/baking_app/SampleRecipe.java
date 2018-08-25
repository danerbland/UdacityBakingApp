package com.example.android.baking_app;

import java.util.ArrayList;

import model.Ingredient;
import model.Recipe;
import model.Step;

public class SampleRecipe {

    private static Recipe mSampleRecipe;

    SampleRecipe(){
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient(0, "sample-measure", "sample-ingredient-name"));
        ArrayList<Step> steps = new ArrayList<>();
        steps.add(new Step(0, "sample-short-description", "sample-description", "", ""));
        mSampleRecipe = new Recipe(0, "sample-recipe", 0, "", ingredients, steps);
    }


    public static Recipe getmSampleRecipe() {
        return mSampleRecipe;
    }
}
