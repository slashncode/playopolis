package de.haw_hamburg.playopolis;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.fasterxml.jackson.databind.JsonNode;

public class AppPreferences {
    private static final String PREF_USERNAME = "username";
    private static final String PREF_USERID = "userid";
    private static final String PREF_IMAGENAME = "imagename";
    private static final String PREF_FILE_NAME = "app_preferences";
    private static final String PREF_IMAGE_ID = "123";

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

}
