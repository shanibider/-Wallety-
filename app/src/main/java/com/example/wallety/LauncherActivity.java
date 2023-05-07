package com.example.wallety;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.wallety.model.Model;
import com.example.wallety.model.User;
import com.example.wallety.model.server.UserFetcherCon;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        Objects.requireNonNull(getSupportActionBar()).hide();

        Intent registrationIntent = new Intent(this, RegistrationActivity.class);
        Intent mainScreenIntent = new Intent(this, MainActivity.class);
        Handler handler = new Handler();


        handler.postDelayed(() ->
                UserFetcherCon.getLoggedInUser(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            User loggedInUser = response.body();
                            Model.instance().setCurrentUser(loggedInUser);
                            startActivity(mainScreenIntent);
                            finish();
                        } else {
                            startActivity(registrationIntent);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        startActivity(registrationIntent);
                        finish();
                    }
                })


//                Model.instance().fetchLoggedUser(
//                onSuccess -> {
//                    startActivity(mainScreenIntent);
//                    finish();
//                },
//                ocFailure -> {
//                    startActivity(registrationIntent);
//                    finish();
//                })
                , 2000);
    }
}