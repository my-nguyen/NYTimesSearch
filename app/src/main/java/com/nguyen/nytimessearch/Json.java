package com.nguyen.nytimessearch;

import com.google.gson.annotations.SerializedName;

import java.util.List;

class Json {
    @SerializedName("response")
    public Response response;

    class Response {
        @SerializedName("docs")
        public List<Doc> docs = null;
    }

    class Doc {
        @SerializedName("web_url")
        public String webUrl;
        @SerializedName("multimedia")
        public List<Multimedium> multimedia = null;
        @SerializedName("headline")
        public Headline headline;
    }

    class Multimedium {
        @SerializedName("subtype")
        public String subtype;
        @SerializedName("url")
        public String url;
    }

    class Headline {
        @SerializedName("main")
        public String main;
    }
}
