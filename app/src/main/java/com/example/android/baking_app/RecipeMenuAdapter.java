package com.example.android.baking_app;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import model.Recipe;

public class RecipeMenuAdapter extends BaseAdapter{

    private static final String TAG = RecipeMenuAdapter.class.getSimpleName();
    private Context mContext;
    private ArrayList<Recipe> mRecipeArrayList;
    LayoutInflater mLayoutInflater;
    private RecipeClickHandler mClickHandler;

    public interface RecipeClickHandler {
        void onClick(int Index);
    }



    public RecipeMenuAdapter (Context context, RecipeClickHandler handler){
        mContext = context;
        mRecipeArrayList = null;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mClickHandler = handler;
    }


    @Override
    public int getCount() {
        if(mRecipeArrayList != null) {
            return mRecipeArrayList.size();
        }

        else {
            return 0;
        }
    }

    @Override
    public Object getItem(int i) {
        return 0;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        final int index = position;

        if(convertView == null){
            convertView = mLayoutInflater.inflate(R.layout.recipe_list_item, null);
        }

        TextView recipeTextView = convertView.findViewById(R.id.textview_recipe_list_item_name);
        ImageView recipeImageView = convertView.findViewById(R.id.imageview_recipe_list_item_image);

        //Set the text
        recipeTextView.setText(mRecipeArrayList.get(position).getmName());

        //If the recipe image isn't null or empty, attempt to load it into the imageview.  If it is or fails, use a stock image
        if(mRecipeArrayList.get(position).getmImagePath() != null && !mRecipeArrayList.get(position).getmImagePath().equals("")) {
            Picasso.with(mContext).load(mRecipeArrayList.get(position).getmImagePath()).into(recipeImageView);
        }   else  {
            recipeImageView.setImageResource(R.color.colorPrimaryLight);
        }
        recipeImageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mClickHandler.onClick(index);
            }
        });


        return convertView;
    }

        public void setmRecipeArrayList(ArrayList<Recipe> list){
        mRecipeArrayList = list;
    }

}
