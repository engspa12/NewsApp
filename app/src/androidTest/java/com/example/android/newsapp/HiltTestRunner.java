package com.example.android.newsapp;

import android.app.Application;
import android.content.Context;

import androidx.test.runner.AndroidJUnitRunner;

import dagger.hilt.android.testing.HiltTestApplication;

public final class  HiltTestRunner extends AndroidJUnitRunner {
    @Override
    public Application newApplication(ClassLoader cl, String appName, Context context)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        return super.newApplication(cl, HiltTestApplication.class.getName(), context);
    }
}
