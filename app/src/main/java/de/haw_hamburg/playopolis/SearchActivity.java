package de.haw_hamburg.playopolis;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class SearchActivity extends AppCompatActivity {

    private ImageView home_btn;
    private ImageView profile_btn;
    private SearchView searchView;
    private LinearLayout searchLinearLayout;
    private Button searchResult1;
    private Button searchResult2;
    private Button searchResult3;
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
        searchResult1 = findViewById(R.id.searchResult1);
        searchResult2 = findViewById(R.id.searchResult2);
        searchResult3 = findViewById(R.id.searchResult3);
        username = findViewById(R.id.profilUserName);
        profile_picture = findViewById(R.id.profilImageButton);
    }

    private void setClickListeners() {
        home_btn.setOnClickListener(v -> openRecommendationActivity());
        profile_btn.setOnClickListener(v -> openProfileActivity());
        searchResult1.setOnClickListener(v -> openGameDetailedActivity());
    }

    private void openRecommendationActivity() {
        Intent intent = new Intent(this, RecommendationActivity.class);
        startActivity(intent);
    }

    private void openProfileActivity() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    private void openGameDetailedActivity() {
        Intent intent = new Intent(this, GameDetailedActivity.class);
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
        if (query.contains("god")) {
            showSearchResults();
        } else {
            hideSearchResults();
        }
    }

    private void showSearchResults() {
        searchResult1.setVisibility(View.VISIBLE);
        searchResult2.setVisibility(View.VISIBLE);
        searchResult3.setVisibility(View.VISIBLE);
    }

    private void hideSearchResults() {
        searchResult1.setVisibility(View.GONE);
        searchResult2.setVisibility(View.GONE);
        searchResult3.setVisibility(View.GONE);
    }
}