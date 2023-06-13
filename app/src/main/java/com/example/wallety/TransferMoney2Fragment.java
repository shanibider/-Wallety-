package com.example.wallety;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wallety.databinding.FragmentTransferMoney2Binding;
import com.example.wallety.model.Model;
import com.example.wallety.model.Transaction;
import com.example.wallety.model.User;
import com.example.wallety.model.server.TransactionRequest;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class TransferMoney2Fragment extends Fragment {
    final String FIRST_AMOUNT_OPTION = "50";
    final String SECOND_AMOUNT_OPTION = "100";
    final String THIRD_AMOUNT_OPTION = "150";
    FragmentTransferMoney2Binding binding;
    String firstAmountOption = FIRST_AMOUNT_OPTION;

    EditText t1;

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
               String id = FirebaseFirestore.getInstance().collection(User.COLLECTION).document().getId();
               Transaction transaction = new Transaction(id, "16.04.2023", 500, "AM PM", true);
               TransactionRequest transactionRequest = new TransactionRequest(transaction);

               Model.instance().makeTransaction(transactionRequest,
                       (success) -> {
                           Navigation.findNavController(view1).navigate(R.id.action_transferMoneyFragment2_to_moneySentFragment);
                       },
                       (error) -> {
                           Toast.makeText(getActivity(), "Error occurred",
                                   Toast.LENGTH_SHORT).show();
                       }
               );
           });

           handleAmountOptionClick(binding.firstAmountOptionCv, FIRST_AMOUNT_OPTION);
           handleAmountOptionClick(binding.secondAmountOptionCv, SECOND_AMOUNT_OPTION);
           handleAmountOptionClick(binding.thirdAmountOptionCv, THIRD_AMOUNT_OPTION);

//           t1= view.findViewById(R.id.amountEt);
//           String amount = t1.getText().toString();
//           Log.d("SendingFragment", "Amount value: " + amount);
//           Bundle bundle = new Bundle();
//           bundle.putString("amount", amount);
//           MoneySentFragment fragment = new MoneySentFragment();
//           fragment.setArguments(bundle);

           return view;
    }


    private void handleAmountOptionClick(MaterialCardView amountOptionCv, String amount) {
        amountOptionCv.setOnClickListener(unused -> binding.amountEt.setText(amount));
    }
}