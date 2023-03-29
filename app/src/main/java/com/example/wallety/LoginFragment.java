package com.example.wallety;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.wallety.databinding.FragmentLoginBinding;
import com.example.wallety.model.Model;

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
            String email = binding.emailEt.getText().toString();
            String password = binding.passwordEt.getText().toString();

            if (email.length() > 0 && password.length() > 0) {
                startActivity(intent);
                getActivity().finish();

//                Model.instance().loginUser(email, password,
//                        (success) -> {
////                            startActivity(intent);
////                            getActivity().finish();
//                        },
//                        (error) -> {
//                            Toast.makeText(getActivity(), "Invalid details",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                );
            } else {
                if (email.length() == 0) {
                    binding.emailEt.setError("Required");
                }
                if (password.length() == 0) {
//                    binding.passwordEt.setError("Required");
                }

            }


//            startActivity(intent);
//            getActivity().finish();
        });

        return view;
    }
}