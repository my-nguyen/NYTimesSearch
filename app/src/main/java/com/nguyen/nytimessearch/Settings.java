package com.nguyen.nytimessearch;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by My on 2/10/2016.
 */
public class Settings implements Serializable {
   Date     beginDate = new Date();
   String   sortOrder = "Newest";
   boolean  arts = true;
   boolean  fashionStyle = false;
   boolean  sports = true;

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
