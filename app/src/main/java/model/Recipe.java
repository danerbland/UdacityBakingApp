package model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Recipe implements Parcelable{

    private int mId;
    private String mName;
    private int mServings;
    private String mImagePath;
    private ArrayList<Ingredient> mIngredientsList;
    private ArrayList<Step> mStepsList;

    public Recipe (int id, String name, int servings, String imagepath, ArrayList<Ingredient> ingredientList, ArrayList<Step> steplist){
        mId = id;
        mName = name;
        mServings = servings;
        mImagePath = imagepath;
        mIngredientsList = ingredientList;
        mStepsList = steplist;
    }


    protected Recipe(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
        mServings = in.readInt();
        mImagePath = in.readString();
        mIngredientsList = in.createTypedArrayList(Ingredient.CREATOR);
        mStepsList = in.createTypedArrayList(Step.CREATOR);
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public int getmId() {
        return mId;
    }

    public String getmName() {
        return mName;
    }

    public int getmServings() {
        return mServings;
    }

    public String getmImagePath() {
        return mImagePath;
    }

    public ArrayList<Ingredient> getmIngredientsList() {
        return mIngredientsList;
    }

    public ArrayList<Step> getmStepsList() {
        return mStepsList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mId);
        parcel.writeString(mName);
        parcel.writeInt(mServings);
        parcel.writeString(mImagePath);
        parcel.writeTypedList(mIngredientsList);
        parcel.writeTypedList(mStepsList);
    }
}
