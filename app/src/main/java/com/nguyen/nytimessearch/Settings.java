package com.nguyen.nytimessearch;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by My on 2/10/2016.
 */
public class Settings implements Serializable {
   private static final long serialVersionUID = -7060210544600464481L;
   Calendar beginDate = new GregorianCalendar();
   String   sortOrder = "Newest";
   boolean  arts = true;
   boolean  fashionStyle = false;
   boolean  sports = true;

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
      String month = String.format("%02d", (beginDate.get(Calendar.MONTH) + 1));
      builder.append(beginDate.get(Calendar.YEAR))
            .append(month)
            .append(beginDate.get(Calendar.DAY_OF_MONTH))
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
