package de.haw_hamburg.playopolis;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class RecommendationActivity extends AppCompatActivity {

    private ImageView search_btn;
    private ImageView profile_btn;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.recommendation_page);

        initializeViews();
        setClickListeners();
    }
    private void initializeViews(){
        search_btn = (ImageView) findViewById(R.id.searchButton);
        profile_btn = (ImageView) findViewById(R.id.profilButton);
    }
    private void setClickListeners(){
        search_btn.setOnClickListener(v -> openSearchActivity());
        profile_btn.setOnClickListener(v -> openProfileActivity());
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
