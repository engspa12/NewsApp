package com.example.android.newsapp.presentation.presenter;

import android.util.Log;

import androidx.test.espresso.idling.CountingIdlingResource;

import com.example.android.newsapp.data.network.theguardian.Fields;
import com.example.android.newsapp.data.network.theguardian.Result;
import com.example.android.newsapp.domain.interactor.NewsInteractor;
import com.example.android.newsapp.domain.model.Article;
import com.example.android.newsapp.presentation.UIStateModel;
import com.example.android.newsapp.presentation.view.NewsView;
import com.example.android.newsapp.util.Helper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class NewsPresenterImpl implements NewsPresenter {

    //Interface View
    @Nullable
    private NewsView newsView;

    //Interface Interactor
    private NewsInteractor newsInteractor;
    private Disposable subscription;
    private CountingIdlingResource countingIdlingResource;

    @Inject
    public NewsPresenterImpl(NewsInteractor newsInteractor, CountingIdlingResource countingIdlingResource) {
        this.newsInteractor = newsInteractor;
        this.countingIdlingResource = countingIdlingResource;
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
                    .subscribe(new Consumer<UIStateModel<List<Article>>>() {
                        @Override
                        public void accept(UIStateModel<List<Article>> uiStateModel) throws Exception {
                            if(uiStateModel.getErrorPresent()){
                                newsView.showErrorMessage(uiStateModel.getErrorMessage());
                            } else {
                                newsView.updateNewsOnScreen(uiStateModel.getValue());
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
}
