package com.nguyen.nytimessearch;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by My on 2/15/2016.
 */
public class ArticleDeserializer implements JsonDeserializer<Article> {
   @Override
   public Article deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
      JsonObject jsonObject = json.getAsJsonObject();
      Article article = new Article();
      // webUrl is the "web_url" field in the outer JsonObject
      article.webUrl = jsonObject.get("web_url").getAsString();
      // headline is the "main" field in the embedded "headline" JsonObject
      article.headline = jsonObject.getAsJsonObject("headline").get("main").getAsString();
      // thumbnail is the "url" field of the first JsonObject in the embedded "multimedia" JsonArray
      JsonArray multimediaArray = jsonObject.getAsJsonArray("multimedia");
      // sometimes the url field is empty
      article.thumbNail = multimediaArray.size() == 0 ? null :
            "http://www.nytimes.com/" + jsonObject.getAsJsonArray("multimedia").get(0).getAsJsonObject().get("url").getAsString();
      return article;
   }
}
