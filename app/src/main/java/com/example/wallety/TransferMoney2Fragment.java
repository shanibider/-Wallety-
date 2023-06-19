package com.example.wallety;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
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

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class TransferMoney2Fragment extends Fragment {
    final String FIRST_AMOUNT_OPTION = "50";
    final String SECOND_AMOUNT_OPTION = "100";
    final String THIRD_AMOUNT_OPTION = "150";
    FragmentTransferMoney2Binding binding;

    private static DocumentReference db;
    static User user = Model.instance().getCurrentUser();

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
        } catch (NullPointerException e) {
        }
        if (actionBar != null) {
            actionBar.hide();
        }

        if (!Model.instance().getCurrentUser().isParent()) {
            binding.chooseGoal.setVisibility(View.INVISIBLE);
        }

        binding.amount.setFilters(new InputFilter[]{new MinMaxFilter()});


        // transfer Btn
        binding.transferBtn.setOnClickListener(view1 -> {
            String receiver = binding.name.getText().toString();
            String transactionAmountText = binding.amount.getText().toString();

            if (receiver.length() > 0 && transactionAmountText.length() > 0) {
                int transactionAmount = Integer.parseInt(transactionAmountText);
                boolean isUnusual = isTransactionUnusual(user.getTransactions(), transactionAmount);
                String id = FirebaseFirestore.getInstance().collection(User.COLLECTION).document().getId();

                Transaction transaction = new Transaction(id, transactionAmount, receiver, isUnusual);
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
            } else {
                if (receiver.length() == 0) {
                    binding.name.setError("Cannot be empty");
                }
                if (transactionAmountText.length() == 0) {
                    binding.amount.setError("Cannot be empty");
                }

            }

        });


        // retrieve goals from db for spinner dropdown
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
        amountOptionCv.setOnClickListener(unused -> {
            binding.amount.setText(amount);
            binding.amount.setError(null);
        });
    }

    // Z-score algorithm for Irregular Expense (on transactions List)
    public boolean isTransactionUnusual(List<Transaction> transactionsList, int transactionAmount) {

        // Calculate the mean and standard deviation of the expenses
        double mean = 0.0;
        double stdDev = 0.0;
        for (Transaction transaction : transactionsList) {
            mean += transaction.getAmount();
        }
        mean /= transactionsList.size();

        for (Transaction transaction : transactionsList) {
            stdDev += Math.pow(transaction.getAmount() - mean, 2);
        }
        stdDev = Math.sqrt(stdDev / (transactionsList.size() - 1));

        // Set the Z-score threshold
        double zScoreThreshold = 1.4;

        // Calculate the Z-score for each expense and mark irregular expenses
        double zScore = (transactionAmount - mean) / stdDev;
        boolean isUnusual = Math.abs(zScore) > zScoreThreshold;

        return isUnusual;
    }

    //    Limit transaction amount (between 1 to user balance)
    private static class MinMaxFilter implements InputFilter {
        private int mIntMin, mIntMax;

        public MinMaxFilter() {
            this.mIntMin = 1;
            this.mIntMax = user.getBalance();
        }

        private boolean isInRange(int a, int b, int c) {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                int input = Integer.parseInt(dest.toString() + source.toString());
                if (isInRange(mIntMin, mIntMax, input))
                    return null;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            return "";
        }
    }
}