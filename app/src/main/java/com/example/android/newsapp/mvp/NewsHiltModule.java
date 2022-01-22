package com.example.android.newsapp.mvp;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;

@Module
@InstallIn(ActivityComponent.class)
public abstract class NewsHiltModule {

    @Binds
    public abstract NewsMVP.Presenter bindPresenter(
            NewsPresenterImpl newsPresenterImpl
    );

    @Binds
    public abstract NewsMVP.Model bindModel(
            NewsModelImpl newsModelImpl
    );

    @Binds
    public abstract Repository bindRepository(
            NewsRepositoryImpl newsRepositoryImpl
    );
}
