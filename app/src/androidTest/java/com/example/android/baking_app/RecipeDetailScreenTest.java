package com.example.android.baking_app;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.InstrumentationInfo;
import android.support.test.InstrumentationRegistry;
import android.content.res.Resources;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import model.Ingredient;
import model.Recipe;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class RecipeDetailScreenTest {




    @Rule
    public IntentsTestRule<RecipeDetailActivity> mRecipeDetailTestRule =
            new IntentsTestRule<RecipeDetailActivity>(RecipeDetailActivity.class){

                //Have to initialize the activity with a Recipe.  Assist from:
                //https://xebia.com/blog/android-intent-extras-espresso-rules/
                SampleRecipe sampleRecipe = new SampleRecipe();
                Recipe testRecipe = SampleRecipe.getmSampleRecipe();

        @Override
            protected Intent getActivityIntent() {
                Context targetContext = InstrumentationRegistry.getInstrumentation()
                        .getTargetContext();
                Intent result = new Intent(targetContext, MainActivity.class);
                result.putExtra("recipe_extra", testRecipe);
                return result;
            }};

    @Before
    public void stubAllExternalIntents() {
        // By default Espresso Intents does not stub any Intents. Stubbing needs to be setup before
        // every test run. In this case all external Intents will be blocked.
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));

    }

    @Test
    public void clickStepListItem_SendsIntent(){



        onView(withId(R.id.recyclerview_steps_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        intended(hasComponent(StepDetailActivity.class.getName()));
    }

}
