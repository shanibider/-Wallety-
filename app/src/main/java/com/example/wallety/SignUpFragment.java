package com.example.wallety;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wallety.databinding.FragmentSignUpBinding;

public class SignUpFragment extends Fragment {
    FragmentSignUpBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        Intent intent = new Intent(getActivity(), MainActivity.class);
        binding.signUpBtn.setOnClickListener(view1 -> {
            startActivity(intent);
            getActivity().finish();
        });

        binding.loginTv.setOnClickListener(view1 -> {
            Navigation.findNavController(view1).popBackStack();
        });

        return view;
    }
}