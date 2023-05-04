package com.example.wallety;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.wallety.databinding.FragmentTransferMoney2Binding;
import com.example.wallety.databinding.FragmentTransferMoneyBinding;
import com.google.android.material.card.MaterialCardView;

import java.util.Objects;

public class TransferMoney2Fragment extends Fragment {
    final String FIRST_AMOUNT_OPTION = "50";
    final String SECOND_AMOUNT_OPTION = "100";
    final String THIRD_AMOUNT_OPTION = "150";
    FragmentTransferMoney2Binding binding;


       @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
           // Inflate the layout for this fragment
           binding = FragmentTransferMoney2Binding.inflate(inflater, container, false);
           View view = binding.getRoot();

           // hide actionBar
           ActionBar actionBar = null;
           try {
               actionBar = Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar());
           } catch (NullPointerException e) {}

           if (actionBar != null) {
               actionBar.hide();
           }

           binding.transferBtn.setOnClickListener(view1 -> {
               Navigation.findNavController(view1).popBackStack();
           });

           handleAmountOptionClick(binding.firstAmountOptionCv, FIRST_AMOUNT_OPTION);
           handleAmountOptionClick(binding.secondAmountOptionCv, SECOND_AMOUNT_OPTION);
           handleAmountOptionClick(binding.thirdAmountOptionCv, THIRD_AMOUNT_OPTION);



           return view;
    }

    private void handleAmountOptionClick(MaterialCardView amountOptionCv, String amount) {
        amountOptionCv.setOnClickListener(unused -> binding.amountEt.setText(amount));
    }
}