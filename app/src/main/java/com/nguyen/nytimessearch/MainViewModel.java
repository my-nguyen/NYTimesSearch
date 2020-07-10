package com.nguyen.nytimessearch;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

public class MainViewModel extends ViewModel {
    private static final String TAG = "MainViewModel";

    private ArticleRepository repository;

    @Inject
    public MainViewModel(ArticleRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<Article>> fetchPage(String query, Integer page, String beginDate, String filterQuery, String sort, String apiKey) {
        return repository.fetchPage(query, page, beginDate, filterQuery, sort, apiKey);
    }
}