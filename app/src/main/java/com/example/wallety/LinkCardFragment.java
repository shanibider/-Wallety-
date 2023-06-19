package com.example.wallety;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.cardform.view.CardForm;
import com.example.wallety.databinding.FragmentLinkCardBinding;
import com.example.wallety.model.CreditCard;
import com.example.wallety.model.Model;
import com.example.wallety.model.server.LinkCardRequest;

public class LinkCardFragment extends Fragment {
    FragmentLinkCardBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLinkCardBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

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

           cvv = cardForm.getCvv();
//            t5.setText(cvv);

//           CreditCard card = new CreditCard(name, year, month, code, cvv);
//            LinkCardRequest request = new LinkCardRequest(card, Model.instance().getCurrentUser().getAccessToken());
//
//            Model.instance().linkCard(request,
//                    (success) -> {
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                Navigation.findNavController(view1).popBackStack();
//                            }
//                        }, 4000);
//
//
//                    },
//                    (error) -> {
//                        Toast.makeText(getActivity(), "Error occurred",
//                                Toast.LENGTH_SHORT).show();
//                    }
//            );
        });

        return view;
    }
}


