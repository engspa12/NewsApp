package com.example.android.newsapp.di;

import androidx.test.espresso.idling.CountingIdlingResource;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class IdlingResourceModule {

    @Provides
    public CountingIdlingResource getIdlingResource() {
        return new CountingIdlingResource("NoOptCountingResource");
    }
}
