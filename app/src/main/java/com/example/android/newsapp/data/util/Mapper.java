package com.example.android.newsapp.data.util;

public interface Mapper<Dto, DomainModel> {
    DomainModel mapToDomainModel(Dto dto);
}
