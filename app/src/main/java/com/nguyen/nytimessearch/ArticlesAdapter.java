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
 * Created by My on 2/13/2016.
 */
// create the basic adapter extending from RecyclerView.Adapter. note that we specify the custom
// ViewHolder which gives us access to our views
public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ViewHolder> {
   // provide a direct reference to each of the views within a data item used to cache the views
   // within the item layout for fast access
   public class ViewHolder extends RecyclerView.ViewHolder {
      // your holder should contain a member variable for any view that will be set as you render a row
      // public DynamicHeightImageView  mImage;
      public ImageView  mImage;
      public TextView   mTitle;
      private Context   mContext;

      // we also create a constructor that accepts the entire item row and does the view lookups to
      // find each subview
      public ViewHolder(final Context context, View itemView) {
         // stores the itemView in a public final member variable that can be used to access the
         // context from any ViewHolder instance.
         super(itemView);
         mTitle = (TextView)itemView.findViewById(R.id.title);
         mImage = (ImageView)itemView.findViewById(R.id.image);
         // mImage = (DynamicHeightImageView)itemView.findViewById(R.id.image);
         // attach a click listener to the entire row view
         itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // create an intent to display the article
               Intent intent = new Intent(context, ArticleActivity.class);
               // get the article to display
               Article article = mArticles.get(getLayoutPosition());
               // pass that article into intent
               intent.putExtra("article", article);
               // launch the activity
               context.startActivity(intent);
            }
         });
      }
   }

   // store a member variable for the articles
   private List<Article>   mArticles;
   private Context         mContext;

   // pass in the article array into the constructor
   public ArticlesAdapter(List<Article> articles) {
      mArticles = articles;
   }

   // usually involves inflating a layout from XML and returning the holder
   @Override
   public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      mContext = parent.getContext();
      LayoutInflater inflater = LayoutInflater.from(mContext);
      // inflate the custom layout
      View contactView = inflater.inflate(R.layout.item_article, parent, false);
      // return a new holder instance
      return new ViewHolder(parent.getContext(), contactView);
   }

   // involves populating data into the item through holder
   @Override
   public void onBindViewHolder(ViewHolder holder, int position) {
      // get the data model based on position
      Article article = mArticles.get(position);
      // populate the thumbnail image by remote downloading the image in the background
      String thumbnail = article.getThumbNail();
      if (!TextUtils.isEmpty(thumbnail)) {
         // set the height ratio before loading an image into Picasso
         // holder.mImage.setHeightRatio(((double)holder.mImage.getHeight())/holder.mImage.getWidth());
         Picasso.with(mContext).load(thumbnail).into(holder.mImage);
      }
      // set article's title
      holder.mTitle.setText(article.getHeadline());;
   }

   // return the total count of items
   @Override
   public int getItemCount() {
      return mArticles.size();
   }
}
