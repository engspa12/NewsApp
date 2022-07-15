package com.example.android.newsapp.presentation.view;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.android.newsapp.R;
import com.example.android.newsapp.di.PresenterModule;
import com.example.android.newsapp.presentation.model.ArticleView;
import com.example.android.newsapp.presentation.presenter.NewsPresenter;
import com.example.android.newsapp.presentation.view.activity.NewsActivity;
import com.example.android.newsapp.presentation.view.contract.NewsView;
import com.example.android.newsapp.util.FrameworkHelper;
import com.example.android.newsapp.util.FrameworkHelperImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.testing.BindValue;
import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;
import dagger.hilt.android.testing.UninstallModules;

@UninstallModules(PresenterModule.class)
@HiltAndroidTest
@LargeTest
public class NewsActivityTest {

    private Context context;
    private boolean errorResponse = false;
    private boolean noInternetConnection = false;
    private Intent intent;
    private ActivityScenario<NewsActivity> scenario;

    @BindValue NewsPresenter newsPresenter;

    @Rule
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        FrameworkHelper frameworkHelper = new FrameworkHelperImpl(context);
        newsPresenter = new FakePresenter(frameworkHelper);
        intent = new Intent(context, NewsActivity.class);
        intent.putExtra("search","Daniel");
        intent.putExtra("sort_type", "relevance");
    }

    @After
    public void tearDown() {
        scenario.close();
    }

    @Test
    public void errorResponseDuringArticlesRetrieval_showErrorMessage(){

        errorInResponseFromAPI();

        scenario = ActivityScenario.launch(intent);

        onView(withId(R.id.empty_view)).check(matches(isDisplayed()));
        onView(withId(R.id.empty_view)).check(matches(withText(context.getString(R.string.error_message))));
        onView(withId(R.id.recycler_view)).check(matches(not(isDisplayed())));
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())));
        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText(context.getString(R.string.error_message))));

        assertEquals(((FakePresenter) newsPresenter).getSearchTerm(),"Daniel");
        assertEquals(((FakePresenter) newsPresenter).getSortType(),"relevance");

        scenario.moveToState(Lifecycle.State.CREATED);
        assertEquals(0, ((FakePresenter) newsPresenter).getCountCleanResources());
    }

    @Test
    public void noInternetConnectionInDevice_showNoInternetMessage(){

        noInternetError();

        scenario = ActivityScenario.launch(intent);

        onView(withId(R.id.empty_view)).check(matches(isDisplayed()));
        onView(withId(R.id.empty_view)).check(matches(withText(context.getString(R.string.no_internet_connection))));
        onView(withId(R.id.recycler_view)).check(matches(not(isDisplayed())));
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())));
        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText(context.getString(R.string.no_internet_connection))));

        assertEquals(((FakePresenter) newsPresenter).getSearchTerm(),"Daniel");
        assertEquals(((FakePresenter) newsPresenter).getSortType(),"relevance");

        scenario.moveToState(Lifecycle.State.CREATED);
        assertEquals(0, ((FakePresenter) newsPresenter).getCountCleanResources());

    }

    @Test
    public void succesfulResponse_showArticlesListOnScreen(){

        successfulResponse();

        scenario = ActivityScenario.launch(intent);

        onView(withId(R.id.empty_view)).check(matches(not(isDisplayed())));
        onView(withId(R.id.recycler_view)).check(matches((isDisplayed())));
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())));

        assertEquals(((FakePresenter) newsPresenter).getSearchTerm(),"Daniel");
        assertEquals(((FakePresenter) newsPresenter).getSortType(),"relevance");

        scenario.moveToState(Lifecycle.State.CREATED);
        assertEquals(0, ((FakePresenter) newsPresenter).getCountCleanResources());
    }

    private void errorInResponseFromAPI(){
        errorResponse = true;
        noInternetConnection = false;
    }

    private void noInternetError(){
        errorResponse = false;
        noInternetConnection = true;
    }

    private void successfulResponse(){
        errorResponse = false;
        noInternetConnection = false;
    }

    public class FakePresenter implements NewsPresenter {

        private final String LOG = FakePresenter.class.getName();

        private NewsView newsView;
        private final FrameworkHelper frameworkHelper;
        private int counterCleanResources = 0;
        private String sortType = "";
        private String searchTerm = "";

        public FakePresenter(FrameworkHelper frameworkHelper){
            this.frameworkHelper = frameworkHelper;
        }

        @Override
        public void setView(NewsView newsView) {
            this.newsView = newsView;
        }

        @Override
        public void loadData(String searchTerm, String sortType) {
            this.searchTerm = searchTerm;
            this.sortType = sortType;
            if (noInternetConnection) {
                newsView.showErrorMessage(frameworkHelper.getNoInternetMessage());
            } else if(errorResponse) {
                newsView.showErrorMessage(frameworkHelper.getErrorMessage());
            } else {
                List<ArticleView> listArticles = new ArrayList<ArticleView>();
                listArticles.add(new ArticleView("article title 1", "article section 1", "author 1", "date 1", "https://www.google.com", null));
                listArticles.add(new ArticleView("article title 2", "article section 2", "author 2", "date 2", "https://www.google.com", null));
                listArticles.add(new ArticleView("article title 3", "article section 3", "author 3", "date 3", "https://www.google.com", null));
                listArticles.add(new ArticleView("article title 4", "article section 4", "author 4", "date 4", "https://www.google.com", null));
                listArticles.add(new ArticleView("article title 5", "article section 5", "author 5", "date 5", "https://www.google.com", null));
                listArticles.add(new ArticleView("article title 6", "article section 6", "author 6", "date 6", "https://www.google.com", null));
                listArticles.add(new ArticleView("article title 7", "article section 7", "author 7", "date 7", "https://www.google.com", null));
                newsView.updateNewsOnScreen(listArticles);
            }

        }

        @Override
        public void rxJavaUnsubscribe() {
            counterCleanResources++;
            Log.i(LOG, "rxJavaUnsubscribe was called");
        }

        @Override
        public void removeView(){
            counterCleanResources++;
            newsView = null;
        }

        @Override
        public void setIdlingResource(CountingIdlingResource countingIdlingResource) {
            //no-opt
        }

        public int getCountCleanResources(){
            return counterCleanResources;
        }

        public String getSearchTerm(){
            return searchTerm;
        }

        public String getSortType(){
            return sortType;
        }


    }
}