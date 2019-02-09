package com.example.android.newsapp.http;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NewsRetrofitModule {

    public static final String BASE_URL = "http://content.guardianapis.com/";

    @Provides
    public Retrofit provideRetrofit(String baseUrl){
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    public NewsAPI provideNewsService(){
        return provideRetrofit(BASE_URL).create(NewsAPI.class);
    }
}
