package de.haw_hamburg.playopolis;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_page);

        /*binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);*/

        //private ActivityMainBinding binding;
        Button button = (Button) findViewById(R.id.login_button);
        button.setOnClickListener(v -> openLoginView());

        button = (Button) findViewById(R.id.register_button);
        button.setOnClickListener(v -> openRegisterView());

    }

    public void openLoginView(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void openRegisterView(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}