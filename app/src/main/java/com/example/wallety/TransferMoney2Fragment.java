package com.example.wallety;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.wallety.databinding.FragmentTransferMoney2Binding;
import com.example.wallety.model.Model;
import com.example.wallety.model.Saving;
import com.example.wallety.model.Transaction;
import com.example.wallety.model.User;
import com.example.wallety.model.server.TransactionRequest;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TransferMoney2Fragment extends Fragment {
    final String NO_ENTITY = "None";
    final String FIRST_AMOUNT_OPTION = "50";
    final String SECOND_AMOUNT_OPTION = "100";
    final String THIRD_AMOUNT_OPTION = "150";
    FragmentTransferMoney2Binding binding;

    static User user = Model.instance().getCurrentUser();
    List<Saving> savings = user.getSavings();
    List<String> allSavingsGoals = new ArrayList<>();
    User selectedChild;
    Saving selectedSaving;

    private void initializeGoalsDropdown() {
        binding.goalDropdown.setText(NO_ENTITY);
        allSavingsGoals.clear();
        allSavingsGoals.add(NO_ENTITY);

        if (savings != null) {
            List<String> savingsGoals = savings.stream()
                    .map(Saving::getGoal)
                    .collect(Collectors.toList());
            allSavingsGoals.addAll(savingsGoals);
        }

        ArrayAdapter<String> savingsAdapter = new ArrayAdapter<>
                (getContext(), android.R.layout.simple_spinner_dropdown_item, allSavingsGoals);
        binding.goalDropdown.setAdapter(savingsAdapter);
    }

    private void handleChildSelection() {
        savings = Model.instance().getChildSavingsById(selectedChild.getId());
        initializeGoalsDropdown();
    }

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

        if (user.isParent()) {
            binding.nameLayout.setVisibility(View.INVISIBLE);
            binding.chooseChildLayout.setVisibility(View.VISIBLE);

            selectedChild = user.getChildren().get(0);
            binding.childrenDropdown.setSelection(0);
            binding.childrenDropdown.setText(selectedChild.getName(), false);

            List<String> childrenNames = Model.instance().getChildrenNames();
            ArrayAdapter<String> childrenAdapter = new ArrayAdapter<>
                    (getContext(), android.R.layout.simple_spinner_dropdown_item, childrenNames);
            binding.childrenDropdown.setAdapter(childrenAdapter);

            binding.childrenDropdown.setOnItemClickListener((parent, view1, position, id) -> {
                selectedChild = user.getChildren().get(position);
                handleChildSelection();
            });
            handleChildSelection();
        } else {
            initializeGoalsDropdown();
        }
        binding.amount.setFilters(new InputFilter[]{new MinMaxFilter()});

        binding.goalDropdown.setOnItemClickListener((parent, view1, position, id) -> {
//                first goal is NONE
            selectedSaving = (position == 0) ? null : savings.get(position - 1);
            if (!user.isParent()) {
//                 if goal is chosen, there is no receiver
                if (selectedSaving != null) {
                    binding.name.setEnabled(false);
                    binding.name.setText(selectedSaving.getGoal());
                } else {
                    binding.name.setEnabled(true);
                    binding.name.setText("");
                }
            }
        });

        // transfer Btn
        binding.transferBtn.setOnClickListener(view1 -> {
            String receiver = binding.name.getText().toString();
            String transactionAmountText = binding.amount.getText().toString();

            if ((user.isParent() || receiver.length() > 0) && transactionAmountText.length() > 0) {
                int transactionAmount = Integer.parseInt(transactionAmountText);
                boolean isUnusual = !user.isParent() && isTransactionUnusual(user.getTransactions(), transactionAmount);

                Transaction transaction = new Transaction(transactionAmount, receiver, isUnusual);
                if (selectedChild != null) {
                    receiver = selectedChild.getName();
                    transaction.setReceiver(selectedChild.getName());
                    transaction.setChildReceiver(selectedChild);
                }
                if (selectedSaving != null) {
                    receiver = selectedSaving.getGoal() + " - Saving";
                    transaction.setReceiver(receiver);
                    transaction.setSaving(selectedSaving);
                }
                TransactionRequest transactionRequest = new TransactionRequest(transaction, user.getAccessToken());

                String updatedReceiver = receiver;
                Model.instance().makeTransaction(transactionRequest,
                        (success) -> {
                            TransferMoney2FragmentDirections.ActionTransferMoneyFragment2ToMoneySentFragment action =
                                    TransferMoney2FragmentDirections
                                            .actionTransferMoneyFragment2ToMoneySentFragment(updatedReceiver, transactionAmount);
                            Navigation.findNavController(view1).navigate(action);
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

        handleAmountOptionClick(binding.firstAmountOptionCv, FIRST_AMOUNT_OPTION);
        handleAmountOptionClick(binding.secondAmountOptionCv, SECOND_AMOUNT_OPTION);
        handleAmountOptionClick(binding.thirdAmountOptionCv, THIRD_AMOUNT_OPTION);

        return view;
    }

    private void handleAmountOptionClick(MaterialCardView amountOptionCv, String amount) {
        amountOptionCv.setOnClickListener(unused -> {
            if (user.getBalance() > Integer.parseInt(amount)) {
                binding.amount.setText(amount);
                binding.amount.setError(null);
            }
        });
    }

    // Z-score algorithm for Irregular Expense (on transactions List)
    public boolean isTransactionUnusual(List<Transaction> transactionsList, int transactionAmount) {
        if (transactionsList == null)
            return false;

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