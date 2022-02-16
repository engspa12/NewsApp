package com.example.android.newsapp.presentation.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.android.newsapp.R;
import com.google.android.material.snackbar.Snackbar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomeActivity extends AppCompatActivity {

    @BindView(R.id.search_button)
    Button searchButton;

    @BindView(R.id.search_et)
    EditText editText;

    private String searchTerm;
    private String sortType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        ButterKnife.bind(this);

        sortType = "relevance";

        editText = (EditText) findViewById(R.id.search_et);

        searchButton.setOnClickListener(view -> {
            searchTerm = editText.getText().toString().trim();
            searchTerm = searchTerm.replace(" ", " AND ");

            if(!searchTerm.equals("")) {
                //Attach data to intent so it can be used in the query
                Intent intent = new Intent(WelcomeActivity.this, NewsActivity.class);
                intent.putExtra("search",searchTerm);
                intent.putExtra("sort_type", sortType);
                startActivity(intent);
            } else {
                Snackbar.make(searchButton, R.string.empty_text_message, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if(item.getItemId() == R.id.relevant_news){
            searchButton.setText(getString(R.string.search_relevant_news_text));
            sortType = "relevance";
            return true;
        } else if (item.getItemId() == R.id.latest_news) {
            searchButton.setText(getString(R.string.search_latest_news_text));
            sortType = "newest";
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
