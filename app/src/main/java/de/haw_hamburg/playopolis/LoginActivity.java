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
                    AppPreferences.getInstance(LoginActivity.this).setUsername(inputMail);
                    String apiUrlPath = "https://directus-se.up.railway.app/items/users?filter[username][_eq]=" + inputMail;
                    DirectusRequests.GetRequestTask getRequestTask = new DirectusRequests.GetRequestTask(executor, new DirectusRequests.GetRequestTask.OnRequestCompleteListener() {
                        @Override
                        public void onRequestComplete(JsonNode result) {
                            AppPreferences.getInstance(LoginActivity.this).setUserId(result);
                        }
                    });
                    getRequestTask.executeInBackground(apiUrlPath);


                    Intent intent = new Intent(LoginActivity.this, RecommendationActivity.class);
                    startActivity(intent);
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
