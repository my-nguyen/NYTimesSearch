package com.nguyen.nytimessearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by My on 2/9/2016.
 */
public class Article implements Serializable {
   String webUrl;
   String headline;
   String thumbNail;

   public String getWebUrl() {
      return webUrl;
   }

   public String getHeadline() {
      return headline;
   }

   public String getThumbNail() {
      return thumbNail;
   }

   public Article(JSONObject jsonObject) {
      try {
         webUrl = jsonObject.getString("web_url");
         headline = jsonObject.getJSONObject("headline").getString("main");
         JSONArray multimediaJsonArray = jsonObject.getJSONArray("multimedia");
         if (multimediaJsonArray.length() > 0) {
            JSONObject multimediaJsonObject = multimediaJsonArray.getJSONObject(0);
            thumbNail = "http://www.nytimes.com/" + multimediaJsonObject.getString("url");
         }
         else
            thumbNail = "";
      }
      catch (JSONException e) {
      }
   }

   public static List<Article> fromJsonArray(JSONArray jsonArray) {
      List<Article> results = new ArrayList<>();
      for (int i = 0; i < jsonArray.length(); i++) {
         try {
            results.add(new Article(jsonArray.getJSONObject(i)));
         }
         catch (JSONException e) {
            e.printStackTrace();
         }
      }
      return results;
   }
}
