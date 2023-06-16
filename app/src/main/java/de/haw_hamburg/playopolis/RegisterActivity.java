package de.haw_hamburg.playopolis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity{

    private EditText e_mail;
    private EditText username;
    private EditText password;
    private EditText password_repeat;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);

        ImageView backToStartImageView = (ImageView) findViewById(R.id.register_back_btn);
        backToStartImageView.setOnClickListener(v -> openMainActivity());

        Button register_btn = (Button) findViewById(R.id.login_btn);
        register_btn.setOnClickListener(v -> openSetProfileActivity());
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
