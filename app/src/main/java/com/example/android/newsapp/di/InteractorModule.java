package com.example.android.newsapp.di;

import com.example.android.newsapp.domain.interactor.NewsInteractor;
import com.example.android.newsapp.domain.interactor.NewsInteractorImpl;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class InteractorModule {

    @Binds
    public abstract NewsInteractor bindInteractor(
            NewsInteractorImpl newsInteractorImpl
    );

}
