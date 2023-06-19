package com.example.wallety;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wallety.adapters.HomeAdapter;
import com.example.wallety.databinding.FragmentChildrenHomeBinding;
import com.example.wallety.databinding.FragmentHomeBinding;
import com.example.wallety.model.CreditCard;
import com.example.wallety.model.Model;
import com.example.wallety.model.Transaction;
import com.example.wallety.model.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    FragmentChildrenHomeBinding bindingChildren;

    RecyclerView recyclerView;
    List<Transaction> transactionsList;
    HomeAdapter homeAdapter;
    View view;
    private View partialView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // get Current User
        User user = Model.instance().getCurrentUser();
        String nameHeader = "Hello " + Model.instance().getCurrentUser().getName();
        int balanceHeader = Model.instance().getCurrentUser().getBalance();
        String balanceText = balanceHeader + "₪";
        String nameHolder = Model.instance().getCurrentUser().getName();


        // Inflate the appropriate layout based on the user type
        if (user.isParent()) {
            binding = FragmentHomeBinding.inflate(inflater, container, false);
            view = binding.getRoot();
            initializeParentViews();

            binding.nameHeaderTv.setText(nameHeader);
//            binding.holder.setText(nameHolder);
        } else {
            bindingChildren = FragmentChildrenHomeBinding.inflate(inflater, container, false);
            view = bindingChildren.getRoot();
            initializeChildViews();

            bindingChildren.nameHeaderTv.setText(nameHeader);
//            bindingChildren.holder.setText(nameHolder);
        }

        partialView = view.findViewById(R.id.partial);

        CreditCard creditCard = user.getCreditCard();

        if (creditCard != null) {
            TextView cardNumTv = partialView.findViewById(R.id.code);
            TextView holderTv = partialView.findViewById(R.id.holder);
            TextView monthTv = partialView.findViewById(R.id.month1);
            TextView yearTv = partialView.findViewById(R.id.year1);
            TextView balanceTv = partialView.findViewById(R.id.balanceTv);
            String formatterCardNum = creditCard.getCardNum()
                    .replaceAll(".{4}(?!$)", "$0" + ' ');
            cardNumTv.setText(formatterCardNum);
            holderTv.setText(creditCard.getHolderName());
            monthTv.setText(creditCard.getMonth() + '/');
            yearTv.setText(creditCard.getYear());
            String formattedBalance = String.valueOf(user.getBalance()) + '₪';
            balanceTv.setText(formattedBalance);
        }
        else{
            TextView cardNumTv = partialView.findViewById(R.id.code);
            TextView holderTv = partialView.findViewById(R.id.holder);
            TextView monthTv = partialView.findViewById(R.id.month1);
            TextView yearTv = partialView.findViewById(R.id.year1);
            TextView balanceTv = partialView.findViewById(R.id.balanceTv);
            cardNumTv.setText("Child Balance");
            holderTv.setText("");
            monthTv.setText("");
            yearTv.setText("");
            String formattedBalance = String.valueOf(user.getBalance()) + '₪';
            balanceTv.setText(formattedBalance);
        }

        recyclerView = view.findViewById(R.id.transactions_recList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        transactionsList = new ArrayList<>();

        String id = FirebaseFirestore.getInstance().collection(User.COLLECTION).document().getId();
        transactionsList.add(new Transaction(id, 199, "Super-Pharm", true));
        transactionsList.add(new Transaction(id, 129, "AM PM", true));
        transactionsList.add(new Transaction(id, 550, "KSP", true));
        transactionsList.add(new Transaction(id,  1999, "KSP", true));


        homeAdapter = new HomeAdapter(getContext(), transactionsList);
        recyclerView.setAdapter(homeAdapter);
        homeAdapter.notifyDataSetChanged();

        return view;
    }


    // Parent layout
    private void initializeParentViews() {
        binding.linkPrepaidCardCv.setOnClickListener(view1 -> {
            Navigation.findNavController(view1).navigate(R.id.action_homeFragment_to_linkChildCardFragment);
        });
        binding.linkCardCv.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_linkCardFragment);
        });

        binding.transferMoneyCv.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_transferMoneyFragment2);
        });

        binding.unusualExpensesCv.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_unusualExpensesFragment);
        });

    }

    // Child layout
    private void initializeChildViews() {
        bindingChildren.transferMoneyCv.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_transferMoneyFragment2);
        });
        bindingChildren.goalsCv.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_savingMoneyFragment);
        });
        bindingChildren.tasksCv.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_tasksFragment);
        });
    }
}