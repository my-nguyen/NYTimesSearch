package com.nguyen.nytimessearch.activities;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nguyen.nytimessearch.EndlessScrollListener;
import com.nguyen.nytimessearch.R;
import com.nguyen.nytimessearch.models.Settings;
import com.nguyen.nytimessearch.fragments.SettingsFragment;
import com.nguyen.nytimessearch.adapters.HeterogenousAdapter;
import com.nguyen.nytimessearch.models.Article;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity implements SettingsFragment.SettingsSaver {
   private final int REQUEST_CODE = 20;
   @Bind(R.id.query) EditText mQueryView;
   @Bind(R.id.search) Button mSearchButton;
   @Bind(R.id.results_recycler_view) RecyclerView mResultsView;
   Settings mSettings;
   HeterogenousAdapter mAdapter;
   List<Article> mArticles;
   String mQueryString;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      ButterKnife.bind(this);

      // setup non-View data
      mSettings = new Settings();

      // set up simple View's
      mSearchButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            mQueryString = mQueryView.getText().toString();
            // make sure query string is not empty
            if (!TextUtils.isEmpty(mQueryString)) {
               // start a fresh search
               fetchPage(0);
            }
         }
      });

      // setup ListView/RecyclerView
      GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
      mResultsView.setLayoutManager(layoutManager);
      mResultsView.addOnScrollListener(new EndlessScrollListener(layoutManager) {
         public void onLoadMore(int page, int totalItemsCount) {
            Log.i("NGUYEN", "onLoadMore() fetching page " + page);
            fetchPage(page);
            mAdapter.notifyItemRangeInserted(mAdapter.getItemCount(), mArticles.size() - 1);
         }
      });
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.menu_main, menu);
      MenuItem searchItem = menu.findItem(R.id.action_search);
      final SearchView searchView = (SearchView)MenuItemCompat.getActionView(searchItem);
      searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
         @Override
         public boolean onQueryTextSubmit(String query) {
            // perform query here
            mQueryString = query;
            fetchPage(0);
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
      // handle action bar item clicks here. the action bar will automatically handle clicks on the
      // Home/Up button, so long as you specify a parent activity in AndroidManifest.xml.
      switch (item.getItemId()) {
         case R.id.action_settings:
            SettingsFragment settingsDialog = SettingsFragment.newInstance(mSettings);
            settingsDialog.show(getSupportFragmentManager(), "SettingsDialog");
            return true;
         default:
            return super.onOptionsItemSelected(item);
      }
   }

   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
         mSettings = (Settings)data.getSerializableExtra("SETTINGS_OUT");
         Log.i("NGUYEN", "MainActivity received Settings from SettingsActivity: " + mSettings);
      }
   }

   @Override
   // this callback method receives a Settings object from SettingsFragment
   public void save(Settings settings) {
      mSettings = settings;
   }

   private void fetchPage(int page) {
      AsyncHttpClient client = new AsyncHttpClient();
      String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json";

      // build the search terms based on query and Settings
      RequestParams params = new RequestParams();
      params.put("q", mQueryString);
      if (mSettings.getBeginDate() != null)
         params.put("begin_date", mSettings.getBeginDate());
      if (mSettings.getSortOrder() != null)
         params.put("sort", mSettings.getSortOrder());
      if (mSettings.getNewsDeskValues() != null)
         params.put("fq", mSettings.getNewsDeskValues());

      // the API Key is immutable and is not part of the search terms
      params.put("api-key", "152d2daca1c285a04dd9315a4ee0b4e9:14:67352863");
      // the page number varies and is not part of the search terms
      params.put("page", "" + page);
      // clear UI on a fresh search
      if (page == 0) {
         mArticles = new ArrayList<>();
      }

      client.get(url, params, new TextHttpResponseHandler() {
         @Override
         public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
         }
         @Override
         public void onSuccess(int statusCode, Header[] headers, String responseString) {
            Log.d("NGUYEN", "onSuccess(), response: " + responseString);
            JsonElement jsonElement = new JsonParser().parse(responseString);
            JsonArray docsJsonArray = jsonElement.getAsJsonObject().getAsJsonObject("response").getAsJsonArray("docs");
            // create adapter passing in the sample user data
            mArticles.addAll(Article.fromJsonArray(docsJsonArray));
            // mAdapter = new ArticlesAdapter(mArticles);
            mAdapter = new HeterogenousAdapter(mArticles);
            // attach the adapter to the recyclerview to populate items
            mResultsView.setAdapter(mAdapter);
         }
      });
   }
}
