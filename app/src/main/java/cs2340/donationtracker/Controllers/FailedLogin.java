package cs2340.donationtracker.Controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import cs2340.donationtracker.R;

public class FailedLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_failed_login);
    }

    public void goBackToLogin(View v) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}
