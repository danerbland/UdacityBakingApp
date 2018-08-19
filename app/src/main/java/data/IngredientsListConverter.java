package data;

import android.arch.persistence.room.TypeConverter;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import model.Ingredient;
import utils.OpenRecipeJsonUtils;

public class IngredientsListConverter {
    private static final String TAG = IngredientsListConverter.class.getSimpleName();

    @TypeConverter
    public static ArrayList<Ingredient> toArrayList(String arrayListJsonString){
        try {
            JSONArray array = new JSONArray(arrayListJsonString);
            return OpenRecipeJsonUtils.getIngredientListFromRecipeEntry(array);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    //With help from:
    //https://stackoverflow.com/questions/18857884/how-to-convert-arraylist-of-custom-class-to-jsonarray-in-java/18858018
    @TypeConverter
    public static String toString(ArrayList<Ingredient> ingredients){
        JSONArray jsonArray = new JSONArray();
        Gson gson = new Gson();
        JsonElement element = gson.toJsonTree(ingredients, new TypeToken<List<Ingredient>>(){}.getType());
        return element.getAsJsonArray().toString();
    }

}








