package com.example.wallety;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wallety.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {
    FragmentLoginBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.signUpTv.setOnClickListener(view1 -> {
            Navigation.findNavController(view1).navigate(R.id.action_loginFragment_to_signUpFragment);
        });

        Intent intent = new Intent(getActivity(), MainActivity.class);
        binding.loginBtn.setOnClickListener(view1 -> {
            startActivity(intent);
            getActivity().finish();
        });

        return view;
    }
}