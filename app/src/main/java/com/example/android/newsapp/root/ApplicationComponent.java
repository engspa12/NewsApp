package com.example.android.newsapp.root;

import com.example.android.newsapp.mvp.NewsMVPModule;
import com.example.android.newsapp.http.NewsRetrofitModule;
import com.example.android.newsapp.news.NewsActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, NewsRetrofitModule.class, NewsMVPModule.class})
public interface ApplicationComponent {

    void inject(NewsActivity newsActivity);

}
