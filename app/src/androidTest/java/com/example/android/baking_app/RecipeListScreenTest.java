package com.example.android.baking_app;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class RecipeListScreenTest {

    @Rule
    public ActivityTestRule<MainActivity> mMainActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource(){
        mIdlingResource = mMainActivityTestRule.getActivity().getIdlingResource();
        if(mIdlingResource != null){
            Espresso.registerIdlingResources(mIdlingResource);
        }
    }

    @Test
    public void clickRecipeItem_SendsIntentWithRecipeExtra(){
        onData(Matchers.anything()).inAdapterView(withId(R.id.gridview_recipe_list)).atPosition(0).perform(click());

        onView(withId(R.id.recyclerview_steps_list)).check(matches(hasDescendant(withText("Recipe Introduction"))));
        }

    @After
    public void unregisterIdlingResource(){
        if(mIdlingResource != null){
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }


}
