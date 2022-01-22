package com.example.android.newsapp.mvp;

import android.util.Log;

import com.example.android.newsapp.http.theguardian.Fields;
import com.example.android.newsapp.http.theguardian.Result;
import com.example.android.newsapp.entities.Article;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class NewsPresenterImpl implements NewsMVP.Presenter {

    //Interface View
    @Nullable
    private NewsMVP.View view;

    //Interface Model
    private NewsMVP.Model model;

    private Disposable subscription;

    private List<Article> articles;

    @Inject
    public NewsPresenterImpl(NewsMVP.Model model){
        this.model = model;
        this.articles = new ArrayList<>();
    }

    private static final String LOG = NewsPresenterImpl.class.getSimpleName();


    @Override
    public void setView(NewsMVP.View view) {
        this.view = view;
    }

    @Override
    public void loadData(String searchTerm, String sortType) {

        if(view != null){
            subscription = model.sendData(searchTerm, sortType)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableObserver<Result>() {
                        @Override
                        public void onNext(Result result) {

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

                        @Override
                        public void onError(Throwable e) {
                            view.showErrorMessage("There was an error while retrieving the data");
                            Log.e(LOG, e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            view.updateNewsOnScreen(articles);
                        }
            });
        }
    }

    @Override
    public void rxJavaUnsubscribe() {
        if(subscription != null && !subscription.isDisposed()){
            subscription.dispose();
        }
    }
}
