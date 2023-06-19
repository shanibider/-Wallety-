package com.example.wallety;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import com.example.wallety.databinding.FragmentMyFamilyBinding;
import com.example.wallety.model.CreditCard;
import com.example.wallety.model.Model;
import com.example.wallety.model.User;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.stream.Collectors;

public class MyFamilyFragment extends Fragment {
    FragmentMyFamilyBinding binding;
    User selectedChild;

    //updates the UI based on the selected child
    private void handleChildSelection() {
        // display the selected child's balance
        String formattedBalance = String.valueOf(selectedChild.getBalance()) + '₪';
        binding.balanceTv.setText(formattedBalance);

        // updates the credit card view
        CreditCard creditCard = Model.instance().getCreditCard();
        if (creditCard != null) {
            binding.creditCardView.setBackgroundResource(com.vinaygaba.creditcardview.R.drawable.cardbackground_world);
            binding.creditCardView.setCardNumber(creditCard.getCardNum());
            binding.creditCardView.setCardName(creditCard.getHolderName());
            String expiryDate = creditCard.getMonth() + '/' + creditCard.getYear();
            binding.creditCardView.setExpiryDate(expiryDate);
        } else {
            binding.creditCardView.setBackgroundResource(com.vinaygaba.creditcardview.R.drawable.cardbackground_world);
            binding.creditCardView.setCardNumber("No Card Yet");
            binding.creditCardView.setExpiryDate("MM/YY");
            binding.creditCardView.setCardName("Holder Name");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMyFamilyBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        //retrieves the list of children from the current user
        List<User> children = Model.instance().getCurrentUser().getChildren();
        // assigns the first child
        selectedChild = children.get(0);

        List<String> childrenNames = children.stream()
                .map(User::getName)
                .collect(Collectors.toList());

        // array adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, childrenNames);

        binding.childDropdown.setAdapter(adapter);
        binding.childDropdown.setSelection(0);
        binding.childDropdown.setText(selectedChild.getName(), false);

        // updates the selectedChild variable and calls handleChildSelection() to update the UI accordingly
        binding.childDropdown.setOnItemClickListener((parent, view1, position, id) -> {
            selectedChild = children.get(position);
            handleChildSelection();
        });
        handleChildSelection();




        return view;
    }
}