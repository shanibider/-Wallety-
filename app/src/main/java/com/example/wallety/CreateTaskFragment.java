package com.example.wallety;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.wallety.databinding.FragmentCreateTaskBinding;
import com.example.wallety.model.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class CreateTaskFragment extends Fragment {
    FragmentCreateTaskBinding binding;

    Button btn;
    EditText et1, et2, et3, et4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreateTaskBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        btn = view.findViewById(R.id.btn1_add_task);
        et1 = view.findViewById(R.id.task_et1);
        et2 = view.findViewById(R.id.task_et2);
        et3 = view.findViewById(R.id.task_et3);
        et4 = view.findViewById(R.id.task_et4);

        //Save data button
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
                Navigation.findNavController(view).navigate(R.id.action_createTaskFragment_to_tasksFragment);
            }
        });
        return view;
    }


    //Save Tasks to firebase
    public void saveData() {
        String task = et1.getText().toString();
        String task_detail = et2.getText().toString();
//      String task_amount = et3.getText().toString();
//      String task_frequency = et3.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String uid = FirebaseAuth.getInstance().getUid();

//        DocumentReference userRef = db.collection("users").document(uid);
//        String curUser = userRef.getId();

        Task tasks = new Task(task, task_detail);

        // Save Goal data to Firebase Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("tasks");

        //Set values to database
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean flag = false;
                if (snapshot.exists()) {
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        if (childSnapshot.getKey().equals(tasks.getTaskTitle())) {
                            flag = true;
                            break;
                        }
                    }
                }
                if (!flag) {
                    if (tasks.equals(""))
                        Toast.makeText(getContext(), "Please set a task", Toast.LENGTH_SHORT).show();
                    else if (task_detail.equals(""))
                        Toast.makeText(getContext(), "Please set task's descrption", Toast.LENGTH_SHORT).show();
//                    else if (task_amount.equals(""))
//                        Toast.makeText(getContext(), "Please set goal's total amount", Toast.LENGTH_SHORT).show();
                    else {
                        //If goal doesn't exists, add it to database
                        DatabaseReference childRef = myRef.child(tasks.getTaskTitle());
                        childRef.setValue(tasks);
                        Toast.makeText(getContext(), "Your task uploaded successfully", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "task name is already taken", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println(error);
            }
        });
    }
}