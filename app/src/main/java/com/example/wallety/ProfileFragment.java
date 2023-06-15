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

import com.example.wallety.databinding.FragmentProfileBinding;
import com.example.wallety.databinding.FragmentSavingBinding;
import com.example.wallety.model.Model;

import java.util.Objects;

public class ProfileFragment extends Fragment {
    FragmentProfileBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // hide actionBar
        ActionBar actionBar = null;
        try {
            actionBar = Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar());
        } catch (NullPointerException e) {}

        if (actionBar != null) {
            actionBar.hide();
        }

        String nameHeader = Model.instance().getCurrentUser().getName();
        binding.userNameTv.setText(nameHeader);

        String emailHeader = Model.instance().getCurrentUser().getEmail();
        binding.emailTv.setText(emailHeader);

        // editProfile button
        binding.editProfile.setOnClickListener(view1 -> {
            Navigation.findNavController(view1).navigate(R.id.action_profileFragment_to_editProfileFragment);
        });

        // myFamily button
        binding.myFamily.setOnClickListener(view1 -> {
            // screen with all the prepaid credit of the children
            Navigation.findNavController(view1).navigate(R.id.action_profileFragment_to_myFamilyFragment);
        });

        binding.knowledge.setOnClickListener(view1 -> {
            // screen with all the prepaid credit of the children
            Navigation.findNavController(view1).navigate(R.id.action_profileFragment_to_knowledgeFragment);
        });


        return view;
    }

}




















//**
//        * A simple {@link Fragment} subclass.
//        * Use the {@link ProfileFragment#newInstance} factory method to
//        * create an instance of this fragment.
//        */
//public class ProfileFragment extends Fragment {
//
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    public ProfileFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment ProfileFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static ProfileFragment newInstance(String param1, String param2) {
//        ProfileFragment fragment = new ProfileFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_profile, container, false);
//    }