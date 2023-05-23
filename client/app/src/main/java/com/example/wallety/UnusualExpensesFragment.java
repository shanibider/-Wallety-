package com.example.wallety;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wallety.databinding.FragmentUnusualExpensesBinding;

public class UnusualExpensesFragment extends Fragment {
    FragmentUnusualExpensesBinding binding;


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

        return view;
    }
}