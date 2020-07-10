package com.nguyen.nytimessearch;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface ArticleAPI {
    // https://api.nytimes.com/svc/search/v2/articlesearch.json?q=messi&api-key=GYWXJ04BtYKmLWLwGouVEON0y34KNYgh
    @GET("articlesearch.json")
    Call<Json> fetchPage(@Query("q") String query, @Query("page") Integer page, @Query("begin_date") String beginDate, @Query("fq") String filterQuery, @Query("sort") String sort, @Query("api-key") String apiKey);
}
