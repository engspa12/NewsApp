package com.example.android.newsapp.di;

import com.example.android.newsapp.data.network.model.ArticleNetwork;
import com.example.android.newsapp.data.network.mapper.ArticleNetworkMapper;
import com.example.android.newsapp.data.helper.NetworkMapper;
import com.example.android.newsapp.domain.model.ArticleDomain;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class MapperModule {

    @Provides
    public NetworkMapper<ArticleNetwork, ArticleDomain> getNetworkMapper(){
        return new ArticleNetworkMapper();
    }
}
