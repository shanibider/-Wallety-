package com.example.wallety;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallety.adapters.HomeAdapter;
import com.example.wallety.adapters.SavingAdapter;
import com.example.wallety.databinding.FragmentSavingMoneyBinding;
import com.example.wallety.model.Saving;
import com.example.wallety.model.Transactions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;

public class SavingMoneyFragment extends Fragment {
    FragmentSavingMoneyBinding binding;

    RecyclerView recyclerView;
    List<Saving> savingList;
    SavingAdapter savingAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSavingMoneyBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        recyclerView = view.findViewById(R.id.saving_recList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        savingList = new ArrayList<>();

        savingList.add(new Saving ("Shani","Scooter", "electric scotter", "₪1200", "50"));
        savingList.add(new Saving("Shani", "Gym clothes", "nike set", "₪400", "100"));
        //savingList.add(new Saving("Trip to Paris", "flight+hotel+pocket money", "₪10,000", 7000));

        savingAdapter= new SavingAdapter(getContext(), savingList);
        recyclerView.setAdapter(savingAdapter);
        savingAdapter.notifyDataSetChanged();

                binding.btn1.setOnClickListener(view1 -> {
            Navigation.findNavController(view1).navigate(R.id.action_savingMoneyFragment_to_addGoalFragment);
        });

        return view;

    }
}







//        tv1 = view.findViewById(R.id.tv1);
//        et3 =  view.findViewById(R.id.et3);
//        tv2 = view.findViewById(R.id.tv2);
//        et2 =  view.findViewById(R.id.et2);
//
//        binding.goalBtn.setOnClickListener(view1 -> {
//            String goal;
//            goal= et3.getText().toString();
//            tv1.setText(goal);
//        });

//        binding.transferBtn.setOnClickListener(view1 -> {
//            String balance;
//            balance= et2.getText().toString();
//            tv2.setText(balance);
//
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    Navigation.findNavController(view1).popBackStack();
//                }
//            }, 4000);
//        });