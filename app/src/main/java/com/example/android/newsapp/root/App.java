package com.example.android.newsapp.root;

import android.app.Application;

import com.example.android.newsapp.http.NewsModule;

public class App extends Application {

    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .newsModule(new NewsModule())
                .build();
    }

    public ApplicationComponent getComponent(){
        return component;
    }
}
