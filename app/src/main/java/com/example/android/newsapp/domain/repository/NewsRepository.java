package com.example.android.newsapp.domain.repository;

import com.example.android.newsapp.domain.model.ArticleDomain;

import java.util.List;

import io.reactivex.Observable;

public interface NewsRepository {
    Observable<List<ArticleDomain>> getNewsData(String searchTerm, String sortType);
}
