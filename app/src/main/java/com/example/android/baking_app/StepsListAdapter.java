package com.example.android.baking_app;

import android.content.ContentValues;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import model.Step;

public class StepsListAdapter  extends RecyclerView.Adapter<StepsListAdapter.StepAdapterViewHolder>{

    private static final String TAG = StepsListAdapter.class.getSimpleName();
    private ArrayList<Step> mStepArrayList;
    private final Context mContext;
    private final StepOnClickHandler mClickHandler;

    public interface StepOnClickHandler {
        void onClick(int index);
    }

    public StepsListAdapter (Context context, StepOnClickHandler clickHandler){
        mContext = context;
        mClickHandler = clickHandler;
    }

    @Override
    public StepAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutID = R.layout.step_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(layoutID, parent, false);

        return new StepAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepAdapterViewHolder holder, int position) {
        if(mStepArrayList != null){
            String stepNumber = String.valueOf(mStepArrayList.get(position).getmId());
            String shortDescription = mStepArrayList.get(position).getmShortDescription();

            holder.mStepNumberTextView.setText(stepNumber);
            holder.mStepShortDescriptionTextView.setText(shortDescription);
        }
    }

    @Override
    public int getItemCount() {
        if(mStepArrayList!=null){
            return mStepArrayList.size();
        } else {
            return 0;
        }
    }

    class StepAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public final TextView mStepNumberTextView;
        public final TextView mStepShortDescriptionTextView;

        public StepAdapterViewHolder(View itemView) {
            super(itemView);
            mStepNumberTextView = itemView.findViewById(R.id.textview_step_number);
            mStepShortDescriptionTextView = itemView.findViewById(R.id.textview_step_short_description);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(adapterPosition);
        }
    }

    public void setmStepArrayList (ArrayList<Step> values){
        mStepArrayList = values;
        notifyDataSetChanged();
    }


}
