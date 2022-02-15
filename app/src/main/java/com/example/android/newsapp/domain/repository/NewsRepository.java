package com.example.android.newsapp.domain.repository;

import com.example.android.newsapp.data.network.theguardian.Result;

import java.util.Map;

import io.reactivex.Observable;

public interface NewsRepository {

    Observable<Result> getNewsData(String searchTerm, String sortType);
    Observable<Result> getNewsDataFromNetwork(Map<String, String> queryParams);
    Observable<Result> getNewsDataFromCache();

}
