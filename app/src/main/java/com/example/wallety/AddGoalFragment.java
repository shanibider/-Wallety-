package com.example.wallety;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.example.wallety.databinding.FragmentAddGoalBinding;
import com.example.wallety.databinding.FragmentSavingMoneyBinding;
import com.example.wallety.model.Saving;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddGoalFragment extends Fragment {
    FragmentAddGoalBinding binding;

    Button btn;
    EditText et1, et2, et3;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddGoalBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        btn = view.findViewById(R.id.btn1_add_goal);
        et1 = view.findViewById(R.id.et1);
        et2 = view.findViewById(R.id.et2);
        et3 = view.findViewById(R.id.et3);
        progressBar = view.findViewById(R.id.progressBar);

        //Save data button
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
                Navigation.findNavController(view).navigate(R.id.action_addGoalFragment_to_savingMoneyFragment);
            }
        });
        return view;
    }

    public void saveData() {
       String goal = et1.getText().toString();
       String detail = et2.getText().toString();
       String amount = et3.getText().toString();
       String progressBar = "50";

       FirebaseFirestore db = FirebaseFirestore.getInstance();
       String uid = FirebaseAuth.getInstance().getUid();
       DocumentReference userRef = db.collection("users").document(uid);
       String curUser = userRef.getId();

        Saving saving = new Saving(curUser, goal, detail, amount, progressBar);

        // Save Goal data to Firebase Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("goals");

        //Set values to database
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean flag = false;
                if (snapshot.exists()) {
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        if (childSnapshot.getKey().equals(saving.getGoal())) {
                            flag = true;
                            break;
                        }
                    }
                }
                if (!flag) {
                    if (goal.equals(""))
                        Toast.makeText(getContext(), "Please set a goal", Toast.LENGTH_SHORT).show();
                    else if (detail.equals(""))
                        Toast.makeText(getContext(), "Please set goal's deails", Toast.LENGTH_SHORT).show();
                    else if (amount.equals(""))
                        Toast.makeText(getContext(), "Please set goal's total amount", Toast.LENGTH_SHORT).show();
                    else {
                        //If goal doesn't exists, add it to database
                        DatabaseReference childRef = myRef.child(saving.getGoal());
                        childRef.setValue(saving);
                        Toast.makeText(getContext(), "Your goal uploaded successfully", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Goal name is already taken", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println(error);
            }
        });

    }
}