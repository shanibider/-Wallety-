package com.example.wallety;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallety.adapters.SavingAdapter;
import com.example.wallety.databinding.FragmentSavingBinding;
import com.example.wallety.model.Model;
import com.example.wallety.model.Saving;
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

public class SavingFragment extends Fragment {
    FragmentSavingBinding binding;

    RecyclerView recyclerView;
    public static List<Saving> savingList;
    public static SavingAdapter savingAdapter;
    public static TextView goalCount_tv;

    private static DocumentReference db;
    static User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSavingBinding.inflate(inflater, container, false);
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

        // adding new goal
        binding.addGoalBtn.setOnClickListener(view1 -> {
            Navigation.findNavController(view1).navigate(R.id.action_savingMoneyFragment_to_addGoalFragment);
        });

        // RecyclerView initialization
        recyclerView = view.findViewById(R.id.saving_recList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        savingList = new ArrayList<>();

        // add new goal
        if(savingList!=null)
            savingList.clear();
        db.collection("saving")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // saving.getResult() returns a list of documents
                            // belonging to the collection = "saving"
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String id = (String) document .get("id");
                                String goal = (String) document .get("goal");
                                String detail = (String) document .get("detail");
                                String amount = (String) document .get("amount");
                                String currentAmount = (String) document .get("currentAmount");


                                Saving s = new Saving(id, goal, detail, amount, currentAmount);
                                savingList.add(s);
                                savingAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });


       //savingList.add(new Saving ("1" ,"Scooter", "electric scotter", "₪1200", "50"));

        goalCount_tv = view.findViewById(R.id.goalCount_tv);
        goalCount_tv.setText(String.valueOf(savingList.size()));

        savingAdapter= new SavingAdapter(getContext(), savingList);
        recyclerView.setAdapter(savingAdapter);
        savingAdapter.notifyDataSetChanged();

         return view;
     }
 }