package com.example.android.newsapp.mvp;

import com.example.android.newsapp.http.NewsAPI;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class NewsMVPModule {

    @Provides
    public NewsMVP.Presenter provideNewsPresenter(NewsMVP.Model model){
        return new NewsPresenter(model);
    }

    @Provides
    public NewsMVP.Model provideNewsModel(Repository repository){
        return new NewsModel(repository);
    }

    @Singleton
    @Provides
    public Repository provideRepository(NewsAPI newsAPI){
        return new NewsRepository(newsAPI);
    }
}
