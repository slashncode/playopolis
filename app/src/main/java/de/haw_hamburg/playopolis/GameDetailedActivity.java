package de.haw_hamburg.playopolis;

import static de.haw_hamburg.playopolis.DirectusRequests.executor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class GameDetailedActivity extends AppCompatActivity {

    private ImageView home_btn;
    private ImageView search_btn;
    private ImageView profile_btn;
    private ImageView favorite;
    private ImageView backToHome;
    private ImageView gamePreviewImage;
    private TextView gameTitle;
    private TextView gameDev;
    private TextView gameGenre;
    private TextView gameDescription;
    private TextView gameDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detailed);

        initializeViews();
        setClickListeners();

        String gameId = getIntent().getStringExtra("gameId");
        System.out.println(gameId);

        String apiUrlPath = "https://directus-se.up.railway.app/items/games/" + gameId;
        DirectusRequests.GetRequestTask getRequestTask = new DirectusRequests.GetRequestTask(executor, result -> {
            String title = String.valueOf(result.get("data").get("title")).replaceAll("\"", "");
            String genre = String.valueOf(result.get("data").get("genre")).replaceAll("\"", "");
            String publisher = String.valueOf(result.get("data").get("publisher")).replaceAll("\"", "");
            String year_published = String.valueOf(result.get("data").get("year_published")).replaceAll("\"", "");
            String description = String.valueOf(result.get("data").get("description")).replaceAll("\"", "");
            String image_id = String.valueOf(result.get("data").get("hero_image")).replaceAll("\"", "");

            runOnUiThread(() -> Glide.with(this).load("https://directus-se.up.railway.app/assets/" + image_id).into(gamePreviewImage));
            runOnUiThread(() -> gameTitle.setText(title));
            if (!description.equals("") && description != null && !description.equals("null")) {
                runOnUiThread(() -> gameDescription.setText(description));
            } else {
                runOnUiThread(() -> gameDescription.setText(""));
            }
            if (!year_published.equals("") && year_published != null && !year_published.equals("null")) {
                runOnUiThread(() -> gameDate.setText(year_published));
            } else {
                runOnUiThread(() -> gameDate.setText(""));
            }
            if (!publisher.equals("") && publisher != null && !publisher.equals("null")) {
                runOnUiThread(() -> gameDev.setText(publisher));
            } else {
                runOnUiThread(() -> gameDev.setText(""));
            }
            if (!genre.equals("") && genre != null && !genre.equals("null")) {
                runOnUiThread(() -> gameGenre.setText(genre));
            } else {
                runOnUiThread(() -> gameGenre.setText(""));
            }

        });
        getRequestTask.executeInBackground(apiUrlPath);
    }

    private void initializeViews(){
        home_btn = findViewById(R.id.homeButton);
        search_btn = findViewById(R.id.searchButton);
        favorite = findViewById(R.id.starButton);
        profile_btn = findViewById(R.id.profilButton);
        backToHome = findViewById(R.id.detail_back_btn);
        gamePreviewImage = findViewById(R.id.gamePreviewImage);
        gameTitle = findViewById(R.id.gameNameText);
        gameGenre = findViewById(R.id.genreText);
        gameDev = findViewById(R.id.devNameText);
        gameDescription = findViewById(R.id.gameDescriptionText);
        gameDate = findViewById(R.id.publishingDateText);

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