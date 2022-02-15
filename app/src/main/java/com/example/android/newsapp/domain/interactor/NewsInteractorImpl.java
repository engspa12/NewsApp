package com.example.android.newsapp.domain.interactor;

import com.example.android.newsapp.data.network.theguardian.Result;
import com.example.android.newsapp.domain.repository.NewsRepository;

import javax.inject.Inject;

import io.reactivex.Observable;

public class NewsInteractorImpl implements NewsInteractor {

    private NewsRepository newsRepository;

    @Inject
    public NewsInteractorImpl(NewsRepository newsRepository){
        this.newsRepository = newsRepository;
    }

    @Override
    public Observable<Result> sendData(String searchTerm, String sortType) {
        return newsRepository.getNewsData(searchTerm, sortType);
    }
}
