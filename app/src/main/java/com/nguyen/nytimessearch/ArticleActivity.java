package com.nguyen.nytimessearch;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ArticleActivity extends AppCompatActivity {

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_article);

      Article article = (Article)getIntent().getSerializableExtra("article");
      WebView webView = (WebView)findViewById(R.id.article);
      // set up to open WebView and not a browser
      webView.setWebViewClient(new WebViewClient() {
         @Override
         public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
         }
      });
      webView.loadUrl(article.getWebUrl());
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.menu_activity, menu);
      MenuItem shareItem = menu.findItem(R.id.action_share);
      ShareActionProvider shareActionProvider = (ShareActionProvider)MenuItemCompat.getActionProvider(shareItem);
      Intent shareIntent = new Intent(Intent.ACTION_SEND);
      shareIntent.setType("text/plain");
      // get reference to WebView
      WebView webView = (WebView)findViewById(R.id.article);
      // pass in the URL currently being used by the WebView
      shareIntent.putExtra(Intent.EXTRA_TEXT, webView.getUrl());
      shareActionProvider.setShareIntent(shareIntent);

      return super.onCreateOptionsMenu(menu);
   }
}
