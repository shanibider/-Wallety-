package com.example.wallety;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallety.adapters.TaskAdapter;
import com.example.wallety.databinding.FragmentTasksBinding;
import com.example.wallety.model.Task;
import com.example.wallety.TODOROOM.TaskViewModel;

import java.util.ArrayList;
import java.util.List;

//The "Main" in youtube
public class TasksFragment extends Fragment {
    FragmentTasksBinding binding;

    RecyclerView recyclerView;
    List<Task> taskList;
    TaskAdapter taskAdapter;
    private TaskViewModel taskViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTasksBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        //Adding new task
        binding.addTask.setOnClickListener(view1 -> {
            Navigation.findNavController(view1).navigate(R.id.action_tasksFragment_to_createTaskFragment);
        });

//        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
//
//        //navigate from TasksFragment to CreateTaskFragment(dataInsertActivity) and retrieve the result data in TasksFragment
//        binding.addTask.setOnClickListener(view1 -> {
//            Navigation.findNavController(view1).navigate(R.id.action_tasksFragment_to_dataInsertActivity);
//        });

        //Result to retrieve from DataInsertActivity (/CreateTaskFragment)
        final ActivityResultLauncher<Intent> getDataForResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        String title = data.getStringExtra("title");
                        String description = data.getStringExtra("description");
                        Task task = new Task(title, description);
                        taskViewModel.insert(task);
                        Toast.makeText(getContext(), "Task added", Toast.LENGTH_SHORT).show();
                    }
                }
        );




        recyclerView = view.findViewById(R.id.taskRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        taskList = new ArrayList<>();

        taskList.add(new Task("Clean the kitchen", "Sweep and wipe"));
        taskList.add(new Task("Fold laundry", "Fold and put in the closet"));

        taskAdapter = new TaskAdapter(getContext(), taskList);
        recyclerView.setAdapter(taskAdapter);
        taskAdapter.notifyDataSetChanged();



        return view;
    }



}