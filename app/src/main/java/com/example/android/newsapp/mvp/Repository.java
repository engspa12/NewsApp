package com.example.android.newsapp.mvp;

import com.example.android.newsapp.http.theguardian.Result;

import java.util.Map;

import io.reactivex.Observable;

public interface Repository {

    Observable<Result> getNewsData(String searchTerm, String sortType);

    Observable<Result> getNewsDataFromNetwork(Map<String, String> queryParams);

    Observable<Result> getNewsDataFromCache();

}
