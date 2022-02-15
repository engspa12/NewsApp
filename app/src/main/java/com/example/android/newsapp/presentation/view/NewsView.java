package com.example.android.newsapp.presentation.view;

import com.example.android.newsapp.domain.model.Article;

import java.util.List;

public interface NewsView {

    void updateNewsOnScreen(List<Article> list);
    void showErrorMessage(String message);

}
