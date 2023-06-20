package com.example.wallety;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.wallety.databinding.FragmentMoneySentBinding;
import com.example.wallety.databinding.FragmentTransferMoneyBinding;
import com.example.wallety.model.Model;

public class MoneySentFragment extends Fragment {
    FragmentMoneySentBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMoneySentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        String receiver = MoneySentFragmentArgs.fromBundle(getArguments()).getReceiver();
        String nameHeader = "To " + receiver;
        int transactionAmount = MoneySentFragmentArgs.fromBundle(getArguments()).getTransactionAmount();
        String formatterTransactionAmount = String.valueOf(transactionAmount) + '₪';

        binding.to.setText(nameHeader);
        binding.amountTv.setText(formatterTransactionAmount);

        binding.returnHomeBtn.setOnClickListener(view1 -> {
            Navigation.findNavController(view1).navigate(R.id.homeFragment);
        });


        return view;
    }

}