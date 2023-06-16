package com.example.wallety;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wallety.adapters.HomeAdapter;
import com.example.wallety.databinding.FragmentChildrenHomeBinding;
import com.example.wallety.databinding.FragmentHomeBinding;
import com.example.wallety.model.Model;
import com.example.wallety.model.Transaction;
import com.example.wallety.model.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    FragmentChildrenHomeBinding bindingChildren;

    RecyclerView recyclerView;
    List<Transaction> transactionsList;
    HomeAdapter homeAdapter;
    View view;
    private View partialView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // get Current User
        User user = Model.instance().getCurrentUser();
        String nameHeader = "Hello " + Model.instance().getCurrentUser().getName();

        // Inflate the appropriate layout based on the user type
        if (!user.isParent()) {
            binding = FragmentHomeBinding.inflate(inflater, container, false);
            view = binding.getRoot();
            initializeParentViews();

            binding.nameHeaderTv.setText(nameHeader);
        } else {
            bindingChildren = FragmentChildrenHomeBinding.inflate(inflater, container, false);
            view = bindingChildren.getRoot();
            initializeChildViews();

            bindingChildren.nameHeaderTv.setText(nameHeader);
        }

        partialView = view.findViewById(R.id.partial);

//        if (user.getIsParent()) {
//            binding.unusualExpensesCv.setOnClickListener(view1 -> {
//                Navigation.findNavController(view1).navigate(R.id.action_homeFragment_to_unusualExpensesFragment);
//            });
//        } else {
//            binding.unusualExpensesCv.setVisibility(View.INVISIBLE);
//        }

        recyclerView = view.findViewById(R.id.transactions_recList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        transactionsList = new ArrayList<>();

        String id = FirebaseFirestore.getInstance().collection(User.COLLECTION).document().getId();
        transactionsList.add(new Transaction(id, "19.05.2023", 199, "Super-Pharm", true, 1));
        transactionsList.add(new Transaction(id, "01.06.2023", 129, "AM PM", true, 1));
        transactionsList.add(new Transaction(id, "06.06.2023", 550, "KSP", true, 1));
        transactionsList.add(new Transaction(id, "06.06.2023", 1999, "KSP", true, 1));

        homeAdapter = new HomeAdapter(getContext(), transactionsList);
        recyclerView.setAdapter(homeAdapter);
        homeAdapter.notifyDataSetChanged();

        calculateZScoresList(transactionsList);

        return view;
    }


    // Parent layout
    private void initializeParentViews() {
        binding.linkPrepaidCardCv.setOnClickListener(view1 -> {
            Navigation.findNavController(view1).navigate(R.id.action_homeFragment_to_linkChildCardFragment);
        });
        binding.linkCardCv.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_linkCardFragment);
        });

        binding.transferMoneyCv.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_transferMoneyFragment2);
        });

        binding.unusualExpensesCv.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_unusualExpensesFragment);
        });

    }

    // Child layout
    private void initializeChildViews() {
        bindingChildren.transferMoneyCv.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_transferMoneyFragment2);
        });
        bindingChildren.unusualExpensesCv.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_unusualExpensesFragment);
        });
    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;
//        bindingChildren = null;
//    }


    // Z-score algorithm for Irregular Expense (on transactions List)
    public void calculateZScoresList(List<Transaction> transactionsList) {

        // Calculate the mean and standard deviation of the expenses
        double mean = 0.0;
        double stdDev = 0.0;
        for (Transaction transaction : transactionsList) {
            mean += transaction.getAmount();
        }
        mean /= transactionsList.size();
        Log.d("result", "This is the mean: " + String.valueOf(mean));

        for (Transaction transaction : transactionsList) {
            stdDev += Math.pow(transaction.getAmount() - mean, 2);
        }
        stdDev = Math.sqrt(stdDev / (transactionsList.size() - 1));
        Log.d("result", "This is the stdDev: " + String.valueOf(stdDev));

        // Set the Z-score threshold
        double zScoreThreshold = 1.4;
        Log.d("result", "Our zScore Threshold: " + String.valueOf(zScoreThreshold));

        // Calculate the Z-score for each expense and mark irregular expenses
        for (Transaction transaction : transactionsList) {
            double zScore = (transaction.getAmount() - mean) / stdDev;
            //set ZScore for the expense
            transaction.setZScore(zScore);
            Log.d("result", "This is the zScore of " + transaction.getAmount() + " expense: " + String.valueOf(transaction.getZScore()));

            if (Math.abs(zScore) > zScoreThreshold) {
                transaction.setUnusual(true);
                Log.d("result", "Irregular Expense of: " + String.valueOf(transaction.getAmount()));
            } else {
                transaction.setUnusual(false);
                Log.d("result", "Normal Expense of: " + String.valueOf(transaction.getAmount()));
            }
        }
    }


}