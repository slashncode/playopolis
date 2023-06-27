package de.haw_hamburg.playopolis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.haw_hamburg.playopolis.databinding.SetProfilePageBinding;
import de.haw_hamburg.playopolis.ui.createProfile.SetProfileGenresAdapter;

public class SetProfileActivity extends AppCompatActivity {
    private SetProfilePageBinding binding;
    private Button chooseFile_btn;
    private Button continue_btn;
    private ImageView setProfile_back_btn;
    private RecyclerView genreRecyclerView;
    private RecyclerView gamesRecyclerView;
    private FlexboxLayoutManager genreLayoutManager;
    private FlexboxLayoutManager gamesLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.set_profile_page);

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
        continue_btn = findViewById(R.id.setProfile_continue_btn);
        chooseFile_btn = findViewById(R.id.setProfile_choosefile_btn);
        setProfile_back_btn = findViewById(R.id.setProfile_back_btn);
        genreRecyclerView = findViewById(R.id.genre_tags_recyclerview);
        gamesRecyclerView = findViewById(R.id.game_tags_recyclerview);
        genreLayoutManager = new FlexboxLayoutManager(getApplicationContext());
        gamesLayoutManager = new FlexboxLayoutManager(getApplicationContext());
    }

    private void setClickListeners(){
        continue_btn.setOnClickListener(v -> openRecommendationsView());
        setProfile_back_btn.setOnClickListener(v -> openRegisterView());
        chooseFile_btn.setOnClickListener(v -> {

        });
    }

    private void openRecommendationsView(){
        Intent intent = new Intent(this, GameDetailedActivity.class);
        startActivity(intent);
    }
    private void openRegisterView(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
