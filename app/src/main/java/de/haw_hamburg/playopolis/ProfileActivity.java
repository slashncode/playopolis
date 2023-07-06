package de.haw_hamburg.playopolis;

import static java.io.File.createTempFile;
import static de.haw_hamburg.playopolis.DirectusRequests.executor;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import de.haw_hamburg.playopolis.databinding.ProfilePageBinding;
import de.haw_hamburg.playopolis.ui.createProfile.SetProfileGenresAdapter;

public class ProfileActivity extends AppCompatActivity {

    private ImageView home_btn;
    private ImageView search_btn;
    private ImageView profile_picture;
    private ProfilePageBinding binding;
    private Button chooseFile_btn;
    private Button logout_btn;
    private Button save_btn;
    private TextView username;

    private RecyclerView genreRecyclerView;
    private RecyclerView gamesRecyclerView;
    private FlexboxLayoutManager genreLayoutManager;
    private FlexboxLayoutManager gamesLayoutManager;
    private ActivityResultLauncher<String> filePickerLauncher;
    private String filePath;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.profile_page);

        filePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        profile_picture.setImageURI(uri);
                        filePath = getFilePathFromUri(uri);
                    }
                });

        initializeViews();
        setClickListeners();

        String loginUsername = AppPreferences.getInstance(this).getUsername();
        if (loginUsername != null) {
            username.setText(loginUsername);
        }
        String imageId = AppPreferences.getInstance(this).getImageId();
        String newImageId = imageId.substring(1, imageId.length() - 1);
        Glide.with(this).load("https://directus-se.up.railway.app/assets/" + newImageId).centerCrop().into(profile_picture);

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
        save_btn = findViewById(R.id.profile_save_btn);
        profile_picture = findViewById(R.id.profilpicture2_imageView);
        username = findViewById(R.id.profile_username);
        genreRecyclerView = binding.profileGenreTagsRecyclerview;
        gamesRecyclerView = binding.profileGameTagsRecyclerview;
        genreLayoutManager = new FlexboxLayoutManager(getApplicationContext());
        gamesLayoutManager = new FlexboxLayoutManager(getApplicationContext());
    }
    private void setClickListeners(){
        home_btn.setOnClickListener(v -> openRecommendationActivity());
        search_btn.setOnClickListener(v -> openSearchActivity());
        save_btn.setOnClickListener(v -> openRecommendationActivityAndSave());
        logout_btn.setOnClickListener(v -> openMainActivity());
        //TODO: set onClick Listener to choose pfp file
        chooseFile_btn.setOnClickListener(v -> {
            filePickerLauncher.launch("image/*");
        });
    }

    private void openRecommendationActivityAndSave() {
        // Set genres
        SetProfileGenresAdapter genresAdapter = binding.getGenresAdapter();
        List<String> enabledTags = genresAdapter.getEnabledTags();
        String genreJsonAsString = "{ \"genres\" : " + enabledTags + "}";
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode genreJson = objectMapper.readTree(genreJsonAsString);
            DirectusRequests.executePatchRequest(AppPreferences.getInstance(this).getUserId(), genreJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // Set image
        DirectusRequests.createImage(filePath, this, imgName -> {
            String fileName = AppPreferences.getInstance(this).getImageName();
            String apiUrlPath = "https://directus-se.up.railway.app/files?filter[filename_download][_icontains]=" + fileName;
            DirectusRequests.GetRequestTask getRequestTask = new DirectusRequests.GetRequestTask(executor, result -> {
                String imageId = String.valueOf(result.get("data").get(0).get("id"));
                String imageJsonAsString = "{ \"avatar\" : " + imageId + "}";
                AppPreferences.getInstance(this).setImageId(imageId);
                ObjectMapper imageObjectMapper = new ObjectMapper();
                try {
                    JsonNode imageJson = imageObjectMapper.readTree(imageJsonAsString);
                    DirectusRequests.executePatchRequest(AppPreferences.getInstance(this).getUserId(), imageJson);
                    Intent intent = new Intent(this, RecommendationActivity.class);
                    startActivity(intent);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
            getRequestTask.executeInBackground(apiUrlPath);
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

    private void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private String getFilePathFromUri(Uri uri) {
        String filePath = null;
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            filePath = cursor.getString(columnIndex);
            cursor.close();
        }

        if (filePath == null) {
            try {
                File tempFile = createTempFileFromUri(uri);
                if (tempFile != null) {
                    filePath = tempFile.getAbsolutePath();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return filePath;
    }

    private File createTempFileFromUri(Uri uri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        if (inputStream != null) {
            File tempFile = createTempFile("temp_", ".jpg");
            FileOutputStream outputStream = new FileOutputStream(tempFile);
            byte[] buffer = new byte[4 * 1024]; // 4 KB buffer
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            return tempFile;
        }
        return null;
    }
}
