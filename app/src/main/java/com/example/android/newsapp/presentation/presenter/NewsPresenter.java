package com.example.android.newsapp.presentation.presenter;

import com.example.android.newsapp.presentation.view.NewsView;

public interface NewsPresenter {

    void setView(NewsView newsView);
    void loadData(String searchTerm, String sortType);
    void rxJavaUnsubscribe();

}
