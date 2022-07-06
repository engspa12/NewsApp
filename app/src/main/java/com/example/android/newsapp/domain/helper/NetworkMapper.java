package com.example.android.newsapp.domain.helper;

public interface NetworkMapper<Dto, DomainModel> {
     DomainModel mapToDomainModel(Dto dto);
}
