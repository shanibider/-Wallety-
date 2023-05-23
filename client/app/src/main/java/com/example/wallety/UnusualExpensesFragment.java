package com.example.wallety;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wallety.adapters.UnusualExpensesAdapter;
import com.example.wallety.databinding.FragmentUnusualExpensesBinding;
import com.example.wallety.model.Model;
import com.example.wallety.model.Transaction;
import com.example.wallety.model.User;
import java.util.List;

public class UnusualExpensesFragment extends Fragment {
    FragmentUnusualExpensesBinding binding;
    public static List<Transaction> unusualExpenses;
    public static TextView unusualExpensesAmountTv;
    static User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentUnusualExpensesBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // hide actionBar
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        user = Model.instance().getCurrentUser();

        // RecyclerView initialization
        binding.unusualExpensesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        unusualExpenses = Model.instance().getParentUnusualExpenses(user.getId());

        unusualExpensesAmountTv = binding.unusualExpensesAmountTv;
        unusualExpensesAmountTv.setText(String.valueOf(unusualExpenses.size()));

        UnusualExpensesAdapter unusualExpensesAdapter = new UnusualExpensesAdapter(getContext(), unusualExpenses);
        binding.unusualExpensesRecyclerView.setAdapter(unusualExpensesAdapter);
        unusualExpensesAdapter.notifyDataSetChanged();

        return view;
    }
}