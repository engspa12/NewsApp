package com.example.android.newsapp.presentation.view;

import static org.junit.Assert.*;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class NewsActivityTest {

    @Rule
    public ActivityScenarioRule<NewsActivity> activityRule = new ActivityScenarioRule<>(NewsActivity.class);

    private ActivityScenario<NewsActivity> scenario;

    @Before
    public void setUp()  {
        scenario = activityRule.getScenario();
    }

    @Test
    public void firstTest(){
        scenario.onActivity(new ActivityScenario.ActivityAction<NewsActivity>() {
            @Override
            public void perform(NewsActivity activity) {

            }
        });
    }
}