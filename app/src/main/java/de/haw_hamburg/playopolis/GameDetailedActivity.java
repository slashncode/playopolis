package de.haw_hamburg.playopolis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class GameDetailedActivity extends AppCompatActivity {

    private ImageView home_btn;
    private ImageView search_btn;
    private ImageView profile_btn;
    private ImageView favorite;
    private ImageView backToHome;
    private ImageView gamePreviewImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detailed);

        initializeViews();
        setClickListeners();

        Glide.with(this).load("https://directus-se.up.railway.app/assets/9c6ad53f-36f5-4a88-acf7-d3966cbc2bca").into(gamePreviewImage);
    }

    private void initializeViews(){
        home_btn = findViewById(R.id.homeButton);
        search_btn = findViewById(R.id.searchButton);
        favorite = findViewById(R.id.starButton);
        profile_btn = findViewById(R.id.profilButton);
        backToHome = findViewById(R.id.detail_back_btn);
        gamePreviewImage = findViewById(R.id.gamePreviewImage);
    }
    private void setClickListeners(){
        backToHome.setOnClickListener(v -> openRecommendationActivity());
        home_btn.setOnClickListener(v -> openRecommendationActivity());
        search_btn.setOnClickListener(v -> openSearchActivity());
        profile_btn.setOnClickListener(v -> openProfileActivity());
        favorite.setOnClickListener(v -> toggleFavoriteImage());
    }

    private void openRecommendationActivity(){
        Intent intent = new Intent(this, RecommendationActivity.class);
        startActivity(intent);
    }
    private void openSearchActivity(){
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }
    private void openProfileActivity(){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
    private void toggleFavoriteImage() {
        // Get the current drawable of the favorite ImageView
        Drawable currentDrawable = favorite.getDrawable();

        // Determine the new image resource based on the current resource
        int newImageResource = (currentDrawable.getConstantState().equals(getResources().getDrawable(R.drawable.star_off).getConstantState()))
                ? R.drawable.star_on
                : R.drawable.star_off;

        // Set the new image resource to the favorite ImageView
        favorite.setImageResource(newImageResource);
    }
}