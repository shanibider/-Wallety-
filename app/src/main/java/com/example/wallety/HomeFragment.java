package com.example.wallety;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.braintreepayments.cardform.view.CardForm;
import com.example.wallety.adapters.HomeAdapter;
import com.example.wallety.databinding.FragmentHomeBinding;
import com.example.wallety.model.Transactions;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    RecyclerView recyclerView;
    List<Transactions> transactionsList;
    HomeAdapter homeAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.linkCardCv.setOnClickListener(view1 -> {
            Navigation.findNavController(view1).navigate(R.id.action_homeFragment_to_linkCardFragment);
        });

        binding.savingMoneyCv.setOnClickListener(view1 -> {
            Navigation.findNavController(view1).navigate(R.id.action_homeFragment_to_savingMoneyFragment);
        });


        recyclerView = view.findViewById(R.id.transactions_recList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        transactionsList = new ArrayList<>();

        transactionsList.add(new Transactions(R.drawable.shopping_cart, "Super market", "08/04/2023", " - ₪159"));
        transactionsList.add(new Transactions(R.drawable.shopping_bag, "KSP", "08/04/2023", " - ₪299"));
        transactionsList.add(new Transactions(R.drawable.shopping_cart, "AM PM", "07/04/2023", " - ₪79"));
        transactionsList.add(new Transactions(R.drawable.shopping_bag, "Shopping", "03/04/2023", " - ₪250"));
        transactionsList.add(new Transactions(R.drawable.parents_transfer, "Mom transfer", "01/04/2023", " + ₪200"));

        homeAdapter= new HomeAdapter(getContext(), transactionsList);
        recyclerView.setAdapter(homeAdapter);
        homeAdapter.notifyDataSetChanged();


        return view;
    }
}