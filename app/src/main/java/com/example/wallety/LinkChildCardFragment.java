package com.example.wallety;

import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.braintreepayments.cardform.view.CardForm;
import com.example.wallety.databinding.FragmentLinkChildCardBinding;
import com.example.wallety.model.CreditCard;
import com.example.wallety.model.Model;
<<<<<<< HEAD
import com.example.wallety.model.User;
import com.example.wallety.model.server.LinkCardRequest;
import com.example.wallety.model.server.LinkCardToChildRequest;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
=======
import com.example.wallety.model.Transaction;
import com.example.wallety.model.User;
import com.example.wallety.model.server.TransactionRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
>>>>>>> master

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class LinkChildCardFragment extends Fragment {

    FragmentLinkChildCardBinding binding;
    private static DocumentReference db;
    static User user;
    User selectedChild;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLinkChildCardBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

<<<<<<< HEAD
            // hide actionBar
            ActionBar actionBar = null;
            try {
                actionBar = Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar());
            } catch (NullPointerException e) {}

            if (actionBar != null) {
                actionBar.hide();
            }

            TextView t1, t2, t3, t4, loadAmount;
            MaterialAutoCompleteTextView chosenChild;

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
            loadAmount = view.findViewById(R.id.loadAmount);
            chosenChild = view.findViewById(R.id.creditCardDropdown);

            binding.linkCardBtn.setOnClickListener(view2 -> {
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

                int amount = Integer.parseInt(loadAmount.getText().toString());

                String childName = chosenChild.getText().toString();

                CreditCard card = new CreditCard(name, year, month, code, cvv);
                LinkCardToChildRequest request = new LinkCardToChildRequest(card, Model.instance().getCurrentUser().getAccessToken(), childName, amount);

                Model.instance().linkCardToChild(request,
                        (success) -> {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Navigation.findNavController(view2).popBackStack();
                                }
                            }, 4000);


                        },
                        (error) -> {
                            Toast.makeText(getActivity(), "Error occurred",
                                    Toast.LENGTH_SHORT).show();
                        }
                );
            });


            List<String> childrenName = new ArrayList<>();
            List<User> children = Model.instance().getCurrentUser().getChildren();
            for(User child : children){
                childrenName.add(child.getName());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, childrenName);
            binding.creditCardDropdown.setAdapter(adapter);


            return view;
=======
        // hide actionBar
        ActionBar actionBar = null;
        try {
            actionBar = Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar());
        } catch (NullPointerException e) {
>>>>>>> master
        }

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

        t1 = view.findViewById(R.id.code);
        t2 = view.findViewById(R.id.holder);
        t3 = view.findViewById(R.id.month1);
        t4 = view.findViewById(R.id.year1);
        //t5= view.findViewById(R.id.cvv);

        // retrieve children from db for spinner dropdown
        user = Model.instance().getCurrentUser();
        db = FirebaseFirestore.getInstance().collection("users").document(user.getId());

        // retrieves list of children from current user
        List<User> children = Model.instance().getCurrentUser().getChildren();
        selectedChild = children.get(0);

        List<String> childrenNames = children.stream()
                .map(User::getName)
                .collect(Collectors.toList());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, childrenNames);
        binding.creditCardDropdown.setAdapter(adapter);
        binding.creditCardDropdown.setSelection(0);
        binding.creditCardDropdown.setText(selectedChild.getName(), false);

        binding.creditCardDropdown.setOnItemClickListener((parent, view1, position, id) -> {
            selectedChild = children.get(position);

        });


        // linkCardBtn
        binding.linkCardBtn.setOnClickListener(view1 -> {
            //display card detail on card template
            String cvv, name, month, year, code;
            code = cardForm.getCardNumber();
            t1.setText(code);
            name = cardForm.getCardholderName();
            t2.setText(name);
            month = cardForm.getExpirationMonth();
            t3.setText(month);
            year = cardForm.getExpirationYear();
            t4.setText(year);
//          cvv = cardForm.getCvv();
//          t5.setText(cvv);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Navigation.findNavController(view1).popBackStack();
                }
            }, 4000);





            // Increase the balance field of the selected child user by 50
//            String childUserId = selectedChild.getId();
//            DocumentReference childUserRef = db.collection("users").document(childUserId);
//
//            childUserRef.update("balance", FieldValue.increment(50))
//                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            // Balance updated successfully
//                            // Perform any necessary actions
//                            Toast.makeText(getActivity(), "Balance updated successfully", Toast.LENGTH_SHORT).show();//
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            // An error occurred while updating the balance
//                            // Handle the error
//                        }
//                    });
        });

        return view;
    }


}