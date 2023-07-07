package de.haw_hamburg.playopolis;

import android.content.Context;
import android.content.SharedPreferences;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AppPreferences {
    private static final String PREF_USERNAME = "username";
    private static final String PREF_USERID = "userid";
    private static final String PREF_IMAGENAME = "imagename";
    private static final String PREF_FILE_NAME = "app_preferences";
    private static final String PREF_IMAGE_ID = "123";
    private static final String PREF_GENRES_KEY = "pref_genres";
    private static final String PREF_DESCRIPTION = "pref_description";


    private SharedPreferences sharedPreferences;
    private static AppPreferences instance;

    private AppPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized AppPreferences getInstance(Context context) {
        if (instance == null) {
            instance = new AppPreferences(context);
        }
        return instance;
    }

    public void setUsername(String username) {
        sharedPreferences.edit().putString(PREF_USERNAME, username).apply();
    }

    public String getUsername() {
        return sharedPreferences.getString(PREF_USERNAME, null);
    }

    public void setUserId(JsonNode userJSON) {
        String userID = String.valueOf(userJSON.get("data").get(0).get("id"));
        sharedPreferences.edit().putString(PREF_USERID, userID).apply();
    }

    public String getUserId() {
        return sharedPreferences.getString(PREF_USERID, null);
    }

    public void setImageName(String imageName) {
        sharedPreferences.edit().putString(PREF_IMAGENAME, imageName).apply();
    }

    public String getImageName() {
        return sharedPreferences.getString(PREF_IMAGENAME, null);
    }

    public void setImageId(String imageId) {
        sharedPreferences.edit().putString(PREF_IMAGE_ID, imageId).apply();
    }

    public String getImageId() {
        return sharedPreferences.getString(PREF_IMAGE_ID, null);
    }
    public void setGenres(String[] genres) {
        Set<String> genresSet = new HashSet<>(Arrays.asList(genres));

        sharedPreferences.edit().putStringSet(PREF_GENRES_KEY, genresSet).apply();
    }

    public String[] getGenres() {
        Set<String> genresSet = sharedPreferences.getStringSet(PREF_GENRES_KEY, new HashSet<>());
        return genresSet.toArray(new String[0]);
    }
    public void setDescription(String description) {
        sharedPreferences.edit().putString(PREF_DESCRIPTION, description).apply();
    }

    public String getDescription() {
        return sharedPreferences.getString(PREF_DESCRIPTION, null);
    }
}
