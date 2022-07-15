package com.example.android.newsapp.di;


import android.content.Context;

import com.example.android.newsapp.util.FrameworkHelper;
import com.example.android.newsapp.util.FrameworkHelperImpl;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class HelperModule {

    @Provides
    public FrameworkHelper getHelper(
            @ApplicationContext Context appContext) {
        return new FrameworkHelperImpl(appContext);
    }
}
