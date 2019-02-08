package com.example.android.newsapp.root;

import com.example.android.newsapp.http.NewsModule;
import com.example.android.newsapp.news.NewsActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, NewsModule.class})
public interface ApplicationComponent {

    void inject(NewsActivity newsActivity);

}
