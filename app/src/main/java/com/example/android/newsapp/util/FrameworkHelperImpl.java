package com.example.android.newsapp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.android.newsapp.R;

import javax.inject.Inject;

public class FrameworkHelperImpl implements FrameworkHelper {

    private Context context;

    @Inject
    public FrameworkHelperImpl(Context context){
        this.context = context;
    }

    public boolean isOnline(){
        //Verify if there is internet connection, if so then update the screen with the news articles
        //Otherwise show the message there is no internet connection
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    public String getNoInternetMessage(){
        return context.getString(R.string.no_internet_connection);
    }

    public String getErrorMessage(){
        return context.getString(R.string.error_message);
    }
}
