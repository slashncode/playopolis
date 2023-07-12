package de.haw_hamburg.playopolis;

import static de.haw_hamburg.playopolis.DirectusRequests.executor;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private ImageView home_btn;
    private ImageView profile_btn;
    private SearchView searchView;
    private LinearLayout searchLinearLayout;
    private TextView username;

    private Handler searchHandler = new Handler();
    private Runnable searchRunnable;
    private ImageView profile_picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initializeViews();
        setClickListeners();
        setupSearchView();

        String loginUsername = AppPreferences.getInstance(this).getUsername();
        if (loginUsername != null) {
            username.setText(loginUsername);
        }

        String imageId = AppPreferences.getInstance(this).getImageId();
        String newImageId = imageId.substring(1, imageId.length() - 1);
        Glide.with(this).load("https://directus-se.up.railway.app/assets/" + newImageId).centerCrop().into(profile_picture);
    }

    private void initializeViews() {
        home_btn = findViewById(R.id.homeButton);
        profile_btn = findViewById(R.id.profilButton);
        searchView = findViewById(R.id.searchView);
        searchLinearLayout = findViewById(R.id.search_linearlayout);
        username = findViewById(R.id.profilUserName);
        profile_picture = findViewById(R.id.profilImageButton);
    }

    private void setClickListeners() {
        home_btn.setOnClickListener(v -> openRecommendationActivity());
        profile_btn.setOnClickListener(v -> openProfileActivity());
    }

    private void openRecommendationActivity() {
        Intent intent = new Intent(this, RecommendationActivity.class);
        startActivity(intent);
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

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchHandler.removeCallbacks(searchRunnable);

                searchRunnable = new Runnable() {
                    @Override
                    public void run() {
                        performSearch(newText);
                    }
                };

                // Delayed search validation after 300ms
                searchHandler.postDelayed(searchRunnable, 300);
                return true;
            }
        });
    }

    private void performSearch(String query) {
        String apiUrlPath = "https://directus-se.up.railway.app/items/games?filter[title][_icontains]=" + query;
        DirectusRequests.GetRequestTask getRequestTask = new DirectusRequests.GetRequestTask(executor, result -> {
            List<String> titles = new ArrayList<>();
            List<String> ids = new ArrayList<>();
            JsonNode dataNode = result.get("data");
            if (dataNode.isArray()) {
                for (JsonNode itemNode : dataNode) {
                    JsonNode titleNode = itemNode.get("title");
                    JsonNode idNode = itemNode.get("id");
                    if (titleNode != null && idNode != null) {
                        String title = titleNode.asText();
                        String id = idNode.asText();
                        titles.add(title);
                        ids.add(id);
                    }
                }
            }
            runOnUiThread(() -> createSearchResultButtons(titles, ids));
        });
        getRequestTask.executeInBackground(apiUrlPath);
    }

    private void createSearchResultButtons(List<String> titles, List<String> ids) {
        // Clear existing buttons
        searchLinearLayout.removeAllViews();

        // Create buttons dynamically
        for (int i = 0; i < titles.size(); i++) {
            String title = titles.get(i);
            String gameId = ids.get(i);

            Button button = new Button(this);
            button.setText(title);
            button.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
            button.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(0, 0, 0, 16);
            button.setLayoutParams(layoutParams);

            button.setOnClickListener(v -> {
                openGameDetailedActivity(gameId);
            });

            searchLinearLayout.addView(button);
        }
    }
}