package com.example.wallety;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.wallety.databinding.FragmentChildProfileBinding;
import com.example.wallety.databinding.FragmentChildrenHomeBinding;
import com.example.wallety.databinding.FragmentHomeBinding;
import com.example.wallety.databinding.FragmentProfileBinding;
import com.example.wallety.databinding.FragmentSavingBinding;
import com.example.wallety.model.Model;
import com.example.wallety.model.User;

import java.util.Objects;

public class ProfileFragment extends Fragment {
    FragmentProfileBinding binding;
    FragmentChildProfileBinding bindingChildren;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // hide actionBar
        ActionBar actionBar = null;
        try {
            actionBar = Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar());
        } catch (NullPointerException e) {
        }

        if (actionBar != null) {
            actionBar.hide();
        }


        // get Current User
        User user = Model.instance().getCurrentUser();

        // Inflate the appropriate layout based on the user type
        if (user.isParent()) {
            binding = FragmentProfileBinding.inflate(inflater, container, false);
            view = binding.getRoot();
            initializeParentViews();
        }

        // if child
        else {
            bindingChildren = FragmentChildProfileBinding.inflate(inflater, container, false);
            view = bindingChildren.getRoot();
            initializeChildViews();
        }
        return view;
    }


    // Parent layout
    private void initializeParentViews() {
        String nameHeader = Model.instance().getCurrentUser().getName();
        binding.userNameTv.setText(nameHeader);

        String emailHeader = Model.instance().getCurrentUser().getEmail();
        binding.emailTv.setText(emailHeader);

        // editProfile button
        binding.editProfile.setOnClickListener(view1 -> {
            Navigation.findNavController(view1).navigate(R.id.action_profileFragment_to_editProfileFragment);
        });

        // myFamily button
        if (Model.instance().getCurrentUser().isParent()) {
            binding.myFamily.setOnClickListener(view1 -> {
                // screen with all the prepaid credit of the children
                Navigation.findNavController(view1).navigate(R.id.action_profileFragment_to_myFamilyFragment);
            });
        } else {
            binding.myFamily.setVisibility(View.INVISIBLE);
        }

        binding.knowledge.setOnClickListener(view1 -> {
            // screen with all the prepaid credit of the children
            Navigation.findNavController(view1).navigate(R.id.action_profileFragment_to_knowledgeFragment);
        });
    }

    // Child layout
    private void initializeChildViews() {

        bindingChildren.knowledge.setOnClickListener(view1 -> {
            // screen with all the prepaid credit of the children
            Navigation.findNavController(view1).navigate(R.id.action_profileFragment_to_knowledgeFragment);
        });
        String nameHeader = Model.instance().getCurrentUser().getName();
        bindingChildren.userNameTv.setText(nameHeader);

        String emailHeader = Model.instance().getCurrentUser().getEmail();
        bindingChildren.emailTv.setText(emailHeader);

        // editProfile button
        bindingChildren.editProfile.setOnClickListener(view1 -> {
            Navigation.findNavController(view1).navigate(R.id.action_profileFragment_to_editProfileFragment);
        });
    }

}