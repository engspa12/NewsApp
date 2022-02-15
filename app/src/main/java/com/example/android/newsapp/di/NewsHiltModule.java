package com.example.android.newsapp.di;

import com.example.android.newsapp.domain.interactor.NewsInteractorImpl;
import com.example.android.newsapp.domain.interactor.NewsInteractor;
import com.example.android.newsapp.domain.repository.NewsRepository;
import com.example.android.newsapp.presentation.presenter.NewsPresenterImpl;
import com.example.android.newsapp.data.repository.NewsRepositoryImpl;
import com.example.android.newsapp.presentation.presenter.NewsPresenter;
import com.example.android.newsapp.util.Helper;
import com.example.android.newsapp.util.HelperImpl;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;

@Module
@InstallIn(ActivityComponent.class)
public abstract class NewsHiltModule {

    @Binds
    public abstract NewsPresenter bindPresenter(
            NewsPresenterImpl newsPresenterImpl
    );

    @Binds
    public abstract NewsInteractor bindInteractor(
            NewsInteractorImpl newsInteractorImpl
    );

    @Binds
    public abstract NewsRepository bindRepository(
            NewsRepositoryImpl newsRepositoryImpl
    );

    @Binds
    public abstract Helper bindHelper(
        HelperImpl helperImpl
    );
}
