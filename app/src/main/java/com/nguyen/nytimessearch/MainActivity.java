package com.nguyen.nytimessearch;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;

import com.nguyen.nytimessearch.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements SettingsFragment.DialogListener {
    private static final String TAG = "MainActivity";
    private static final String NYTIMES_API_KEY = "GYWXJ04BtYKmLWLwGouVEON0y34KNYgh";

    @Inject
    MainViewModel mainViewModel;
    @Inject
    Settings settings;

    private ActivityMainBinding binding;
    // private HeterogenousAdapter adapter;
    private ArticlesAdapter adapter;
    private List<Article> articles;
    private int page;
    private String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((MyApplication)getApplicationContext()).appComponent.inject(this);
        super.onCreate(savedInstanceState);

        // mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        articles = new ArrayList<>();
        // adapter = new HeterogenousAdapter(MainActivity.this.articles);
        adapter = new ArticlesAdapter(articles, this);
        binding.recyclerView.setAdapter(adapter);
        binding.queryBox.requestFocus();

        binding.searchButton.setOnClickListener(v -> {
            query = binding.queryBox.getText().toString();
            if (!TextUtils.isEmpty(query)) {
                page = 0;
                fetchPage();
                hideKeyboard();
            }
        });

        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        binding.recyclerView.setLayoutManager(layoutManager);
        /*// disable EndlessRecyclerViewScrollListener for now, since a query may not yield any result
        // and not filling the screen will cause this to be called infinitely
        binding.recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                fetchPage();
                adapter.notifyItemRangeInserted(adapter.getItemCount(), articles.size() - 1);
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                MainActivity.this.query = query;
                page = 0;
                fetchPage();
                // workaround to avoid issues with some emulators and keyboard devices firing twice if
                // a keyboard enter is used. see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                SettingsFragment settingsDialog = SettingsFragment.newInstance(settings);
                settingsDialog.show(getSupportFragmentManager(), "SettingsDialog");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void fetchPage() {
        mainViewModel.fetchPage(query, page, settings.getBeginDate(), settings.getFilterQuery(), settings.getSortOrder(), NYTIMES_API_KEY).observe(this, new Observer<List<Article>>() {
            @Override
            public void onChanged(List<Article> data) {
                if (data.size() != 0) {
                    if (page == 0) {
                        int size = articles.size();
                        // reset the adapter if this is a new query and the current dataset is not empty
                        if (size != 0) {
                            articles.clear();
                            adapter.notifyItemRangeRemoved(0, size);
                        }
                    }
                    int size = articles.size();
                    articles.addAll(data);
                    adapter.notifyItemRangeInserted(size, data.size());
                    // keep fetching on if current batch contains 10 articles
                    if (data.size() == 10) {
                        page++;
                        Log.d(TAG, "fetchPage, page: " + page);
                        if (page % 3 != 0) {
                            // each NYTimes API call returns 10 articles, and the device screen fits
                            // 3 times as many articles
                            fetchPage();
                        }
                    }
                }
            }
        });
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    // receive data returned from SettingsFragment
    @Override
    public void onFinish(Settings settings) {
        this.settings = settings;
    }
}
