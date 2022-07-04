package com.example.android.newsapp.domain.interactor;

import com.example.android.newsapp.domain.model.Article;
import com.example.android.newsapp.util.Result;

import java.util.List;

import io.reactivex.Observable;

public interface NewsInteractor {
    Observable<Result<List<Article>>> getData(String searchTerm, String sortType);
}
