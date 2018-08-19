package data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.ArrayList;

import model.Ingredient;
import model.Step;

@Entity(tableName = "recipe")
public class RecipeEntry {


    @PrimaryKey
    private int id;
    private String name;
    private int servings;
    private ArrayList<Ingredient> ingredients;
    private ArrayList<Step> steps;

    public RecipeEntry(int id, String name, int servings, ArrayList<Ingredient> ingredients, ArrayList<Step> steps){
        this.id = id;
        this.name = name;
        this.servings = servings;
        this.ingredients = ingredients;
        this.steps = steps;
    }



    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getServings() {
        return servings;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void setSteps(ArrayList<Step> steps) {
        this.steps = steps;
    }
}




















