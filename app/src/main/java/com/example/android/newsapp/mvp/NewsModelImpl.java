package com.example.android.newsapp.mvp;

import com.example.android.newsapp.http.theguardian.Result;

import javax.inject.Inject;

import io.reactivex.Observable;

public class NewsModelImpl implements NewsMVP.Model {

    private Repository repository;

    @Inject
    public NewsModelImpl(Repository repository){
        this.repository = repository;
    }

    @Override
    public Observable<Result> sendData(String searchTerm, String sortType) {
        return repository.getNewsData(searchTerm, sortType);
    }
}
