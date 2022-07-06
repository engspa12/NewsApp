package com.example.android.newsapp.presentation.presenter;

import androidx.annotation.VisibleForTesting;
import androidx.test.espresso.idling.CountingIdlingResource;

import com.example.android.newsapp.domain.interactor.NewsInteractor;
import com.example.android.newsapp.domain.model.ArticleDomain;
import com.example.android.newsapp.domain.helper.ResultDomain;
import com.example.android.newsapp.presentation.model.ArticleView;
import com.example.android.newsapp.presentation.view.contract.NewsView;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class NewsPresenterImpl implements NewsPresenter {

    //Interface View
    @Nullable
    private NewsView newsView;

    //Interface Interactor
    private NewsInteractor newsInteractor;
    private Disposable subscription;
    private CountingIdlingResource countingIdlingResource;

    @Inject
    public NewsPresenterImpl(NewsInteractor newsInteractor) {
        this.newsInteractor = newsInteractor;
    }

    private static final String LOG = NewsPresenterImpl.class.getSimpleName();


    @Override
    public void setView(NewsView view) {
        this.newsView = view;
    }

    @Override
    public void loadData(String searchTerm, String sortType) {

        incrementIdlingResource();

        if (newsView != null) {
            subscription = newsInteractor.getData(searchTerm, sortType)
                    .subscribe(new Consumer<ResultDomain<List<ArticleView>>>() {
                        @Override
                        public void accept(ResultDomain<List<ArticleView>> listArticles) throws Exception {
                            if(listArticles.getErrorPresent()){
                                newsView.showErrorMessage(listArticles.getErrorMessage());
                            } else {
                                newsView.updateNewsOnScreen(listArticles.getValue());
                            }
                            decrementIdlingResource();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            newsView.showErrorMessage(throwable.getMessage());
                            decrementIdlingResource();
                        }
                    });
        }

    }

    private void incrementIdlingResource(){
        if(countingIdlingResource != null){
            countingIdlingResource.increment();
        }
    }

    private void decrementIdlingResource(){
        if(countingIdlingResource != null){
            countingIdlingResource.decrement();
        }
    }

    @Override
    public void rxJavaUnsubscribe() {
        if (subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
        }
    }

    @Override
    public void removeView() {
        newsView = null;
    }

    @VisibleForTesting
    @Override
    public void setIdlingResource(CountingIdlingResource countingIdlingResource){
        this.countingIdlingResource = countingIdlingResource;
    }
}
