package com.example.android.newsapp.presentation.model;

import androidx.annotation.Nullable;

import com.example.android.newsapp.domain.model.ArticleDomain;

public class ArticleView {

    //Instance Variables
    private String title;
    private String sectionName;
    private String author;
    private String releaseDate;
    private String webUrl;
    private String thumbnailUrl;

    public ArticleView(String articleTitle, String sectionName, String author, String date, String webUrl, String thumbnailUrl){
        this.title = articleTitle;
        this.sectionName = sectionName;
        this.author = author;
        this.releaseDate = date;
        this.webUrl = webUrl;
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getAuthor() {
        return author;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    @Override
    public boolean equals(@Nullable Object obj) {

        if(this.getClass() != (obj != null ? obj.getClass() : null)){
            return false;
        }

        ArticleView objArticle = (ArticleView) obj;

        if(!title.equals(objArticle.title)){
            return false;
        }

        if(!sectionName.equals(objArticle.sectionName)){
            return false;
        }

        if(!author.equals(objArticle.author)){
            return false;
        }

        if(!releaseDate.equals(objArticle.releaseDate)){
            return false;
        }

        if(!webUrl.equals(objArticle.webUrl)){
            return false;
        }

        if(!thumbnailUrl.equals(objArticle.thumbnailUrl)){
            return false;
        }

        return true;
    }
}
