package com.example.android.newsapp.domain.interactor;

import com.example.android.newsapp.domain.model.ArticleDomain;
import com.example.android.newsapp.domain.helper.ResultDomain;
import com.example.android.newsapp.presentation.model.ArticleView;

import java.util.List;

import io.reactivex.Observable;

public interface NewsInteractor {
    Observable<ResultDomain<List<ArticleView>>> getData(String searchTerm, String sortType);
}
