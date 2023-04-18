package com.example.wallety.TODOROOM;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.wallety.R;
import com.example.wallety.TasksFragment;
import com.example.wallety.databinding.ActivityDataInsertBinding;

public class DataInsertActivity extends AppCompatActivity {

ActivityDataInsertBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDataInsertBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_data_insert);

//This code sets the result of the activity as OK and puts the data to be returned as extras in an intent
    binding.btnCreateTask.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent= new Intent();
            intent.putExtra("title", binding.taskEt1.getText().toString());
            intent.putExtra("description", binding.taskEt2.getText().toString());
            setResult(RESULT_OK, intent);
            //finish();

            //?????

            Intent intent1 = new Intent(getApplicationContext(), TasksFragment.class);
            startActivity(intent1);


        }
    });

    }






}