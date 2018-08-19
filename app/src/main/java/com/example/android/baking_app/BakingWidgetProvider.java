package com.example.android.baking_app;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.example.android.baking_app.MainActivity;
import com.example.android.baking_app.R;
import com.example.android.baking_app.RecipeDetailActivity;

import java.util.ArrayList;

import model.Ingredient;
import model.Recipe;

import static android.support.constraint.Constraints.TAG;

public class BakingWidgetProvider extends AppWidgetProvider{


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                Recipe recipe, int appWidgetId) {

        Log.e(TAG, "updateAppWidget w/ recipe called");

        //Crete intent
        Intent intent;
        if(recipe == null){
            intent = new Intent(context, MainActivity.class);
        } else {
            Log.e(TAG, "pendingintent created");
            intent = new Intent(context, RecipeDetailActivity.class);
            intent.putExtra(context.getString(R.string.recipe_extra_key), recipe);
        }
        //Wrap intent in pendingintent
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Find RemoteViews
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_widget);
        if(recipe!=null){
            String ingredientsList = recipe.getmName() + " Ingredients:\n\n";
            ArrayList<Ingredient> ingredients = recipe.getmIngredientsList();
            for(int i = 0; i < ingredients.size(); i++){
                Ingredient ingredient = ingredients.get(i);
                ingredientsList += ingredient.getmQuantity() + "\t" + ingredient.getmMeasure() + "\t" + ingredient.getmIngredientName() + "\n";
            }
            views.setTextViewText(R.id.widget_ingredients_list, ingredientsList);
        } else {
            views.setTextViewText(R.id.widget_ingredients_list, "No Recipe Selected");
        }

        views.setOnClickPendingIntent(R.id.widget_ingredients_list, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        updateBakingWidgets(context, appWidgetManager, null, appWidgetIds);
    }

    public static void updateBakingWidgets(Context context, AppWidgetManager appWidgetManager,
                                          Recipe recipe, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, recipe, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // Perform any action when one or more AppWidget instances have been deleted
    }

    @Override
    public void onEnabled(Context context) {
        // Perform any action when an AppWidget for this provider is instantiated
    }

    @Override
    public void onDisabled(Context context) {
        // Perform any action when the last AppWidget instance for this provider is deleted
    }


}
