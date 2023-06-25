package de.haw_hamburg.playopolis;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

public class RecommendationActivity extends AppCompatActivity {

    private ImageView game1;
    private ImageView game2;
    private ImageView game3;
    private ImageView game4;
    private ImageView home;
    private SearchView search;
    private ImageView profile;
    private TextView username;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommendation_page);

        game1 = (ImageView) findViewById(R.id.game1);
        game2 = (ImageView) findViewById(R.id.game2);
        game3 = (ImageView) findViewById(R.id.game3);
        game4 = (ImageView) findViewById(R.id.game4);

        Glide.with(this).load("https://directus-se.up.railway.app/assets/1fc08ec4-cb1b-4475-ba4a-b7485dfc4ee8").centerCrop().into(game1);
        Glide.with(this).load("https://directus-se.up.railway.app/assets/97127f04-aebc-4418-9874-1d845e9922a8").centerCrop().into(game2);
        Glide.with(this).load("https://directus-se.up.railway.app/assets/4b9a81c1-1c04-4215-90f6-9552bb73eb7a").centerCrop().into(game3);
        Glide.with(this).load("https://directus-se.up.railway.app/assets/9c6ad53f-36f5-4a88-acf7-d3966cbc2bca").centerCrop().into(game4);

        game1.setOnClickListener(v -> openGame_detailedActivity());
        game2.setOnClickListener(v -> openGame_detailedActivity());
        game3.setOnClickListener(v -> openGame_detailedActivity());
        game4.setOnClickListener(v -> openGame_detailedActivity());

        home = (ImageView) findViewById(R.id.homeButton);
        home.setOnClickListener(v -> recreate());

        search = (SearchView) findViewById(R.id.searchGame);
        search.setOnClickListener(v -> recreate());

        profile = (ImageView) findViewById(R.id.profilButton);
        profile.setOnClickListener(v -> openProfileActivity());

        username = (TextView) findViewById(R.id.profilUserName);
        Intent intent = getIntent();
        String loginUsername = intent.getStringExtra("username");
        username.setText(loginUsername);
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
