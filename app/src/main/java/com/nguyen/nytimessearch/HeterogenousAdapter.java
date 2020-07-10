package com.nguyen.nytimessearch;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.nguyen.nytimessearch.databinding.ViewHolderTitleOnlyBinding;
import com.nguyen.nytimessearch.databinding.ViewHolderTitleWithImageBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by My on 2/14/2016.
 * Updated by My on 7/7/2020:
 * 1. migrated to AndroidX
 * 2. replaced ButterKnife with data binding
 * 3. replaced Volley with Retrofit
 * 4. replaced LinearLayout and RelativeLayout with ConstraintLayout
 * 5. implemented MVVM
 * 6. partially implemented Dagger DI
 */
public class HeterogenousAdapter extends RecyclerView.Adapter<HeterogenousAdapter.ViewHolder> {

    private static final String TAG = "HeterogenousAdapter";
    private static final int TITLE_ONLY = 0;
    private static final int TITLE_WITH_IMAGE = 1;

    class ViewHolder extends RecyclerView.ViewHolder {
        private ViewHolderTitleOnlyBinding titleOnlyBinding;
        private ViewHolderTitleWithImageBinding titleWithImageBinding;

        ViewHolder(ViewHolderTitleOnlyBinding binding) {
            super(binding.getRoot());
            titleOnlyBinding = binding;
        }

        ViewHolder(ViewHolderTitleWithImageBinding binding) {
            super(binding.getRoot());
            titleWithImageBinding = binding;
        }
    }

    private List<Article> articles;

    public HeterogenousAdapter(List<Article> items) {
        articles = items;
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    @Override
    public int getItemViewType(int position) {
        Article article = articles.get(position);
        return TextUtils.isEmpty(article.thumbNail) ? TITLE_ONLY : TITLE_WITH_IMAGE;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding;
        switch (viewType) {
            case TITLE_ONLY:
                binding = DataBindingUtil.inflate(inflater, R.layout.view_holder_title_only, parent, false);
                return new ViewHolder((ViewHolderTitleOnlyBinding) binding);
            default:
                binding = DataBindingUtil.inflate(inflater, R.layout.view_holder_title_with_image, parent, false);
                return new ViewHolder((ViewHolderTitleWithImageBinding) binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Article article = (Article) articles.get(position);
        switch (holder.getItemViewType()) {
            case TITLE_ONLY:
                holder.titleOnlyBinding.titleOnly.setText(article.headline);
                break;
            case TITLE_WITH_IMAGE:
                holder.titleWithImageBinding.titleWithImage.setText(article.headline);
                Log.d(TAG, "thumbNail: " + article.thumbNail);
                Picasso.get()
                        .load(article.thumbNail)
                        // .resize(width, 0)
                        // .placeholder(R.drawable.homer)
                        .into(holder.titleWithImageBinding.image);
                break;
        }
    }
}
