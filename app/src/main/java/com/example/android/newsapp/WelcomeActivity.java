package com.example.android.newsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomeActivity extends AppCompatActivity{

    @BindView(R.id.welcome_message)
    TextView welcomeTV;

    @BindView(R.id.instructions)
    TextView instructionsTV;

    @BindView(R.id.search_button)
    Button searchButton;

    @BindView(R.id.search_et)
    EditText editText;

    private String searchTerm;

    private static final String LOG = WelcomeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        ButterKnife.bind(this);

        welcomeTV.setText(getString(R.string.welcome_message));

        instructionsTV.setText(getString(R.string.instructions));

        editText = (EditText) findViewById(R.id.search_et);

        searchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                searchTerm = editText.getText().toString().trim();
                searchTerm = searchTerm.replace(" ", " AND ");


                if(!searchTerm.equals("")) {
                    //Attach data to intent so it can be used in the query
                    Intent intent = new Intent(WelcomeActivity.this, NewsActivity.class);
                    intent.putExtra("search",searchTerm);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getBaseContext(), R.string.empty_text_message,Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
