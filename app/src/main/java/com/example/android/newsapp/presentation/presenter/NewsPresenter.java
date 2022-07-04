package com.example.android.newsapp.presentation.presenter;

import androidx.annotation.VisibleForTesting;
import androidx.test.espresso.idling.CountingIdlingResource;

import com.example.android.newsapp.presentation.view.NewsView;

public interface NewsPresenter {

    void setView(NewsView newsView);
    void loadData(String searchTerm, String sortType);
    void rxJavaUnsubscribe();
    void removeView();
    @VisibleForTesting
    void setIdlingResource(CountingIdlingResource countingIdlingResource);
}
