package de.haw_hamburg.playopolis;

import static de.haw_hamburg.playopolis.DirectusRequests.executor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.fasterxml.jackson.databind.JsonNode;

public class RecommendationActivity extends AppCompatActivity {

    private LinearLayout gamesLinear1;
    private LinearLayout gamesLayout;
    private ImageView home;
    private SearchView search;
    private TextView username;
    //NAV BAR
    private ImageView search_btn;
    private ImageView profile_btn;
    private ImageView profile_img;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommendation_page);

        initializeViews();
        setClickListeners();

        loadGames();

        String imageId = AppPreferences.getInstance(this).getImageId();
        String newImageId = imageId.substring(1, imageId.length() - 1);
        Glide.with(this).load("https://directus-se.up.railway.app/assets/" + newImageId).centerCrop().into(profile_img);

        String loginUsername = AppPreferences.getInstance(this).getUsername();
        if (loginUsername != null) {
            username.setText(loginUsername);
        }
    }

    private void openProfileActivity() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    private void openGameDetailedActivity(String gameId) {
        Intent intent = new Intent(this, GameDetailedActivity.class);
        intent.putExtra("gameId", gameId);
        startActivity(intent);
    }

    private void openSearchActivity() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    private void initializeViews() {
        search_btn = findViewById(R.id.searchButton);
        profile_btn = findViewById(R.id.profilButton);
        home = findViewById(R.id.homeButton);
        search = findViewById(R.id.searchGame);
        username = findViewById(R.id.profilUserName);
        gamesLinear1 = findViewById(R.id.gamesLinear1);
        gamesLayout = findViewById(R.id.gamesLayout);
        profile_img = findViewById(R.id.profilImageButton);
    }

    private void setClickListeners() {
        search_btn.setOnClickListener(v -> openSearchActivity());
        search.setOnClickListener(v -> openSearchActivity());
        profile_btn.setOnClickListener(v -> openProfileActivity());
        home.setOnClickListener(v -> recreate());
    }

    private void loadGames() {
        String apiUrlPath = "https://directus-se.up.railway.app/items/games/";
        DirectusRequests.GetRequestTask getRequestTask = new DirectusRequests.GetRequestTask(executor, result -> {
            int count = 0;
            LinearLayout currentLinearLayout = gamesLinear1;

            for (JsonNode gameNode : result.get("data")) {
                int gameId = gameNode.get("id").asInt();
                String gameImageUrl = gameNode.get("hero_image").asText().replaceAll("\"", "");
                gameImageUrl = "https://directus-se.up.railway.app/assets/" + gameImageUrl;
                int marginRight = 0;
                if (count == 0) {
                    marginRight = 16;
                } else {
                    marginRight = 0;
                }
                ImageView gameImageView = createGameImageView(gameId, gameImageUrl, marginRight);

                LinearLayout finalCurrentLinearLayout = currentLinearLayout;
                runOnUiThread(() -> finalCurrentLinearLayout.addView(gameImageView));
                count++;

                if (count == 2) {
                    // Create a new LinearLayout for the next pair of games
                    currentLinearLayout = createNewLinearLayout();
                    LinearLayout finalCurrentLinearLayout1 = currentLinearLayout;
                    runOnUiThread(() -> gamesLayout.addView(finalCurrentLinearLayout1));
                    count = 0;
                }
            }
        });
        getRequestTask.executeInBackground(apiUrlPath);
    }

    private ImageView createGameImageView(int gameId, String imageUrl, int marginRight) {
        ImageView gameImageView = new ImageView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                (int) convertDpToPx(this,145), (int) convertDpToPx(this,145)
        );
        layoutParams.setMargins(
                0,
                0,
                (int) convertDpToPx(this,marginRight),
                (int) convertDpToPx(this,16)
        );
        gameImageView.setLayoutParams(layoutParams);
        runOnUiThread(() -> Glide.with(this).load(imageUrl).centerCrop().into(gameImageView));

        runOnUiThread(() -> gameImageView.setOnClickListener(v -> openGameDetailedActivity(String.valueOf(gameId))));

        return gameImageView;
    }

    private LinearLayout createNewLinearLayout() {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER);
        return linearLayout;
    }

    private float convertDpToPx(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
}
