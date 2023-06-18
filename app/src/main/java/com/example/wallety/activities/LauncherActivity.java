package com.example.wallety.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.wallety.R;
import com.example.wallety.model.Model;
import java.util.Objects;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        Objects.requireNonNull(getSupportActionBar()).hide();

        Intent registrationIntent = new Intent(this, RegistrationActivity.class);

        new Handler().postDelayed(() -> {
            startActivity(registrationIntent);
            finish();
        }, 2000);
    }
}