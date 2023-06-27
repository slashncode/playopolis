package de.haw_hamburg.playopolis;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.Arrays;
import java.util.List;

import de.haw_hamburg.playopolis.databinding.ProfilePageBinding;
import de.haw_hamburg.playopolis.ui.createProfile.SetProfileGenresAdapter;

public class ProfileActivity extends AppCompatActivity {

    private ImageView home_btn;
    private ImageView search_btn;
    private ProfilePageBinding binding;
    private Button chooseFile_btn;
    private Button logout_btn;
    private RecyclerView genreRecyclerView;
    private RecyclerView gamesRecyclerView;
    private FlexboxLayoutManager genreLayoutManager;
    private FlexboxLayoutManager gamesLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.profile_page);

        initializeViews();
        setClickListeners();

        genreLayoutManager.setFlexDirection(FlexDirection.ROW);
        genreRecyclerView.setLayoutManager(genreLayoutManager);
        gamesLayoutManager.setFlexDirection(FlexDirection.ROW);
        gamesRecyclerView.setLayoutManager(gamesLayoutManager);

        populateGenres();
        populateGames();

        setContentView(binding.getRoot());
    }

    private void populateGenres() {
        List<String> dataModelList = Arrays.asList(getResources().getStringArray(R.array.genres));

        int color = getResources().getColor(R.color.genre_tag);

        SetProfileGenresAdapter genresAdapter = new SetProfileGenresAdapter(color, dataModelList);
        binding.setGenresAdapter(genresAdapter);
    }

    private void populateGames() {
        List<String> dataModelList = Arrays.asList(getResources().getStringArray(R.array.games));

        int color = getResources().getColor(R.color.game_tag);

        SetProfileGenresAdapter gamesAdapter = new SetProfileGenresAdapter(color, dataModelList);
        binding.setGamesAdapter(gamesAdapter);
    }
    private void initializeViews(){
        home_btn = findViewById(R.id.homeButton);
        search_btn = findViewById(R.id.searchButton);
        chooseFile_btn = findViewById(R.id.profile_choosefile_btn);
        logout_btn = findViewById(R.id.logout_btn);
        genreRecyclerView = binding.profileGenreTagsRecyclerview;
        gamesRecyclerView = binding.profileGameTagsRecyclerview;
        genreLayoutManager = new FlexboxLayoutManager(getApplicationContext());
        gamesLayoutManager = new FlexboxLayoutManager(getApplicationContext());
    }
    private void setClickListeners(){
        home_btn.setOnClickListener(v -> openRecommendationActivity());
        search_btn.setOnClickListener(v -> openSearchActivity());
        //TODO: set onClick Listener to choose pfp file
        /*chooseFile_btn.setOnClickListener(v -> {

        });*/
        //TODO: set onClick Listener to actually logout
        /*logout_btn.setOnClickListener(v -> {
            openMainActivity();
        });*/
    }

    private void openRecommendationActivity(){
        Intent intent = new Intent(this, RecommendationActivity.class);
        startActivity(intent);
    }
    private void openSearchActivity(){
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    private void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
