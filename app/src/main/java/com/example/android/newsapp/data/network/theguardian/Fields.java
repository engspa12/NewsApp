
package com.example.android.newsapp.data.network.theguardian;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Fields {

    @SerializedName("headline")
    @Expose
    private String headline;
    @SerializedName("byline")
    @Expose
    private String byline;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;

    public String getHeadline() {
        if(headline != null){
            return headline;
        } else {
            return "No image available";
        }

    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getByline() {
        if(byline != null){
            return byline;
        } else {
            return "No image available";
        }
    }

    public void setByline(String byline) {
        this.byline = byline;
    }

    public String getThumbnail() {
        if(thumbnail != null){
            return thumbnail;
        } else {
            return "No image available";
        }
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

}
