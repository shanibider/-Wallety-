package com.example.wallety;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;
import com.braintreepayments.cardform.view.CardForm;
import com.example.wallety.databinding.FragmentLinkChildCardBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LinkChildCardFragment extends Fragment {

        FragmentLinkChildCardBinding binding;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            binding = FragmentLinkChildCardBinding.inflate(inflater, container, false);
            View view = binding.getRoot();

            // hide actionBar
            ActionBar actionBar = null;
            try {
                actionBar = Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar());
            } catch (NullPointerException e) {}

            if (actionBar != null) {
                actionBar.hide();
            }

            TextView t1, t2, t3, t4, t5;

            CardForm cardForm = binding.cardForm;
            cardForm.cardRequired(true)
                    .expirationRequired(true)
                    .cvvRequired(true)
                    .cardholderName(CardForm.FIELD_REQUIRED)
                    .actionLabel("Purchase")
                    .setup(getActivity());

            t1= view.findViewById(R.id.code);
            t2= view.findViewById(R.id.holder);
            t3= view.findViewById(R.id.month1);
            t4= view.findViewById(R.id.year1);
            //t5= view.findViewById(R.id.cvv);

            binding.linkCardBtn.setOnClickListener(view1 -> {
                //display card detail on card template
                String cvv, name, month, year, code;
                code= cardForm.getCardNumber();
                t1.setText(code);

                name= cardForm.getCardholderName();
                t2.setText(name);

                month= cardForm.getExpirationMonth();
                t3.setText(month);

                year= cardForm.getExpirationYear();
                t4.setText(year);
//            cvv = cardForm.getCvv();
//            t5.setText(cvv);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Navigation.findNavController(view1).popBackStack();
                    }
                }, 4000);
            });





            List<String> Childs = new ArrayList<>();
            Childs.add("Ron");
            Childs.add("Chen");
            Childs.add("Adam");

            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, Childs);
            binding.creditCardDropdown.setAdapter(adapter);









            return view;
        }
    }


