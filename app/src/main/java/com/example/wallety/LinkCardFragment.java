package com.example.wallety;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.braintreepayments.cardform.view.CardForm;
import com.example.wallety.databinding.FragmentLinkCardBinding;


public class LinkCardFragment extends Fragment {
    FragmentLinkCardBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLinkCardBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        CardForm cardForm = binding.cardForm;
        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .actionLabel("Purchase")
                .setup(getActivity());

        return view;
    }
}