package com.nguyen.nytimessearch;

import java.io.Serializable;

/**
 * Created by My on 2/9/2016.
 */
public class Article implements Serializable {
    public String webUrl;
    public String headline;
    public String thumbNail;

    @Override
    public String toString() {
        return "Article{" +
                "webUrl='" + webUrl + '\'' +
                ", headline='" + headline + '\'' +
                ", thumbNail='" + thumbNail + '\'' +
                '}';
    }
}
