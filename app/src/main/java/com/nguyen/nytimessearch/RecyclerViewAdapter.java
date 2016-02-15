package com.nguyen.nytimessearch;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by My on 2/14/2016.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
   private List<Article>   mArticles;
   private Context         mContext;
   private final int TITLE_ONLY = 0, TITLE_IMAGE = 1;

   // Provide a suitable constructor (depends on the kind of dataset)
   public RecyclerViewAdapter(List<Article> items) {
      mArticles = items;
   }

   // Return the size of your dataset (invoked by the layout manager)
   @Override
   public int getItemCount() {
      return mArticles.size();
   }

   @Override
   public int getItemViewType(int position) {
      Article article = mArticles.get(position);
      if (TextUtils.isEmpty(article.getThumbNail()))
         return TITLE_ONLY;
      else
         return TITLE_IMAGE;
   }

   /**
    * This method creates different RecyclerView.ViewHolder objects based on the item view type.\
    *
    * @param viewGroup ViewGroup container for the item
    * @param viewType type of view to be inflated
    * @return viewHolder to be inflated
    */
   @Override
   public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
      mContext = viewGroup.getContext();
      LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
      return viewType == TITLE_ONLY ?
         new ViewHolder1(inflater.inflate(R.layout.viewholder1, viewGroup, false)) :
         new ViewHolder2(inflater.inflate(R.layout.viewholder2, viewGroup, false));
   }

   /**
    * This method internally calls onBindViewHolder(ViewHolder, int) to update the
    * RecyclerView.ViewHolder contents with the item at the given position
    * and also sets up some private fields to be used by RecyclerView.
    *
    * @param viewHolder The type of RecyclerView.ViewHolder to populate
    * @param position Item position in the viewgroup.
    */
   @Override
   public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
      Article article = (Article) mArticles.get(position);
      int viewType = viewHolder.getItemViewType();
      if (viewType == TITLE_ONLY) {
         ViewHolder1 vh1 = (ViewHolder1) viewHolder;
         vh1.mTitle.setText(article.headline);
      }
      else {
         ViewHolder2 vh2 = (ViewHolder2) viewHolder;
         Picasso.with(mContext).load(article.thumbNail).into(vh2.mImage);
         vh2.mTitle.setText(article.headline);
      }
   }

   private class ViewHolder1 extends RecyclerView.ViewHolder {
      public TextView mTitle;

      public ViewHolder1(View view) {
         super(view);
         mTitle = (TextView)view.findViewById(R.id.title);
         view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startArticleActivity(getLayoutPosition());
            }
         });
      }
   }

   private class ViewHolder2 extends RecyclerView.ViewHolder {
      public ImageView mImage;
      public TextView  mTitle;

      public ViewHolder2(View view) {
         super(view);
         mImage = (ImageView)view.findViewById(R.id.image);
         mTitle = (TextView)view.findViewById(R.id.title);
         view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startArticleActivity(getLayoutPosition());
            }
         });
      }
   }

   private void startArticleActivity(int position) {
      // create an intent to display the article
      Intent intent = new Intent(mContext, ArticleActivity.class);
      // get the article to display
      Article article = mArticles.get(position);
      // pass that article into intent
      intent.putExtra("article", article);
      // launch the activity
      mContext.startActivity(intent);
   }
}
