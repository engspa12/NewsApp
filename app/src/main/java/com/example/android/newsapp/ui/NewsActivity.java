package com.example.android.newsapp.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.example.android.newsapp.entities.Article;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.NavUtils;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.newsapp.mvp.NewsMVP;
import com.example.android.newsapp.R;
import com.example.android.newsapp.ui.adapter.ArticleAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class NewsActivity extends AppCompatActivity implements NewsMVP.View {

    public static final String LOG = NewsActivity.class.getSimpleName();

    private ArticleAdapter adapter;

    //Interface Presenter
    @Inject
    NewsMVP.Presenter presenter;

    @BindView(R.id.rootView)
    ViewGroup rootView;

    @BindView(R.id.list)
    ListView listView;

    @BindView(R.id.empty_view)
    TextView emptyView;

    @BindView(R.id.progress_bar)
    ProgressBar mProgress;

    //Word to query in Articles
    private String searchTerm;

    private String sortType;

    private List<Article> articles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Get intent
        Intent intent = getIntent();

        if(intent.hasExtra("search")) {

            if(isOnline()) {
                //Create empty list of articles
                articles = new ArrayList<>();

                //Term to use in the search of news
                searchTerm = intent.getStringExtra("search");

                //Sort type for query
                sortType = intent.getStringExtra("sort_type");

                if(sortType.equals("relevance")){
                    setTitle("Relevant News");
                } else if (sortType.equals("newest")) {
                    setTitle("Latest News");
                } else{
                    setTitle("NewsApp");
                }

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

    @Override
    protected void onResume() {
        super.onResume();
        presenter.setView(this);
        presenter.loadData(searchTerm, sortType);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Unsubscribe Disposable
        presenter.rxJavaUnsubscribe();
        articles.clear();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void updateNewsOnScreen(List<Article> list) {

        articles = list;

        //Clear Adapter
        adapter.clear();

        //In case the articles list is null, show the message No Articles Found
        emptyView.setText(R.string.no_data);
        emptyView.setVisibility(View.VISIBLE);
        mProgress.setVisibility(View.GONE);

        if (articles != null && !articles.isEmpty()) {
            //Add data to Adapter
            adapter.addAll(articles);
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showErrorMessage(String message) {
        Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show();

        //In case of error in the server or in the query
        emptyView.setText(R.string.error_message);
        emptyView.setVisibility(View.VISIBLE);
        mProgress.setVisibility(View.GONE);
    }


}
