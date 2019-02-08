package com.example.android.newsapp.http;

import com.example.android.newsapp.http.theguardian.NewsSearch;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface NewsAPI {

    @GET("search")
    Observable<NewsSearch> getNews(@QueryMap Map<String, String> query);
}
