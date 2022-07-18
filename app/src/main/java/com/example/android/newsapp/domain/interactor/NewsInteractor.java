package com.example.android.newsapp.domain.interactor;

import com.example.android.newsapp.domain.util.ResultWrapper;
import com.example.android.newsapp.presentation.model.ArticleView;

import java.util.List;

import io.reactivex.Observable;

public interface NewsInteractor {
    Observable<ResultWrapper<List<ArticleView>>> getData(String searchTerm, String sortType);
}
