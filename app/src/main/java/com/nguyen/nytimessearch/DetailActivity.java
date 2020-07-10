package com.nguyen.nytimessearch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.core.view.MenuItemCompat;
import androidx.databinding.DataBindingUtil;

import com.nguyen.nytimessearch.databinding.ActivityDetailBinding;

/*
 * Updated by My on 7/7/2020:
 * 1. migrated to AndroidX
 * 2. replaced ButterKnife with data binding
 * 3. replaced Volley with Retrofit
 * 4. replaced LinearLayout and RelativeLayout with ConstraintLayout
 * 5. implemented MVVM
 * 6. partially implemented Dagger DI
 */
public class DetailActivity extends AppCompatActivity {
    private static final String EXTRA_ARTICLE_OBJECT = "ARTICLE_OBJECT";

    ActivityDetailBinding binding;

    public static Intent newIntent(Context context, Article article) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(EXTRA_ARTICLE_OBJECT, article);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Article article = (Article)getIntent().getSerializableExtra(EXTRA_ARTICLE_OBJECT);
        // set up to open WebView and not a browser
        binding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        binding.webView.loadUrl(article.webUrl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity, menu);
        MenuItem shareItem = menu.findItem(R.id.action_share);
        ShareActionProvider shareActionProvider = (ShareActionProvider)MenuItemCompat.getActionProvider(shareItem);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, binding.webView.getUrl());
        shareActionProvider.setShareIntent(shareIntent);

        return super.onCreateOptionsMenu(menu);
    }
}
