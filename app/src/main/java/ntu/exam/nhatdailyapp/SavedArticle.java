package ntu.exam.nhatdailyapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SavedArticle {
    private static final String PREF_NAME = "saved_articles";
    private static final String KEY_LIST = "article_list";

    public static void saveArticle(Context context, Article article) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();

        Set<String> savedJsons = prefs.getStringSet(KEY_LIST, new HashSet<>());
        savedJsons.add(gson.toJson(article));

        prefs.edit().putStringSet(KEY_LIST, savedJsons).apply();
    }

    public static void removeArticle(Context context, Article article) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();

        Set<String> savedJsons = new HashSet<>(prefs.getStringSet(KEY_LIST, new HashSet<>()));
        savedJsons.remove(gson.toJson(article));

        prefs.edit().putStringSet(KEY_LIST, savedJsons).apply();
    }

    public static List<Article> getSavedArticles(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Set<String> savedJsons = prefs.getStringSet(KEY_LIST, new HashSet<>());
        Gson gson = new Gson();

        List<Article> list = new ArrayList<>();
        for (String json : savedJsons) {
            list.add(gson.fromJson(json, Article.class));
        }
        return list;
    }

    public static boolean isSaved(Context context, Article article) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Set<String> savedJsons = prefs.getStringSet(KEY_LIST, new HashSet<>());
        Gson gson = new Gson();
        return savedJsons.contains(gson.toJson(article));
    }
}

