package com.example.android.newsapp.root;

import android.app.Application;

import com.example.android.newsapp.mvp.NewsMVPModule;
import com.example.android.newsapp.http.NewsRetrofitModule;

public class App extends Application {

    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .newsRetrofitModule(new NewsRetrofitModule())
                .newsMVPModule(new NewsMVPModule())
                .build();
    }

    public ApplicationComponent getComponent(){
        return component;
    }
}
