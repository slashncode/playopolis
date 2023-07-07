package de.haw_hamburg.playopolis;

import static de.haw_hamburg.playopolis.DirectusRequests.executor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText email;
    private EditText username;
    private EditText password;
    private EditText password_repeat;
    private ImageView backToStartImageView;
    private Button register_btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);

        initializeViews();

        ImageView backToStartImageView = findViewById(R.id.register_back_btn);
        backToStartImageView.setOnClickListener(v -> openMainActivity());

        Button register_btn =findViewById(R.id.register_btn);
        register_btn.setOnClickListener(v -> {
            String inputMail = email.getText().toString();
            String inputUsername = username.getText().toString();
            String inputPassword = password.getText().toString();
            String inputRepPassword = password_repeat.getText().toString();

            if (inputMail.isEmpty() || inputUsername.isEmpty() || inputPassword.isEmpty() || inputRepPassword.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Please fill in all the fields!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!inputPassword.equals(inputRepPassword)) {
                Toast.makeText(RegisterActivity.this, "Passwords are not the same. Try again.", Toast.LENGTH_SHORT).show();
            } else {
                AppPreferences.getInstance(RegisterActivity.this).setUsername(inputUsername);

                DirectusRequests.registerUser(inputMail, inputUsername, inputPassword, r -> {
                    String loginUsername = AppPreferences.getInstance(this).getUsername();
                    String apiUrlPath = "https://directus-se.up.railway.app/items/users?filter[username][_eq]=" + loginUsername;
                    DirectusRequests.GetRequestTask getRequestTask = new DirectusRequests.GetRequestTask(executor, result -> AppPreferences.getInstance(this).setUserId(result));
                    getRequestTask.executeInBackground(apiUrlPath);
                });
                openSetProfileActivity();
            }
        });


    }

    private void initializeViews() {
        email = findViewById(R.id.edit_email);
        username = findViewById(R.id.edit_username);
        password = findViewById(R.id.edit_enterpassword);
        password_repeat = findViewById(R.id.editTextRepeatPassword);
    }

    private void openSetProfileActivity() {
        Intent intent = new Intent(this, SetProfileActivity.class);
        startActivity(intent);
    }

    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
