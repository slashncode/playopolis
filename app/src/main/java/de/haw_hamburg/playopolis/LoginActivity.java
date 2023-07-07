package de.haw_hamburg.playopolis;

import static de.haw_hamburg.playopolis.DirectusRequests.executor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.databind.JsonNode;

public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button login_btn;
    private ImageView backToStartImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        initializeViews();

        backToStartImageView.setOnClickListener(v -> openMainActivity());

        login_btn.setOnClickListener(v -> {
                String inputMail = username.getText().toString();
                String inputPassword = password.getText().toString();

                if (inputMail.isEmpty() || inputPassword.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please fill in all the fields!", Toast.LENGTH_SHORT).show();
                } else {
                    String apiUrlPath = "https://directus-se.up.railway.app/items/users?filter[username][_eq]=" + inputMail;
                    DirectusRequests.GetRequestTask getRequestTask = new DirectusRequests.GetRequestTask(executor, new DirectusRequests.GetRequestTask.OnRequestCompleteListener() {
                        @Override
                        public void onRequestComplete(JsonNode result) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (result.get("data").size() > 0) {
                                        String directusUsername = String.valueOf(result.get("data").get(0).get("username"));
                                        directusUsername = directusUsername.substring(1, directusUsername.length() - 1);
                                        String directusPassword = String.valueOf(result.get("data").get(0).get("password"));
                                        directusPassword = directusPassword.substring(1, directusPassword.length() - 1);
                                        if (inputMail.equals(directusUsername) && inputPassword.equals(directusPassword)) {
                                            String imageId = String.valueOf(result.get("data").get(0).get("avatar"));
                                            imageId.substring(1, imageId.length() - 1);
                                            AppPreferences.getInstance(LoginActivity.this).setUserId(result);
                                            AppPreferences.getInstance(LoginActivity.this).setUsername(directusUsername);
                                            AppPreferences.getInstance(LoginActivity.this).setImageId(imageId);
                                            Intent intent = new Intent(LoginActivity.this, RecommendationActivity.class);
                                            startActivity(intent);
                                        } else {
                                            System.out.println(directusPassword + " " + inputPassword);
                                            System.out.println(directusUsername + " " + inputMail);
                                            Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(LoginActivity.this, "No data available", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                    getRequestTask.executeInBackground(apiUrlPath);
                }
        });
    }


    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    private void initializeViews(){
        username = findViewById(R.id.edit_username2);
        password = findViewById(R.id.edit_enterpassword);
        login_btn = findViewById(R.id.login_btn);
        backToStartImageView = findViewById(R.id.register_back_btn);
    }
}
