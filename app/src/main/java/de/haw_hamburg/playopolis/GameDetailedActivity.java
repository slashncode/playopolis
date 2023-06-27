package de.haw_hamburg.playopolis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class GameDetailedActivity extends AppCompatActivity {

    private ImageView home_btn;
    private ImageView search_btn;
    private ImageView profile_btn;
    private ImageView favorite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detailed);

        initializeViews();
        setClickListeners();
    }

    private void initializeViews(){
        home_btn = findViewById(R.id.homeButton);
        search_btn = findViewById(R.id.searchButton);
        favorite = findViewById(R.id.starButton);
        profile_btn = findViewById(R.id.profilButton);
    }
    private void setClickListeners(){
        home_btn.setOnClickListener(v -> openRecommendationActivity());
        search_btn.setOnClickListener(v -> openSearchActivity());
        profile_btn.setOnClickListener(v -> openProfileActivity());
        //TODO: set onClick Listener for favorite Button
        favorite.setOnClickListener(v -> {

        });
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
}