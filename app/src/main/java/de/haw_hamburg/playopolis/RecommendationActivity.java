package de.haw_hamburg.playopolis;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class RecommendationActivity extends AppCompatActivity {

    private SearchView searchView;
    private ImageView game1;
    private ImageView game2;
    private ImageView game3;
    private ImageView game4;
    private ImageView home;
    private ImageView search;
    private ImageView profile;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommendation_page);

        game1 = (ImageView) findViewById(R.id.game1);
        game2 = (ImageView) findViewById(R.id.game2);
        game3 = (ImageView) findViewById(R.id.game3);
        game4 = (ImageView) findViewById(R.id.game4);

        game1.setOnClickListener(v -> openGame_detailedActivity());
        game2.setOnClickListener(v -> openGame_detailedActivity());
        game3.setOnClickListener(v -> openGame_detailedActivity());
        game4.setOnClickListener(v -> openGame_detailedActivity());

        home = (ImageView) findViewById(R.id.homeButton);
        home.setOnClickListener(v -> recreate());

        search = (ImageView) findViewById(R.id.searchGame);
        search.setOnClickListener(v -> recreate());

        profile = (ImageView) findViewById(R.id.profilButton);
        profile.setOnClickListener(v -> openProfileActivity());

    }

    private void openProfileActivity() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    private void openGame_detailedActivity() {
        Intent intent = new Intent(this, Game_detailedActivity.class);
        startActivity(intent);
    }
}
