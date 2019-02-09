package com.example.android.newsapp.mvp;

import com.example.android.newsapp.http.theguardian.Result;

import io.reactivex.Observable;

public class NewsModel implements NewsMVP.Model {

    private Repository repository;

    public NewsModel(Repository repository){
        this.repository = repository;
    }

    @Override
    public Observable<Result> sendData(String searchTerm, String sortType) {
        return repository.getNewsData(searchTerm, sortType);
    }
}
