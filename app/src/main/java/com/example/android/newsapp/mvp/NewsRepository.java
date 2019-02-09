package com.example.android.newsapp.mvp;

import com.example.android.newsapp.BuildConfig;
import com.example.android.newsapp.http.NewsAPI;
import com.example.android.newsapp.http.theguardian.NewsSearch;
import com.example.android.newsapp.http.theguardian.Response;
import com.example.android.newsapp.http.theguardian.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class NewsRepository implements Repository {

    private NewsAPI newsAPI;

    private List<Result> results;

    private long lastTimeStamp;

    //Cache lifetime
    private static final long CACHE_LIFETIME = 20 * 1000;

    //Number of articles to query
    private static final String NUMBER_OF_ARTICLES = "50";
    private static final String API_KEY = BuildConfig.API_KEY;
    private static final String FILTER_RESULTS = "headline,byline,thumbnail";

    public NewsRepository(NewsAPI newsAPI){
        this.newsAPI = newsAPI;
        this.results = new ArrayList<>();
    }

    public boolean isUpdated(){
        return (System.currentTimeMillis() - lastTimeStamp) < CACHE_LIFETIME;
    }

    @Override
    public Observable<Result> getNewsData(String searchTerm, String sortType) {

        //Map for querying the API
        Map<String, String> parameters = new HashMap<>();

        parameters.put("q", searchTerm);
        parameters.put("order-by", sortType);
        parameters.put("show-fields", FILTER_RESULTS);
        parameters.put("page-size", NUMBER_OF_ARTICLES);
        parameters.put("api-key", API_KEY);

        return getNewsDataFromCache().switchIfEmpty(getNewsDataFromNetwork(parameters));
    }

    @Override
    public Observable<Result> getNewsDataFromNetwork(Map<String, String> queryParams) {

        //Create Observable
        Observable<NewsSearch> newsApiObservable = newsAPI.getNews(queryParams);

        return newsApiObservable
                .concatMap(new Function<NewsSearch, Observable<Response>>() {
                    @Override
                    public Observable<Response> apply(NewsSearch newsSearch) throws Exception {
                        return Observable.just(newsSearch.getResponse());
                    }
                })
                .concatMap(new Function<Response, Observable<Result>>() {
                    @Override
                    public Observable<Result> apply(Response response) throws Exception {
                        return Observable.fromIterable(response.getResults());
                    }
                });

    }

    @Override
    public Observable<Result> getNewsDataFromCache() {
        if(isUpdated()){
           return Observable.fromIterable(results);
        } else{
            lastTimeStamp = System.currentTimeMillis();
            results.clear();
            return Observable.empty();
        }
    }
}
