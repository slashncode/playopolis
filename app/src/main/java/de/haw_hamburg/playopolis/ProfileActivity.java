package de.haw_hamburg.playopolis;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import java.util.Arrays;
import java.util.List;

import de.haw_hamburg.playopolis.databinding.ProfilePageBinding;
import de.haw_hamburg.playopolis.ui.createProfile.SetProfileGenresAdapter;

public class ProfileActivity extends AppCompatActivity {

    private ImageView home_btn;
    private ImageView search_btn;
    private ProfilePageBinding binding;
    private Button chooseFile_btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.profile_page);

        initializeViews();
        setClickListeners();

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
        home_btn = (ImageView) findViewById(R.id.homeButton);
        search_btn = (ImageView) findViewById(R.id.searchButton);
        chooseFile_btn = (Button) findViewById(R.id.profile_choosefile_btn);
    }
    private void setClickListeners(){
        home_btn.setOnClickListener(v -> openRecommendationActivity());
        search_btn.setOnClickListener(v -> openSearchActivity());
        chooseFile_btn.setOnClickListener(v -> {

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
}
