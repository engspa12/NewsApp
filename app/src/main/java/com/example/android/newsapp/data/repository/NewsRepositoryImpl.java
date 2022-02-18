package com.example.android.newsapp.data.repository;

import com.example.android.newsapp.BuildConfig;
import com.example.android.newsapp.data.network.NewsService;
import com.example.android.newsapp.data.network.theguardian.NewsSearch;
import com.example.android.newsapp.data.network.theguardian.Response;
import com.example.android.newsapp.data.network.theguardian.Result;
import com.example.android.newsapp.domain.repository.NewsRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class NewsRepositoryImpl implements NewsRepository {

    private NewsService newsService;

    private List<Result> results;

    private long lastTimeStamp;

    //Cache lifetime
    private static final long CACHE_LIFETIME = 20 * 1000;

    //Number of articles to query
    private static final String NUMBER_OF_ARTICLES = "50";
    private static final String API_KEY = BuildConfig.API_KEY;
    private static final String FILTER_RESULTS = "headline,byline,thumbnail";

    @Inject
    public NewsRepositoryImpl(NewsService newsService){
        this.newsService = newsService;
        this.results = new ArrayList<>();
    }

    public boolean isUpdated(){
        return (System.currentTimeMillis() - lastTimeStamp) < CACHE_LIFETIME;
    }

    @Override
    public Observable<List<Result>> getNewsData(String searchTerm, String sortType) {

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
    public Observable<List<Result>> getNewsDataFromNetwork(Map<String, String> queryParams) {

        //Create Observable
        Observable<NewsSearch> newsApiObservable = newsService.getNews(queryParams);

        return newsApiObservable
                .concatMap(new Function<NewsSearch, Observable<Response>>() {
                    @Override
                    public Observable<Response> apply(NewsSearch newsSearch) throws Exception {
                        return Observable.just(newsSearch.getResponse());
                    }
                })
                .concatMap(new Function<Response, Observable<List<Result>>>() {
                    @Override
                    public Observable<List<Result>> apply(Response response) throws Exception {
                        results = response.getResults();
                        return Observable.just(response.getResults());
                    }
                });

    }

    @Override
    public Observable<List<Result>> getNewsDataFromCache() {
        if(isUpdated()){
           return Observable.just(results);
        } else{
            lastTimeStamp = System.currentTimeMillis();
            results.clear();
            return Observable.empty();
        }
    }
}
