package com.example.android.baking_app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import model.Ingredient;

public class IngredientListAdapter extends RecyclerView.Adapter<IngredientListAdapter.IngredientListAdapterViewHolder> {

    private static final String TAG = IngredientListAdapter.class.getSimpleName();
    private final Context mContext;
    private ArrayList<Ingredient> mIngredientList = null;

    public IngredientListAdapter(Context context, ArrayList<Ingredient> ingredientArrayList){
        mIngredientList = ingredientArrayList;
        mContext = context;
    }

    @NonNull
    @Override
    public IngredientListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutID = R.layout.ingredient_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(layoutID, parent, false);
        return new IngredientListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientListAdapterViewHolder holder, int position) {
        if(mIngredientList != null){
            int quantity = mIngredientList.get(position).getmQuantity();
            String measure = mIngredientList.get(position).getmMeasure();
            String ingredient = mIngredientList.get(position).getmIngredientName();
            Log.e(TAG, ingredient);
            holder.mQuantityTextview.setText(String.valueOf(quantity));
            holder.mMeasureTextview.setText(measure);
            holder.mIngredientNameTextview.setText(ingredient);
        }


    }

    @Override
    public int getItemCount() {
        if(mIngredientList != null){
            return mIngredientList.size();
        } else {
            return 0;
        }
    }

    class IngredientListAdapterViewHolder extends RecyclerView.ViewHolder{

        public final TextView mQuantityTextview;
        public final TextView mMeasureTextview;
        public final TextView mIngredientNameTextview;

        public IngredientListAdapterViewHolder(View itemView) {
            super(itemView);
            mQuantityTextview = (TextView) itemView.findViewById(R.id.textview_ingredient_list_item_quantity);
            mMeasureTextview = (TextView) itemView.findViewById(R.id.textview_ingredient_list_item_measure);
            mIngredientNameTextview = (TextView) itemView.findViewById(R.id.textview_ingredient_list_item_ingredient_name);
        }
    }

    public void setmIngredientList(ArrayList<Ingredient> list){
        mIngredientList = list;
        notifyDataSetChanged();
    }

}
