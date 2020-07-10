package com.nguyen.nytimessearch;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {
    private static final String NYTIMES_BASE_URL = "https://api.nytimes.com/svc/search/v2/";

    @Provides
    GsonConverterFactory provideGsonConverterFactory() {
        return GsonConverterFactory.create();
    }

    @Provides
    Retrofit provideRetrofit(GsonConverterFactory factory) {
        return new Retrofit.Builder()
                .baseUrl(NYTIMES_BASE_URL)
                .addConverterFactory(factory.create())
                .build();
    }

    @Provides
    ArticleAPI provideArticleAPI(Retrofit retrofit) {
        return retrofit.create(ArticleAPI.class);
    }
}
