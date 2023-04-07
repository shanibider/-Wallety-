package com.example.wallety;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.wallety.databinding.FragmentLinkCardBinding;
import com.example.wallety.databinding.FragmentSavingMoneyBinding;

public class SavingMoneyFragment extends Fragment {
    FragmentSavingMoneyBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSavingMoneyBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        TextView tv1, tv2;
        EditText et3, et2;

        tv1 = view.findViewById(R.id.tv1);
        et3 =  view.findViewById(R.id.et3);
        tv2 = view.findViewById(R.id.tv2);
        et2 =  view.findViewById(R.id.et2);

        binding.goalBtn.setOnClickListener(view1 -> {

            String goal;
            goal= et3.getText().toString();
            tv1.setText(goal);


        });


        binding.transferBtn.setOnClickListener(view1 -> {
            String balance;
            balance= et2.getText().toString();
            tv2.setText(balance);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Navigation.findNavController(view1).popBackStack();
                }
            }, 4000);



        });


        return view;

    }
}
