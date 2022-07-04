package com.example.android.newsapp.domain.interactor;

import com.example.android.newsapp.data.network.theguardian.Fields;
import com.example.android.newsapp.domain.model.Article;
import com.example.android.newsapp.domain.repository.NewsRepository;
import com.example.android.newsapp.util.Result;
import com.example.android.newsapp.util.Helper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class NewsInteractorImpl implements NewsInteractor {

    private NewsRepository newsRepository;
    private Helper helper;

    @Inject
    public NewsInteractorImpl(NewsRepository newsRepository, Helper helper){
        this.newsRepository = newsRepository;
        this.helper = helper;
    }

    @Override
    public Observable<Result<List<Article>>> getData(String searchTerm, String sortType) {

        List<Article> articles = new ArrayList<>();

        if(helper.isOnline()){
            return newsRepository.getNewsData(searchTerm,sortType)
                    .subscribeOn(Schedulers.io())
                    .map(new Function<List<com.example.android.newsapp.data.network.theguardian.Result>, Result<List<Article>>>() {
                        @Override
                        public Result<List<Article>> apply(List<com.example.android.newsapp.data.network.theguardian.Result> results) throws Exception {

                            for (com.example.android.newsapp.data.network.theguardian.Result result: results) {
                                String sectionName = result.getSectionName();
                                String webUrl = result.getWebUrl();
                                String publicationDate = result.getWebPublicationDate().substring(0, 10);
                                String webTitle;
                                String author;
                                String thumbnailUrl;

                                try {
                                    Fields fields = result.getFields();
                                    try {
                                        author = fields.getByline();
                                    } catch (Exception e) {
                                        author = "unknown author";
                                    }

                                    try {
                                        webTitle = fields.getHeadline();
                                    } catch (Exception e) {
                                        webTitle = "Unknown title";
                                    }

                                    try {
                                        thumbnailUrl = fields.getThumbnail();
                                    } catch (Exception e) {
                                        thumbnailUrl = "No image available";
                                    }
                                } catch (Exception e) {
                                    author = "unknown author";
                                    webTitle = "Unknown title";
                                    thumbnailUrl = "No image available";
                                }

                                articles.add(new Article(webTitle, sectionName, author, publicationDate, webUrl, thumbnailUrl));

                            }

                            return new Result<>(articles, false, null);
                        }
                    })
                    .onErrorReturn(new Function<Throwable, Result<List<Article>>>() {
                        @Override
                        public Result<List<Article>> apply(Throwable throwable) throws Exception {
                            return new Result<>(articles, true, helper.getErrorMessage());
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread());
        } else {
            Result<List<Article>> uiStateModel = new Result<List<Article>>(articles, true, helper.getNoInternetMessage());
            return Observable.just(uiStateModel);
        }
    }
}
