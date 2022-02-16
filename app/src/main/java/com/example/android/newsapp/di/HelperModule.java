package com.example.android.newsapp.di;


import android.content.Context;

import com.example.android.newsapp.util.Helper;
import com.example.android.newsapp.util.HelperImpl;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class HelperModule {

    @Provides
    public Helper getHelper(
            @ApplicationContext Context appContext) {
        return new HelperImpl(appContext);
    }
}
