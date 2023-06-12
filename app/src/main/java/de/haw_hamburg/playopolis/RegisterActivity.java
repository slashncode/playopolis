package de.haw_hamburg.playopolis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity{

    ImageView backToStartImageView;
    Button register_btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);

        backToStartImageView = findViewById(R.id.back_btn);
        backToStartImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });

        register_btn = findViewById(R.id.register_btn);
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
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
    }
}
