package com.example.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;


public class ArticleLoader extends AsyncTaskLoader<List<Article>> {

    //Url for the query
    private String urlRequest="";

    //Constructor of ArticleLoader
    public ArticleLoader(Context context, String url){
        super(context);
        urlRequest = url;
    }


    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    //Worker Thread
    @Override
    public List<Article> loadInBackground() {
        if (urlRequest == null) {
            return null;
        }
    //return a List of Article objects
        return NetworkQueryUtils.getArticles(urlRequest);
    }
}
