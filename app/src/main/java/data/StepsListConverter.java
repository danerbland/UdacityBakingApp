package data;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import model.Ingredient;
import model.Step;
import utils.OpenRecipeJsonUtils;

public class StepsListConverter {


    @TypeConverter
    public static ArrayList<Step> toArrayList(String arrayListJsonString){
        try {
            JSONArray array = new JSONArray(arrayListJsonString);
            return OpenRecipeJsonUtils.getStepListFromJSON(array);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    //With help from:
    //https://stackoverflow.com/questions/18857884/how-to-convert-arraylist-of-custom-class-to-jsonarray-in-java/18858018
    @TypeConverter
    public static String toString(ArrayList<Step> steps){
        JSONArray jsonArray = new JSONArray();
        Gson gson = new Gson();
        JsonElement element = gson.toJsonTree(steps, new TypeToken<List<Ingredient>>(){}.getType());
        return element.getAsJsonArray().toString();
    }

}
