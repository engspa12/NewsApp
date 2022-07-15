package com.example.android.newsapp.util;

import android.content.Context;

public interface FrameworkHelper {

    public boolean isOnline();
    public String getNoInternetMessage();
    public String getErrorMessage();
}
