package de.haw_hamburg.playopolis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        ImageView backToStartImageView = (ImageView) findViewById(R.id.register_back_btn);
        Button login_btn = (Button) findViewById(R.id.login_btn);
        email = (EditText) findViewById(R.id.edit_entermail);
        password = (EditText) findViewById(R.id.edit_enterpassword);

        backToStartImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputMail = email.getText().toString();
                String inputPassword = password.getText().toString();

                if (inputMail.isEmpty() || inputPassword.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please fill in all the fields!", Toast.LENGTH_SHORT).show();
                }
                openRecommendationActivity();
            }
        });
    }

    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void openRecommendationActivity() {
        Intent intent = new Intent(this, RecommendationActivity.class);
        startActivity(intent);
    }


}
