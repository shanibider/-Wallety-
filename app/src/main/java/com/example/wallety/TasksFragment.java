package com.example.wallety;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.wallety.adapters.TaskAdapter;
import com.example.wallety.databinding.FragmentTasksBinding;
import com.example.wallety.model.Model;
import com.example.wallety.model.Task;
import com.example.wallety.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TasksFragment extends Fragment {
    FragmentTasksBinding binding;

    RecyclerView recyclerView;
    public static List<Task> taskList;
    public static TaskAdapter taskAdapter;
    AppCompatButton addNewTaskBtn;
    public static TextView taskCount_tv;

    private static DocumentReference db;
    static User user;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTasksBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        // hide actionBar
        ActionBar actionBar = null;
        try {
            actionBar = Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar());
        } catch (NullPointerException e) {}

        if (actionBar != null) {
            actionBar.hide();
        }

        user = Model.instance().getCurrentUser();
        db = FirebaseFirestore.getInstance().collection("users").document(user.getId());

        //Adding new task
        binding.addTaskBtn.setOnClickListener(view1 -> {

            // navigation with BottomSheetDialogFragment
            Window window = getActivity().getWindow();
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // finally change the color
            window.setStatusBarColor(ContextCompat.getColor(getContext(), R.color.teal_100));

            // Show the AddNewTask BottomSheetDialogFragment
            CreateTaskFragment createTaskFragment = new CreateTaskFragment();
            createTaskFragment.show(getChildFragmentManager(), CreateTaskFragment.TAG);

            // navigation without BottomSheetDialogFragment
            //Navigation.findNavController(view1).navigate(R.id.action_tasksFragment_to_createTaskFragment);
        });

        // RecyclerView initialization
        recyclerView = view.findViewById(R.id.task_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        taskList = new ArrayList<>();

        taskList = addTasks();

        taskCount_tv = view.findViewById(R.id.taskCount_tv);
        taskCount_tv.setText(String.valueOf(taskList.size()));

        taskAdapter = new TaskAdapter(getContext(), taskList);
        recyclerView.setAdapter(taskAdapter);
        taskAdapter.notifyDataSetChanged();

        return view;
    }


    // Add the tasks to the List, to be shown in RecyclerView
    // By fetching the tasks from DataBase
    public static List<Task> addTasks() {
        if(taskList!=null)
            taskList.clear();
        db.collection("tasks")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // task.getResult() returns a list of documents
                            // belonging to the collection = "tasks"
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String id = (String) document .get("id");
                                String name = (String) document .get("name");
                                String desc = (String) document .get("desc");
                                String date = (String) document .get("date");
                                String time = (String) document .get("time");
                                String amount = (String) document .get("amount");
                                String targetChild = (String) document .get("targetChild");


                                Task t = new Task(id, name, desc, date, time, amount, targetChild);
                                taskList.add(t);
                                taskAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
        return taskList;
    }
}