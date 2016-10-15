package com.nguyen.nytimessearch.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.nguyen.nytimessearch.ArticleDeserializer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by My on 2/9/2016.
 */
@Parcel
public class Article {
   public String webUrl;
   public String headline;
   public String thumbNail;

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append(headline).append("\n").append(webUrl).append("\n").append(thumbNail);
      return builder.toString();
   }

   public Article() {
   }

   public Article(JSONObject jsonObject) {
      try {
         webUrl = jsonObject.getString("web_url");
         // headline is the main field in the headline object
         headline = jsonObject.getJSONObject("headline").getString("main");
         // thumbnail is the url field of the first object in the multimedia array
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

   public static List<Article> fromJsonArray(JsonArray jsonArray) {
      // build a Gson object
      GsonBuilder builder = new GsonBuilder();
      builder.registerTypeAdapter(Article.class, new ArticleDeserializer());
      Gson gson = builder.create();

      List<Article> articles = new ArrayList<>();
      for (int i = 0; i < jsonArray.size(); i++) {
         // extract JsonObject at i from jsonArray
         JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
         // build an Article from the JsonObject, using the ArticleDeserializer
         Article article = gson.fromJson(jsonObject, Article.class);
         // add the Article into list of articles
         articles.add(article);
      }
      return articles;
   }
}
