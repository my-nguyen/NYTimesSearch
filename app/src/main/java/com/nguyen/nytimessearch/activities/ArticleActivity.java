package com.nguyen.nytimessearch.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.nguyen.nytimessearch.R;
import com.nguyen.nytimessearch.models.Article;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ArticleActivity extends AppCompatActivity {
   @Bind(R.id.article) WebView mWebView;

   public static Intent newIntent(Context context, Article article) {
      // create an intent to display the article
      Intent intent = new Intent(context, ArticleActivity.class);
      // pass Article into intent. even though Article is Serializable, calling putExtra() will do
      intent.putExtra("ARTICLE_IN", Parcels.wrap(article));
      return intent;
   }

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_article);
      ButterKnife.bind(this);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);

      // extract Article from Intent. since Article is Serializable, we need to call getSerializableExtra()
      // Article article = (Article)getIntent().getSerializableExtra("ARTICLE_IN");
      Article article = (Article)Parcels.unwrap(getIntent().getParcelableExtra("ARTICLE_IN"));
      // WebView mWebView = (WebView)findViewById(R.id.article);
      // set up to open WebView and not a browser
      mWebView.setWebViewClient(new WebViewClient() {
         @Override
         public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
         }
      });
      mWebView.loadUrl(article.webUrl);
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.menu_activity, menu);
      MenuItem shareItem = menu.findItem(R.id.action_share);
      ShareActionProvider shareActionProvider = (ShareActionProvider)MenuItemCompat.getActionProvider(shareItem);
      Intent shareIntent = new Intent(Intent.ACTION_SEND);
      shareIntent.setType("text/plain");
      // pass in the URL currently being used by the WebView
      shareIntent.putExtra(Intent.EXTRA_TEXT, mWebView.getUrl());
      shareActionProvider.setShareIntent(shareIntent);

      return super.onCreateOptionsMenu(menu);
   }
}
