package com.example.android.newsapp.data.network.mapper;

import com.example.android.newsapp.data.network.model.ArticleNetwork;
import com.example.android.newsapp.data.network.model.Fields;
import com.example.android.newsapp.domain.model.ArticleDomain;
import com.example.android.newsapp.data.util.Mapper;

public class ArticleNetworkMapper implements Mapper<ArticleNetwork, ArticleDomain> {

    @Override
    public ArticleDomain mapToDomainModel(ArticleNetwork articleNetwork) {

        String sectionName = articleNetwork.getSectionName();
        String webUrl = articleNetwork.getWebUrl();
        String publicationDate;
        String webTitle;
        String author;
        String thumbnailUrl;

        Fields fields = articleNetwork.getFields();

        try {
            publicationDate = articleNetwork.getWebPublicationDate().substring(0, 10);
        } catch (Exception e){
            publicationDate = "Date Unknown";
        }

        try {
            author = fields.getByline();
            if(author == null || author.isEmpty()){
                author = "Author Unknown";
            }
        } catch (Exception e) {
            author = "Author Unknown";
        }

        try {
            webTitle = fields.getHeadline();
            if(webTitle == null || webTitle.isEmpty()){
               webTitle = "Title Unknown";
            }
        } catch (Exception e) {
            webTitle = "Tile Unknown";
        }

        try {
            thumbnailUrl = fields.getThumbnail();
            if(thumbnailUrl == null || thumbnailUrl.isEmpty()){
                thumbnailUrl = "No image available";
            }
        } catch (Exception e) {
            thumbnailUrl = "No image available";
        }

        if(sectionName == null || sectionName.isEmpty()){
            sectionName = "Section Unknown";
        }

        if(webUrl == null){
            webUrl = "";
        }

        return new ArticleDomain(webTitle, sectionName, author, publicationDate, webUrl, thumbnailUrl);
    }
}
