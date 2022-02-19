package com.example.android.newsapp.presentation.view;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.not;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.filters.LargeTest;
import androidx.test.filters.MediumTest;

import com.example.android.newsapp.R;
import com.example.android.newsapp.di.PresenterModule;
import com.example.android.newsapp.domain.interactor.NewsInteractor;
import com.example.android.newsapp.presentation.presenter.NewsPresenter;
import com.example.android.newsapp.presentation.presenter.NewsPresenterImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.inject.Inject;

import dagger.hilt.android.testing.BindValue;
import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;
import dagger.hilt.android.testing.UninstallModules;

@UninstallModules(PresenterModule.class)
@HiltAndroidTest
@LargeTest
public class NavigationTest {

    private CountingIdlingResource countingIdlingResource;
    private ActivityScenario<WelcomeActivity> scenario;

    @Rule
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    @BindValue
    NewsPresenter newsPresenter;

    @Inject
    NewsInteractor newsInteractor;

    @Before
    public void setUp(){
        hiltRule.inject();
        countingIdlingResource = new CountingIdlingResource("ArticlesAPICalls");
        newsPresenter = new NewsPresenterImpl(newsInteractor, countingIdlingResource);
        IdlingRegistry.getInstance().register(countingIdlingResource);
    }

    @Test
    public void navigateToNewsActivity_searchNewsAndAppearArticles() {

        scenario = ActivityScenario.launch(WelcomeActivity.class);

        onView(withId(R.id.search_et)).check(matches(isDisplayed()));
        onView(withId(R.id.search_et)).perform(typeText("Real Madrid"), closeSoftKeyboard());

        onView(withId(R.id.search_button)).check(matches(isDisplayed()));
        onView(withId(R.id.search_button)).perform(click());

        onView(withId(R.id.empty_view)).check(matches(not(isDisplayed())));
        onView(withId(R.id.recycler_view)).check(matches((isDisplayed())));
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())));

    }

    @After
    public void tearDown() {
        IdlingRegistry.getInstance().unregister(countingIdlingResource);
        scenario.close();
    }
}
