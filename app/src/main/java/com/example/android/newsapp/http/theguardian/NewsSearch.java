
package com.example.android.newsapp.http.theguardian;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewsSearch {

    @SerializedName("response")
    @Expose
    private Response response;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

}
