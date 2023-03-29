package com.example.wallety;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import java.util.Objects;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        Objects.requireNonNull(getSupportActionBar()).hide();
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent = new Intent(this, RegistrationActivity.class);
            startActivity(intent);
            finish();
        }, 2000);
    }
}