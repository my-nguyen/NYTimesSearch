package com.nguyen.nytimessearch;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
class ArticleRepository {
    private static final String TAG = "ArticleRepository";

    private ArticleAPI articleAPI;

    @Inject
    ArticleRepository(ArticleAPI articleAPI) {
        this.articleAPI = articleAPI;
    }

    public LiveData<List<Article>> fetchPage(String query, Integer page, String beginDate, String filterQuery, String sort, String apiKey) {
        Log.d(TAG, "fetchPage: query: " + query + ", page: " + page + ", beginDate: " + beginDate + ", filterQuery: " + filterQuery + ", sort: " + sort);
        final MutableLiveData<List<Article>> data = new MutableLiveData<>();
        articleAPI.fetchPage(query, page, beginDate, filterQuery, sort, apiKey).enqueue(new Callback<Json>() {
            @Override
            public void onResponse(Call<Json> call, Response<Json> response) {
                List<Article> articles = new ArrayList<>();
                if (response.body() != null) {
                    for (Json.Doc doc : response.body().response.docs) {
                        Article article = new Article();
                        article.webUrl = doc.webUrl;
                        article.headline = doc.headline.main;
                        for (Json.Multimedium multimedium : doc.multimedia) {
                            if (multimedium.subtype.equals("thumbnail")) {
                                article.thumbNail = "http://www.nytimes.com/" + multimedium.url;
                                break;
                            }
                        }
                        articles.add(article);
                    }
                }

                Log.d(TAG, "articles: " + articles.size());
                data.setValue(articles);
            }

            @Override
            public void onFailure(Call<Json> call, Throwable t) {
            }
        });
        return data;
    }
}
