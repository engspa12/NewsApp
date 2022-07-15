package com.example.android.newsapp.data.repository;

import com.example.android.newsapp.BuildConfig;
import com.example.android.newsapp.data.network.datasource.NewsService;
import com.example.android.newsapp.data.network.model.NewsSearch;
import com.example.android.newsapp.data.network.model.Response;
import com.example.android.newsapp.data.network.model.ArticleNetwork;
import com.example.android.newsapp.data.helper.NetworkMapper;
import com.example.android.newsapp.domain.model.ArticleDomain;
import com.example.android.newsapp.domain.repository.NewsRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

public class NewsRepositoryImpl implements NewsRepository {

    private final NewsService newsService;
    private final NetworkMapper<ArticleNetwork, ArticleDomain> networkMapper;

    /*Simulate the data in Cache*/
    private List<ArticleNetwork> results;

    private long lastTimeStamp;

    //Cache lifetime
    private static final long CACHE_LIFETIME = 20 * 1000;

    //Number of articles to query
    private static final String NUMBER_OF_ARTICLES = "50";
    private static final String API_KEY = BuildConfig.API_KEY;
    private static final String FILTER_RESULTS = "headline,byline,thumbnail";

    @Inject
    public NewsRepositoryImpl(NewsService newsService, NetworkMapper<ArticleNetwork, ArticleDomain> networkMapper){
        this.newsService = newsService;
        this.networkMapper = networkMapper;
        this.results = new ArrayList<>();
    }

    public boolean isUpdated(){
        return (System.currentTimeMillis() - lastTimeStamp) < CACHE_LIFETIME;
    }

    @Override
    public Observable<List<ArticleDomain>> getNewsData(String searchTerm, String sortType) {

        //Map for querying the API
        Map<String, String> parameters = new HashMap<>();

        parameters.put("q", searchTerm);
        parameters.put("order-by", sortType);
        parameters.put("show-fields", FILTER_RESULTS);
        parameters.put("page-size", NUMBER_OF_ARTICLES);
        parameters.put("api-key", API_KEY);

        return getNewsDataFromCache().switchIfEmpty(getNewsDataFromNetwork(parameters)).concatMap(new Function<List<ArticleNetwork>, ObservableSource<? extends List<ArticleDomain>>>() {
            @Override
            public ObservableSource<? extends List<ArticleDomain>> apply(List<ArticleNetwork> articleNetworkItems) throws Exception {

                List<ArticleDomain> tempList = new ArrayList<>();

                for(ArticleNetwork item: articleNetworkItems){
                    tempList.add(networkMapper.mapToDomainModel(item));
                }
                return Observable.just(tempList);
            }
        });
    }

    private Observable<List<ArticleNetwork>> getNewsDataFromNetwork(Map<String, String> queryParams) {

        //Create Observable
        Observable<NewsSearch> newsApiObservable = newsService.getNews(queryParams);

        return newsApiObservable
                .concatMap(new Function<NewsSearch, Observable<Response>>() {
                    @Override
                    public Observable<Response> apply(NewsSearch newsSearch) throws Exception {
                        return Observable.just(newsSearch.getResponse());
                    }
                })
                .concatMap(new Function<Response, Observable<List<ArticleNetwork>>>() {
                    @Override
                    public Observable<List<ArticleNetwork>> apply(Response response) throws Exception {
                        results = response.getResults();
                        return Observable.just(response.getResults());
                    }
                });

    }

    private Observable<List<ArticleNetwork>> getNewsDataFromCache() {
        if(isUpdated()){
           return Observable.just(results);
        } else{
            lastTimeStamp = System.currentTimeMillis();
            results.clear();
            return Observable.empty();
        }
    }
}
