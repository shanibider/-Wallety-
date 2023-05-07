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
        Intent mainScreenIntent = new Intent(this, MainActivity.class);
        Handler handler = new Handler();
        handler.postDelayed(() -> Model.instance().fetchLoggedUser(
                onSuccess -> {
                    startActivity(mainScreenIntent);
                    finish();
                },
                ocFailure -> {
                    startActivity(registrationIntent);
                    finish();
                }), 2000);
    }
}