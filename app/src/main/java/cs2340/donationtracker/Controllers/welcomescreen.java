package cs2340.donationtracker.Controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import cs2340.donationtracker.R;

public class welcomescreen extends AppCompatActivity {
    //preCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcomescreen);

    }

    public void goToLogin(View v) {
        Intent intent = new Intent(this, login.class);
        startActivity(intent);
    }

    public void goToRegistration(View v) {
        Intent intent = new Intent(this, registration.class);
        startActivity(intent);
    }
    // setCache
}