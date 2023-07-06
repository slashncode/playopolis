package de.haw_hamburg.playopolis;

import static java.io.File.createTempFile;

import static de.haw_hamburg.playopolis.DirectusRequests.executor;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.haw_hamburg.playopolis.databinding.SetProfilePageBinding;
import de.haw_hamburg.playopolis.ui.createProfile.SetProfileGenresAdapter;

public class SetProfileActivity extends AppCompatActivity implements DirectusRequests.GetRequestTask.OnRequestCompleteListener {
    private SetProfilePageBinding binding;
    private Button chooseFile_btn;
    private Button continue_btn;
    private ImageView setProfile_back_btn;
    private ImageView profile_picture;
    private RecyclerView genreRecyclerView;
    private RecyclerView gamesRecyclerView;
    private FlexboxLayoutManager genreLayoutManager;
    private FlexboxLayoutManager gamesLayoutManager;
    private ActivityResultLauncher<String> filePickerLauncher;
    private String filePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.set_profile_page);

        filePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    profile_picture.setImageURI(uri);
                    filePath = getFilePathFromUri(uri);
                }
            });

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
        profile_picture = findViewById(R.id.profilpicture_imageView);
        genreRecyclerView = findViewById(R.id.genre_tags_recyclerview);
        gamesRecyclerView = findViewById(R.id.game_tags_recyclerview);
        genreLayoutManager = new FlexboxLayoutManager(getApplicationContext());
        gamesLayoutManager = new FlexboxLayoutManager(getApplicationContext());
    }

    private void setClickListeners(){
        continue_btn.setOnClickListener(v -> openRecommendationsView());
        setProfile_back_btn.setOnClickListener(v -> openRegisterView());
        chooseFile_btn.setOnClickListener(v -> {
            filePickerLauncher.launch("image/*");
        });
    }

    private void openRecommendationsView(){
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
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
            getRequestTask.executeInBackground(apiUrlPath);
        });

        Intent intent = new Intent(this, RecommendationActivity.class);
        startActivity(intent);
    }
    private void openRegisterView(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRequestComplete(JsonNode result) {
        AppPreferences.getInstance(SetProfileActivity.this).setUserId(result);
        System.out.println(result);
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
