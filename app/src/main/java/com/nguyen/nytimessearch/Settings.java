package com.nguyen.nytimessearch;

import org.parceler.Parcel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by My on 2/10/2016.
 */
@Parcel
public class Settings {
   Date     beginDate = null;
   String   sortOrder = null;
   boolean  arts = false;
   boolean  fashionStyle = false;
   boolean  sports = false;

   public Settings() {
   }

   public String getBeginDate() {
      return beginDate == null ? null : beginDate.getString();
   }

   public String getSortOrder() {
      return sortOrder == null ? null : (sortOrder.substring(0, 1).toLowerCase() + sortOrder.substring(1));
   }

   public String getNewsDeskValues() {
      if (!arts && !fashionStyle && !sports)
         return null;
      else {
         List<String> values = new ArrayList<>();
         if (arts)
            values.add("\"Arts\"");
         if (fashionStyle)
            values.add("\"Fashion & Style\"");
         if (sports)
            values.add("\"Sports\"");

         StringBuilder builder = new StringBuilder();
         builder.append("news_desk:(");
         builder.append(values.get(0));
         for (int i = 1; i < values.size(); i++)
            builder.append(" ").append(values.get(i));
         builder.append(")");
         return builder.toString();
      }
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append(beginDate)
            .append(" ")
            .append(sortOrder)
            .append(" ")
            .append(arts)
            .append(" ")
            .append(fashionStyle)
            .append(" ")
            .append(sports);
      return builder.toString();
   }
}
