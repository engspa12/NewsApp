package com.example.android.newsapp.di;

import com.example.android.newsapp.data.repository.NewsRepositoryImpl;
import com.example.android.newsapp.domain.repository.NewsRepository;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;

@Module
@InstallIn(ActivityComponent.class)
public abstract class RepositoryModule {

    @Binds
    public abstract NewsRepository bindRepository(
            NewsRepositoryImpl newsRepositoryImpl
    );
}
