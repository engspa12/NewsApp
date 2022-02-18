package com.example.android.newsapp.domain.repository;

import com.example.android.newsapp.data.network.theguardian.Result;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

public interface NewsRepository {

    Observable<List<Result>> getNewsData(String searchTerm, String sortType);
    Observable<List<Result>> getNewsDataFromNetwork(Map<String, String> queryParams);
    Observable<List<Result>> getNewsDataFromCache();

}
