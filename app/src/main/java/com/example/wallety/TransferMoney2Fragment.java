package com.example.wallety;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wallety.databinding.FragmentTransferMoney2Binding;
import com.example.wallety.model.Model;
import com.example.wallety.model.Saving;
import com.example.wallety.model.Transaction;
import com.example.wallety.model.User;
import com.example.wallety.model.server.TransactionRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TransferMoney2Fragment extends Fragment {
    final String FIRST_AMOUNT_OPTION = "50";
    final String SECOND_AMOUNT_OPTION = "100";
    final String THIRD_AMOUNT_OPTION = "150";
    FragmentTransferMoney2Binding binding;
    String firstAmountOption = FIRST_AMOUNT_OPTION;

    private static DocumentReference db;
    static User user;

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




        // transfer Btn
        binding.transferBtn.setOnClickListener(view1 -> {
            String id = FirebaseFirestore.getInstance().collection(User.COLLECTION).document().getId();
            Transaction transaction = new Transaction(id, "16.04.2023", 500, "AM PM", true, 1);

            TransactionRequest transactionRequest = new TransactionRequest(transaction);
            TransactionRequest transactionRequest = new TransactionRequest(transaction, Model.instance().getCurrentUser().getAccessToken());

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






        // retrieve goals from db for spinner dropdown
        user = Model.instance().getCurrentUser();
        db = FirebaseFirestore.getInstance().collection("users").document(user.getId());

        List<String> goalsList = new ArrayList<>();

        if (goalsList != null)
            goalsList.clear();
        db.collection("saving")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String goal = (String) document.get("goal");
                                goalsList.add(goal);
                            }
                        }
                    }
                });
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, goalsList);
        binding.goalDropdown.setAdapter(adapter);




        handleAmountOptionClick(binding.firstAmountOptionCv, FIRST_AMOUNT_OPTION);
        handleAmountOptionClick(binding.secondAmountOptionCv, SECOND_AMOUNT_OPTION);
        handleAmountOptionClick(binding.thirdAmountOptionCv, THIRD_AMOUNT_OPTION);



        return view;
    }





    private void handleAmountOptionClick(MaterialCardView amountOptionCv, String amount) {
        amountOptionCv.setOnClickListener(unused -> binding.amount.setText(amount));
    }
}