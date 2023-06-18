package com.example.wallety;

import android.animation.AnimatorSet;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.wallety.activities.MainActivity;
import com.example.wallety.databinding.FragmentSignUpBinding;
import com.example.wallety.model.Model;
import com.example.wallety.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignUpFragment extends Fragment {
    FragmentSignUpBinding binding;
    static Map<String, CheckBox> addedCheckboxesByEmail = new HashMap<>();
    static List<String> addedChildrenIds = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        Intent intent = new Intent(getActivity(), MainActivity.class);
        binding.parentRb.setOnClickListener(view1 -> {
            if (((RadioButton) view1).isChecked()) {
                Navigation.findNavController(view1)
                        .navigate(R.id.action_signUpFragment_to_addChildrenFragment);
            }
        });
        binding.signUpBtn.setOnClickListener(view1 -> {
            String name = binding.nameEt.getText().toString().trim();
            String phone = binding.phoneEt.getText().toString().trim();
            String email = binding.emailEt.getText().toString().trim();
            String password = binding.passwordEt.getText().toString().trim();
            String confirmedPassword = binding.confirmPasswordEt.getText().toString().trim();
            binding.passwordTv.setErrorIconDrawable(null);
            binding.passwordTv.setError(password.length() < 6 ? "Must have more than 5 characters" : null);
            int balance = 100;

            boolean isValidPhone = Patterns.PHONE.matcher(phone).matches();
            boolean isValidEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches();
            boolean isParent = binding.parentRb.isChecked();
            boolean isValidParent = !isParent || addedChildrenIds.size() > 0;

            if (name.length() > 0 && email.length() > 0 && isValidPhone && isValidEmail &&
                    password.length() > 5 && password.equals(confirmedPassword) && isValidParent
            ) {
                User user = new User(name, phone, email, password, balance);

                if (isParent) {
                    user.setChildren(addedChildrenIds);
                }
                Model.instance().createUser(user,
                        (onSuccess) -> {
                            startActivity(intent);
                            getActivity().finish();
                        },
                        (existingDetail) -> {
                            String error = existingDetail.length() > 0
                                    ? existingDetail + " already exists" : "Error occurred";
                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                        });
            } else {
                if (name.length() == 0) {
                    binding.nameEt.setError("Required");
                }
                if (phone.length() == 0) {
                    binding.phoneEt.setError("Required");
                } else if (!isValidPhone) {
                    binding.phoneEt.setError("Invalid phone number");
                }
                if (email.length() == 0) {
                    binding.emailEt.setError("Required");
                } else if (!isValidEmail) {
                    binding.emailEt.setError("Invalid email");
                }
                if (!password.equals(confirmedPassword)) {
                    binding.confirmPasswordEt.setError("Must be equal to password");
                }
                if (!isValidParent) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext())
                            .setTitle("Error")
                            .setMessage("You have to choose children as a parent")
                            .setPositiveButton("OK",null);
                    alert.show();
                }
            }
        });

        binding.loginTv.setOnClickListener(view1 -> {
            Navigation.findNavController(view1).popBackStack();
        });

        return view;
    }
}