package com.nguyen.nytimessearch;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nguyen.nytimessearch.databinding.ItemArticleBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by My on 2/13/2016.
 * Updated by My on 7/7/2020:
 * 1. migrated to AndroidX
 * 2. replaced ButterKnife with data binding
 * 3. replaced Volley with Retrofit
 * 4. replaced LinearLayout and RelativeLayout with ConstraintLayout
 * 5. implemented MVVM
 * 6. partially implemented Dagger DI
 */
public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ItemArticleBinding binding;

        public ViewHolder(ItemArticleBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Article article = articles.get(getLayoutPosition());
            Intent intent = DetailActivity.newIntent(context, article);
            context.startActivity(intent);
        }

        public void bind(Article article) {
            if (!TextUtils.isEmpty(article.thumbNail)) {
                Picasso.get()
                        .load(article.thumbNail)
                        .fit()
                        .into(binding.articleImage);
                        /* // use the following in case of error loading images
                        .into(binding.articleImage, new Callback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError(Exception e) {
                                e.printStackTrace();
                            }
                        });*/
            }
            binding.articleTitle.setText(article.headline);
        }
    }

    private List<Article> articles;
    private Context context;

    public ArticlesAdapter(List<Article> articles, Context context) {
        this.articles = articles;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        // binding = DataBindingUtil.inflate(inflater, R.layout.item_article, parent, false);
        ItemArticleBinding binding = ItemArticleBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Article article = articles.get(position);
        holder.bind(article);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }
}
