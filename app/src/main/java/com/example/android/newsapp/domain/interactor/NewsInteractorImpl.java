package com.example.android.newsapp.domain.interactor;

import android.util.Log;

import com.example.android.newsapp.domain.model.ArticleDomain;
import com.example.android.newsapp.domain.repository.NewsRepository;
import com.example.android.newsapp.domain.helper.ResultDomain;
import com.example.android.newsapp.presentation.model.ArticleView;
import com.example.android.newsapp.util.FrameworkHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class NewsInteractorImpl implements NewsInteractor {

    private NewsRepository newsRepository;
    private FrameworkHelper frameworkHelper;

    @Inject
    public NewsInteractorImpl(NewsRepository newsRepository, FrameworkHelper frameworkHelper){
        this.newsRepository = newsRepository;
        this.frameworkHelper = frameworkHelper;
    }

    @Override
    public Observable<ResultDomain<List<ArticleView>>> getData(String searchTerm, String sortType) {

        List<ArticleView> articles = new ArrayList<>();

        if(frameworkHelper.isOnline()){
            return newsRepository.getNewsData(searchTerm,sortType)
                    .subscribeOn(Schedulers.io())
                    .map(new Function<List<ArticleDomain>, ResultDomain<List<ArticleView>>>() {
                        @Override
                        public ResultDomain<List<ArticleView>> apply(List<ArticleDomain> domainItems) throws Exception {

                            for (ArticleDomain domainItem: domainItems) {
                                String sectionName = domainItem.getSectionName();
                                String webUrl = domainItem.getWebUrl();
                                String publicationDate = domainItem.getReleaseDate();
                                String webTitle = domainItem.getTitle();
                                String author = domainItem.getAuthor();
                                String thumbnailUrl = domainItem.getThumbnailUrl();

                                articles.add(new ArticleView(webTitle, sectionName, author, publicationDate, webUrl, thumbnailUrl));
                            }

                            return new ResultDomain<>(articles, false, null);
                        }
                    })
                    .onErrorReturn(new Function<Throwable, ResultDomain<List<ArticleView>>>() {
                        @Override
                        public ResultDomain<List<ArticleView>> apply(Throwable throwable) throws Exception {
                            Log.e("NewsInteractorImpl", throwable.getMessage());
                            return new ResultDomain<>(articles, true, frameworkHelper.getErrorMessage());
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread());
        } else {
            ResultDomain<List<ArticleView>> listArticles = new ResultDomain<List<ArticleView>>(articles, true, frameworkHelper.getNoInternetMessage());
            return Observable.just(listArticles);
        }
    }
}
