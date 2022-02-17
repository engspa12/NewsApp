package com.example.android.newsapp.di;

import com.example.android.newsapp.presentation.presenter.NewsPresenter;
import com.example.android.newsapp.presentation.presenter.NewsPresenterImpl;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;

@Module
@InstallIn(ActivityComponent.class)
public abstract class PresenterModule {

    @Binds
    public abstract NewsPresenter bindPresenter(
            NewsPresenterImpl newsPresenterImpl
    );
}
