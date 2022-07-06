package com.example.android.newsapp.presentation.view;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

import android.content.Context;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.android.newsapp.R;
import com.example.android.newsapp.presentation.view.activity.NewsActivity;
import com.example.android.newsapp.presentation.view.activity.WelcomeActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;

@HiltAndroidTest
@LargeTest
public class WelcomeActivityTest {

    public Context context;

    @Rule(order = 0)
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    @Rule(order = 1)
    public ActivityScenarioRule<WelcomeActivity> activityRule = new ActivityScenarioRule<>(WelcomeActivity.class);

    @Before
    public void setUp(){
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    @Test
    public void launchActivityIsLoaded_itemsAreDisplayedInPlace(){
        onView(withId(R.id.search_button)).check(matches(isDisplayed()));
        onView(withId(R.id.search_et)).check(matches(isDisplayed()));
        onView(withId(R.id.welcome_message)).check(matches(isDisplayed()));
        onView(withId(R.id.instructions)).check(matches(isDisplayed()));
        onView(withId(R.id.news_image)).check(matches(isDisplayed()));
    }

    @Test
    public void selectRelevantNewsUsingMenu_changeButtonTextAndSortTypeToRelevant(){
        openActionBarOverflowOrOptionsMenu(context);
        onView(withText(context.getString(R.string.relevant_news_menu))).perform(click());
        openActionBarOverflowOrOptionsMenu(context);
        onView(withText(context.getString(R.string.newest_news_menu))).perform(click());
        openActionBarOverflowOrOptionsMenu(context);
        onView(withText(context.getString(R.string.relevant_news_menu))).perform(click());
        onView(withId(R.id.search_button)).check(matches(withText(context.getString(R.string.search_relevant_news_text))));
    }

    @Test
    public void selectLatestNewsUsingMenu_changeButtonTextAndSortTypeToLatest(){
        openActionBarOverflowOrOptionsMenu(context);
        onView(withText(context.getString(R.string.relevant_news_menu))).perform(click());
        openActionBarOverflowOrOptionsMenu(context);
        onView(withText(context.getString(R.string.newest_news_menu))).perform(click());
        openActionBarOverflowOrOptionsMenu(context);
        onView(withText(context.getString(R.string.relevant_news_menu))).perform(click());
        openActionBarOverflowOrOptionsMenu(context);
        onView(withText(context.getString(R.string.newest_news_menu))).perform(click());
        onView(withId(R.id.search_button)).check(matches(withText(context.getString(R.string.search_latest_news_text))));
    }

    @Test
    public void pressSearchButton_performIntentToNewsActivity() {
        Intents.init();

        onView(withId(R.id.search_et)).perform(typeText("Daniel"),  closeSoftKeyboard());

        onView(withId(R.id.search_button)).perform(click());

        intended(hasComponent(NewsActivity.class.getName()));
        intended(hasExtra("search", "Daniel"));
        intended(hasExtra("sort_type", "relevance"));

        Intents.release();
    }

    @Test
    public void pressSearchButtonWithEmptyInput_showSnackBarErrorMessage() {
        onView(withId(R.id.search_button)).perform(click());
        onView(withId(R.id.search_et)).check(matches(withText("")));
        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText(R.string.empty_text_message)));
    }
}