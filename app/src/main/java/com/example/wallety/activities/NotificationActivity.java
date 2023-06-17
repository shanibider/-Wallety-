package com.example.wallety.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.wallety.R;

public class NotificationActivity extends AppCompatActivity {
 TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        textView = findViewById(R.id.tv);
        String data  = getIntent().getStringExtra("data");
        textView.setText(data);

    }
}