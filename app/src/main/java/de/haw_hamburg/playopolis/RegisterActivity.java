package de.haw_hamburg.playopolis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity{

    private EditText email;
    private EditText username;
    private EditText password;
    private EditText password_repeat;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);

        email = (EditText) findViewById(R.id.edit_entermail);
        username = (EditText) findViewById(R.id.editTextText);
        password = (EditText) findViewById(R.id.edit_enterpassword);
        password_repeat = (EditText) findViewById(R.id.editTextRepeatPassword);

        ImageView backToStartImageView = (ImageView) findViewById(R.id.register_back_btn);
        backToStartImageView.setOnClickListener(v -> openMainActivity());

        Button register_btn = (Button) findViewById(R.id.register_btn);
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputMail = email.getText().toString();
                String inputUsername = username.getText().toString();
                String inputPassword = password.getText().toString();
                String inputRepPassword = password_repeat.getText().toString();

                if (inputMail.isEmpty() || inputUsername.isEmpty() || inputPassword.isEmpty() || inputRepPassword.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please fill in all the fields!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!inputPassword.equals(inputRepPassword)) {
                    Toast.makeText(RegisterActivity.this, "Passwords are not the same. Try again.", Toast.LENGTH_SHORT).show();
                } else {
                    //DirectusRequests.registerUser(inputMail, inputUsername, inputPassword, inputRepPassword);
                    openSetProfileActivity();
                }
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
