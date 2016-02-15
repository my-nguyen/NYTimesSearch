package com.nguyen.nytimessearch;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by My on 2/14/2016.
 */
public class ViewHolder2 extends RecyclerView.ViewHolder {
   public ImageView mImage;
   public TextView  mTitle;

   public ViewHolder2(View view) {
      super(view);
      mImage = (ImageView)view.findViewById(R.id.image);
      mTitle = (TextView)view.findViewById(R.id.title);
   }
}
