package com.example.wallety;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.wallety.activities.MainActivity;
import com.example.wallety.databinding.FragmentSignUpBinding;
import com.example.wallety.model.Model;
import com.example.wallety.model.User;
import com.example.wallety.model.server.UserFetcherCon;
import com.example.wallety.model.server.UserSignUpResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
            String name = binding.nameEt.getText().toString();
            String phone = binding.phoneEt.getText().toString();
            String email = binding.emailEt.getText().toString();
            String password = binding.passwordEt.getText().toString();
            String confirmedPassword = binding.confirmPasswordEt.getText().toString();
            binding.passwordTv.setErrorIconDrawable(null);
            binding.passwordTv.setError(password.length() < 6 ? "Must have more than 5 characters" : null);

            boolean isValidPhone = Patterns.PHONE.matcher(phone).matches();
            boolean isValidEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches();
            if (name.length() > 0 && email.length() > 0 && isValidPhone && isValidEmail &&
                    password.length() > 5 && password.equals(confirmedPassword)
            ) {
                User user = new User(name, phone, email, password);

                UserFetcherCon.signUpUser(user, new Callback<UserSignUpResponse>() {
                    @Override
                    public void onResponse(Call<UserSignUpResponse> call, Response<UserSignUpResponse> response) {
                        if (response.isSuccessful()) {
                            User user = response.body().getUser();
                            if (user != null) {
                                Model.instance().setCurrentUser(user);
                                startActivity(intent);
                                getActivity().finish();
                            } else {
                                Toast.makeText(getContext(), response.body().getExistingDetail() + " already exists",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Error occurred",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserSignUpResponse> call, Throwable t) {
                        System.out.println("error " + t.getMessage());
                        Toast.makeText(getActivity(), "Error occurred",
                                Toast.LENGTH_SHORT).show();
                    }
                });


//                Model.instance().createUser(user,
//                        (onSuccess) -> {
//                            startActivity(intent);
//                            getActivity().finish();
//                        },
//                        (existingDetail) -> {
//                            Toast.makeText(getContext(), existingDetail + " already exists",
//                                    Toast.LENGTH_SHORT).show();
//                        });
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
            }

        });

        binding.loginTv.setOnClickListener(view1 -> {
            Navigation.findNavController(view1).popBackStack();
        });

        return view;
    }
}