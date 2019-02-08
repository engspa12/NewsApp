package com.example.android.newsapp.news;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.newsapp.BuildConfig;
import com.example.android.newsapp.R;
import com.example.android.newsapp.http.theguardian.Fields;
import com.example.android.newsapp.http.NewsAPI;
import com.example.android.newsapp.http.theguardian.NewsSearch;
import com.example.android.newsapp.http.theguardian.Response;
import com.example.android.newsapp.http.theguardian.Result;
import com.example.android.newsapp.news.adapter.ArticleAdapter;
import com.example.android.newsapp.root.App;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class NewsActivity extends AppCompatActivity {

    public static final String LOG = NewsActivity.class.getSimpleName();

    private ArticleAdapter adapter;

    @Inject
    NewsAPI newsAPI;

    @BindView(R.id.progress_bar)
    ProgressBar mProgress;

    @BindView(R.id.empty_view)
    TextView emptyView;

    @BindView(R.id.list)
    ListView listView;

    //Word to query in Articles
    private String searchTerm = "";
    //Number of articles to query
    private static final String NUMBER_OF_ARTICLES = "50";

    private static final String SORT_CRITERIA = "relevance";

    private static final String API_KEY = BuildConfig.API_KEY;

    private static final String FILTER_RESULTS = "headline,byline,thumbnail";

    private List<Article> articles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        ButterKnife.bind(this);

        ((App) getApplication()).getComponent().inject(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Get intent
        Intent intent = getIntent();

        if(intent.hasExtra("search")) {

            if(isOnline()) {
                //Create empty list of articles
                articles = new ArrayList<>();

                //Term to use in the search of news
                searchTerm = intent.getStringExtra("search");

                //Set EmptyView
                listView.setEmptyView(emptyView);

                //Declare Adapter
                adapter = new ArticleAdapter(this, new ArrayList<Article>());

                //Set Adapter
                listView.setAdapter(adapter);

                //Set Listener to ListView items
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        //Get article URL
                        Article article = (Article) listView.getItemAtPosition(position);
                        String url = article.getWebUrl();
                        Uri webPage = Uri.parse(url);

                        //Show article in browser
                        Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }
                    }
                });

                //Map for querying the API
                Map<String, String> parameters = new HashMap<>();
                parameters.put("q", searchTerm);
                parameters.put(getString(R.string.order_by_query_key), SORT_CRITERIA);
                parameters.put(getString(R.string.show_fields_query_key), FILTER_RESULTS);
                parameters.put(getString(R.string.page_size_query_key), NUMBER_OF_ARTICLES);
                parameters.put(getString(R.string.api_key_query_key), API_KEY);

                //Create Observable
                Observable<NewsSearch> newsApiObservable = newsAPI.getNews(parameters);

                newsApiObservable
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
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Result>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Result result) {

                                String sectionName = result.getSectionName();
                                String webUrl = result.getWebUrl();
                                String publicationDate = result.getWebPublicationDate().substring(0, 10);
                                String webTitle;
                                String author;
                                String thumbnailUrl;

                                try {
                                    //Get JSONObject
                                    Fields fields = result.getFields();
                                    //Parse the JSON response from the byline key
                                    try {
                                        author = fields.getByline();
                                    } catch (Exception e) {
                                        author = "unknown author";
                                    }

                                    try {
                                        webTitle = fields.getHeadline();
                                    } catch (Exception e) {
                                        webTitle = "Unknown title";
                                    }

                                    try {
                                        thumbnailUrl = fields.getThumbnail();
                                    } catch (Exception e) {
                                        thumbnailUrl = "No image available";
                                    }
                                } catch (Exception e) {
                                    //Handle Exception in case there is no data for fields JSON Object
                                    author = "unknown author";
                                    webTitle = "Unknown title";
                                    thumbnailUrl = "No image available";
                                }

                                articles.add(new Article(webTitle, sectionName, author, publicationDate, webUrl, thumbnailUrl));
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(LOG, e.getMessage());
                            }

                            @Override
                            public void onComplete() {
                                //Clear Adapter
                                adapter.clear();

                                //In case the articles list is null, show the message No Articles Found
                                emptyView.setText(R.string.no_data);
                                emptyView.setVisibility(View.VISIBLE);
                                mProgress.setVisibility(View.GONE);

                                if (articles != null && !articles.isEmpty()) {
                                    adapter.addAll(articles);
                                    emptyView.setVisibility(View.GONE);
                                }
                            }
                        });


            } else{
                //Set No internet Connection message
                mProgress.setVisibility(View.GONE);
                emptyView.setText(R.string.no_internet_connection);
            }
        }


    }

    private boolean isOnline() {
        //Verify if there is internet connection, if so then update the screen with the news articles
        //Otherwise show the message there is no internet connection
        ConnectivityManager cm =
                (ConnectivityManager)getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
