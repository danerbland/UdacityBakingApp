package utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import model.Ingredient;
import model.Recipe;
import model.Step;

public class OpenRecipeJsonUtils {

    //Dish Level
    public static final String JSON_ID_KEY = "id";
    public static final String JSON_NAME_KEY = "name";
    public static final String JSON_INGREDIENTS_KEY = "ingredients";
    public static final String JSON_STEPS_KEY = "steps";
    public static final String JSON_SERVINGS_KEY = "servings";
    public static final String JSON_IMAGE_KEY = "image";

    //Ingredients Level
    public static final String JSON_QUANTITY_KEY = "quantity";
    public static final String JSON_MEASURE_KEY = "measure";
    public static final String JSON_INGREDIENT_KEY = "ingredient";

    //Steps Level
    public static final String JSON_SHORT_DESCRIPTION_KEY = "shortDescription";
    public static final String JSON_DESCRIPTION_KEY = "description";
    public static final String JSON_VIDEO_URL = "videoURL";
    public static final String JSON_THUMBNAIL_URL = "thumbnailURL";
    private static final Object TAG = OpenRecipeJsonUtils.class.getSimpleName();

    public static ArrayList<Recipe> getRecipesFromJSON (String jsonString){

        try {
            JSONArray recipesJSONArray = new JSONArray(jsonString);

            //Check for null data
            if(recipesJSONArray == null|| recipesJSONArray.length() == 0){
                return null;
            }

            //create recipe array list to return.
            ArrayList<Recipe> RecipeList = new ArrayList<>();

            //iterate through our JSONArray and add recipes to our recipelist.
            for(int i = 0; i < recipesJSONArray.length(); i++){
                JSONObject recipeJSONObject = recipesJSONArray.getJSONObject(i);
                Recipe recipe = new Recipe(
                        recipeJSONObject.getInt(JSON_ID_KEY),
                        recipeJSONObject.getString(JSON_NAME_KEY),
                        recipeJSONObject.getInt(JSON_SERVINGS_KEY),
                        recipeJSONObject.getString(JSON_IMAGE_KEY),
                        getIngredientListFromJSONArray(recipeJSONObject.getJSONArray(JSON_INGREDIENTS_KEY)),
                        getStepListFromJSON(recipeJSONObject.getJSONArray(JSON_STEPS_KEY))
                );
                RecipeList.add(recipe);
            }

            return RecipeList;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    //takes a JSONArray of ingredients and returns an arraylist
    public static ArrayList<Ingredient> getIngredientListFromJSONArray (JSONArray IngredientArray){
        try {
            ArrayList ingredientList = new ArrayList<Ingredient>();
            for (int i = 0; i < IngredientArray.length(); i++) {
                JSONObject ingredientObject = IngredientArray.getJSONObject(i);
                Ingredient ingredient = new Ingredient(
                        ingredientObject.getInt(JSON_QUANTITY_KEY),
                        ingredientObject.getString(JSON_MEASURE_KEY),
                        ingredientObject.getString(JSON_INGREDIENT_KEY)
                );
                ingredientList.add(ingredient);
            }
            return ingredientList;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }



    public static ArrayList<Step> getStepListFromJSON (JSONArray StepArray){
        try {
            ArrayList stepList = new ArrayList<Step>();
            for (int i = 0; i < StepArray.length(); i++) {
                JSONObject stepObject = StepArray.getJSONObject(i);
                Step step = new Step(
                        stepObject.getInt(JSON_ID_KEY),
                        stepObject.getString(JSON_SHORT_DESCRIPTION_KEY),
                        stepObject.getString(JSON_DESCRIPTION_KEY),
                        stepObject.getString(JSON_VIDEO_URL),
                        stepObject.getString(JSON_THUMBNAIL_URL)
                );
                stepList.add(step);
            }
            return stepList;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<Ingredient> getIngredientListFromRecipeEntry (JSONArray IngredientArray){
        try {
            ArrayList ingredientList = new ArrayList<Ingredient>();
            for (int i = 0; i < IngredientArray.length(); i++) {
                JSONObject ingredientObject = IngredientArray.getJSONObject(i);
                Ingredient ingredient = new Ingredient(
                        ingredientObject.getInt("mQuantity"),
                        ingredientObject.getString("mMeasure"),
                        ingredientObject.getString("mIngredientName")
                );
                ingredientList.add(ingredient);
            }
            return ingredientList;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static ArrayList<Step> getStepListFromRecipeEntry (JSONArray StepArray){
        try {
            ArrayList stepList = new ArrayList<Step>();
            for (int i = 0; i < StepArray.length(); i++) {
                JSONObject stepObject = StepArray.getJSONObject(i);
                Step step = new Step(
                        stepObject.getInt("mId"),
                        stepObject.getString("mShortDescription"),
                        stepObject.getString("mDescription"),
                        stepObject.getString("mVideoUrl"),
                        stepObject.getString("mThumbnailUrl")
                );
                stepList.add(step);
            }
            return stepList;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}
