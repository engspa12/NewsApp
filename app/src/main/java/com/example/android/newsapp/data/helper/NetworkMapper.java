package com.example.android.newsapp.data.helper;

public interface NetworkMapper<Dto, DomainModel> {
     DomainModel mapToDomainModel(Dto dto);
}
