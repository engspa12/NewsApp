package com.example.android.newsapp.domain.interactor;

import com.example.android.newsapp.data.network.theguardian.Result;
import com.example.android.newsapp.domain.model.Article;
import com.example.android.newsapp.presentation.UIStateModel;

import java.util.List;

import io.reactivex.Observable;

public interface NewsInteractor {
    Observable<UIStateModel<List<Article>>> getData(String searchTerm, String sortType);
}
