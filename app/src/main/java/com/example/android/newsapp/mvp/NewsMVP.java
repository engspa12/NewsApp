package com.example.android.newsapp.mvp;

import com.example.android.newsapp.http.theguardian.Result;
import com.example.android.newsapp.news.Article;

import java.util.List;

import io.reactivex.Observable;


public interface NewsMVP {

    interface View{
        void updateNewsOnScreen(List<Article> list);
        void showErrorMessage(String message);
    }

    interface Presenter{
        void setView(NewsMVP.View view);
        void loadData(String searchTerm, String sortType);
        void rxJavaUnsubscribe();
    }

    interface Model{
        Observable<Result> sendData(String searchTerm, String sortType);
    }
}
