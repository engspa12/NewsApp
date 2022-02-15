package com.example.android.newsapp.domain.interactor;

import com.example.android.newsapp.data.network.theguardian.Result;

import io.reactivex.Observable;

public interface NewsInteractor {
    Observable<Result> sendData(String searchTerm, String sortType);
}
