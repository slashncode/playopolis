package de.haw_hamburg.playopolis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity{

    private ImageView backToStartImageView;
    private Button register_btn;
    private EditText e_mail;
    private EditText username;
    private EditText password;
    private EditText password_repeat;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);

        backToStartImageView = (ImageView) findViewById(R.id.back_btn);
        backToStartImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });

        register_btn = (Button) findViewById(R.id.register_btn);
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSetProfileActivity();
            }
        });
    }

    private void openSetProfileActivity() {
            Intent intent = new Intent(this, SetProfileActivity.class);
            startActivity(intent);
    }

    private void openMainActivity() {
            Intent intent = new Intent(this, RecommendationActivity.MainActivity.class);
            startActivity(intent);
    }
}
