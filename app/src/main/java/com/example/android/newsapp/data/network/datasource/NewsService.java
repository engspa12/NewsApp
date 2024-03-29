package com.example.android.newsapp.data.network.datasource;

import com.example.android.newsapp.data.network.model.NewsSearch;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface NewsService {

    @GET("search")
    Observable<NewsSearch> getNews(@QueryMap Map<String, String> query);
}
