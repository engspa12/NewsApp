package com.example.android.newsapp;


public class Article {


    //Instance Variables
    private String title;
    private String sectionName;
    private String author;
    private String releaseDate;
    private String webUrl;
    private String thumbnailUrl;

    //Constructor of Article
    public Article(String articleTitle, String sectionName, String author, String date,String webUrl,String thumbnailUrl){
        this.title = articleTitle;
        this.sectionName = sectionName;
        this.author = author;
        releaseDate = date;
        this.webUrl = webUrl;
        this.thumbnailUrl = thumbnailUrl;
    }

    /**
     * Getter method for the title of the article
     *
     * @return title of the article
     */
    public String getTitle(){
        return title;
    }

    /**
     * Getter method for the section name of the article
     *
     * @return section name of the article
     */
    public String getSectionName(){
        return sectionName;
    }

    /**
     * Getter method for the author of the article
     *
     * @return author of the article
     */
    public String getAuthor(){
        return author;
    }

    /**
     * Getter method for the date of the article
     *
     * @return published date of the article
     */
    public String getReleaseDate(){
        return releaseDate;
    }

    /**
     * Getter method for the url of the article
     *
     * @return url of the article
     */
    public String getWebUrl(){
        return webUrl;
    }

    public String getThumbnailUrl(){return thumbnailUrl;}
}
