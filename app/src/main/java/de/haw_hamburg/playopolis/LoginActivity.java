package de.haw_hamburg.playopolis;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button login_btn;
    private ImageView backToStartImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                    Intent intent = new Intent(LoginActivity.this, RecommendationActivity.class);
                    intent.putExtra("username", inputMail);
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
