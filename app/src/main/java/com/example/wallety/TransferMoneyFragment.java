package com.example.wallety;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;

import com.example.wallety.databinding.FragmentTransferMoneyBinding;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class TransferMoneyFragment extends Fragment {
    final String FIRST_AMOUNT_OPTION = "20";
    final String SECOND_AMOUNT_OPTION = "50";
    final String THIRD_AMOUNT_OPTION = "100";
    FragmentTransferMoneyBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTransferMoneyBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.transferBtn.setOnClickListener(view1 -> {
            Navigation.findNavController(view1).popBackStack();
        });

        RadioButton radioButton1 = new RadioButton(getContext());
        RadioButton radioButton2 = new RadioButton(getContext());
        RadioButton radioButton3 = new RadioButton(getContext());
        radioButton1.setText("Adam");
        radioButton2.setText("John");
        radioButton3.setText("Maya");

        binding.receiversRadioGroup.addView(radioButton1);
        binding.receiversRadioGroup.addView(radioButton2);
        binding.receiversRadioGroup.addView(radioButton3);

        handleAmountOptionClick(binding.firstAmountOptionCv, FIRST_AMOUNT_OPTION);
        handleAmountOptionClick(binding.secondAmountOptionCv, SECOND_AMOUNT_OPTION);
        handleAmountOptionClick(binding.thirdAmountOptionCv, THIRD_AMOUNT_OPTION);

        List<String> creditCards = new ArrayList<>();
        creditCards.add("**** 0713");
        creditCards.add("**** 8269");
        creditCards.add("**** 5288");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, creditCards);
        binding.creditCardDropdown.setAdapter(adapter);

        return view;
    }

    private void handleAmountOptionClick(MaterialCardView amountOptionCv, String amount) {
        amountOptionCv.setOnClickListener(unused -> binding.amountEt.setText(amount));
    }
}