package com.example.android.newsapp.presentation.view.contract;

import com.example.android.newsapp.domain.model.ArticleDomain;
import com.example.android.newsapp.presentation.model.ArticleView;

import java.util.List;

public interface NewsView {

    void updateNewsOnScreen(List<ArticleView> list);
    void showErrorMessage(String message);

}
