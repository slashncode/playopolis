package de.haw_hamburg.playopolis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class SearchActivity extends AppCompatActivity {

    private ImageView home_btn;
    private ImageView profile_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initializeViews();
        setClickListeners();
    }
    private void initializeViews(){
        home_btn = (ImageView) findViewById(R.id.homeButton);
        profile_btn = (ImageView) findViewById(R.id.profilButton);
    }
    private void setClickListeners(){
        home_btn.setOnClickListener(v -> openRecommendationActivity());
        profile_btn.setOnClickListener(v -> openProfileActivity());
    }

    private void openRecommendationActivity(){
        Intent intent = new Intent(this, RecommendationActivity.class);
        startActivity(intent);
    }
    private void openProfileActivity(){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
}