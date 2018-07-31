package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>> {

    //ArticleAdapter
    private ArticleAdapter adapter;

    @BindView(R.id.progress_bar) ProgressBar mProgress;
    @BindView(R.id.empty_view) TextView emptyView;

    //Base url for HTTP request
    private static final String URL_REQUEST_BASE = "http://content.guardianapis.com/search";
    //Word to query in Articles
    private String searchTerm="";
    //Number of articles to query
    private static final String NUMBER_OF_ARTICLES = "50";

    private static final String SORT_CRITERIA = "relevance";

    private static final String API_KEY = BuildConfig.API_KEY;

    private static final String FILTER_RESULTS = "headline,byline,thumbnail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        ButterKnife.bind(this);
        //Get the search word
        Intent intent = getIntent();
        searchTerm = intent.getStringExtra("search");
        //searchTerm = "\"" + searchTerm + "\"";

        final ListView listView = (ListView) findViewById(R.id.list);

        //Set EmptyView
        listView.setEmptyView(emptyView);

        //Verify if there is internet connection, if so then update the screen with the news articles
        //Otherwise show the message there is no internet connection
        ConnectivityManager cm =
                (ConnectivityManager)getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();


        if (isConnected) {
            //Declare Adapter
            adapter = new ArticleAdapter(NewsActivity.this, new ArrayList<Article>());
            //Set Adapter
            listView.setAdapter(adapter);
            //Set Listener to ListView items
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                    Article article = (Article) listView.getItemAtPosition(position);
                    String url = article.getWebUrl();
                    Uri webPage = Uri.parse(url);

                    Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }
            });

            //LoaderManager
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(1, null, this);
        }
        else{
            //Set No internet Connection message
            mProgress.setVisibility(View.GONE);
            emptyView.setText(R.string.no_internet_connection);

        }

    }

    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {

        //Create Uri object
        Uri baseUri = Uri.parse(URL_REQUEST_BASE);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        //Modify Uri to query with specific parameters
        //order-by, show-fields, page-size, q and api-key
        uriBuilder.appendQueryParameter("q",searchTerm);
        uriBuilder.appendQueryParameter(getString(R.string.order_by_query_key), SORT_CRITERIA);
        uriBuilder.appendQueryParameter(getString(R.string.show_fields_query_key), FILTER_RESULTS);
        uriBuilder.appendQueryParameter(getString(R.string.page_size_query_key), NUMBER_OF_ARTICLES);
        uriBuilder.appendQueryParameter(getString(R.string.api_key_query_key), API_KEY);

        return new ArticleLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {
        //Clear Adapter
        adapter.clear();

        //In case the articles list is null, show the message No Articles Found
        emptyView.setText(R.string.no_data);
        emptyView.setVisibility(View.VISIBLE);
        mProgress.setVisibility(View.GONE);

        if (articles != null && !articles.isEmpty()) {
            //Add articles to Adapter
            adapter.addAll(articles);
            emptyView.setVisibility(View.GONE);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        adapter.clear();
    }
}
