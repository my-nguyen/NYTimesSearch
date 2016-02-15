package com.nguyen.nytimessearch;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by My on 2/14/2016.
 */
public class ViewHolder1 extends RecyclerView.ViewHolder {
   public TextView  mTitle;

   public ViewHolder1(View view) {
      super(view);
      mTitle = (TextView)view.findViewById(R.id.title);
      /*
      view.setOnClickListener(new View.OnClickListener() {
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
      */
   }
}
