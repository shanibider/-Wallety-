package com.example.wallety;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wallety.databinding.FragmentEditProfileBinding;
import com.example.wallety.model.FirebaseModel;
import com.example.wallety.model.Model;
import com.example.wallety.model.User;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class EditProfileFragment extends Fragment {
    FragmentEditProfileBinding binding;

    private static DocumentReference db;
    static User user;
    EditText editName, editEmail;
    Button saveButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        user = Model.instance().getCurrentUser();
        db = FirebaseFirestore.getInstance().collection("users").document(user.getId());

        editName = view.findViewById(R.id.editName);
        editEmail = view.findViewById(R.id.editEmail);

        saveButton = view.findViewById(R.id.saveButton);

        // save Button handler
        binding.saveButton.setOnClickListener(view1 -> {
            if (isDataChanged()) {
                Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "No Changes Found", Toast.LENGTH_SHORT).show();
            }
            Navigation.findNavController(view1).navigate(R.id.action_editProfileFragment_to_profileFragment);
        });

        return view;

    }


    // check if user data changed
    private boolean isDataChanged() {


        // update user info
        String userId = Model.instance().getCurrentUser().getId();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(userId);

        //retrieve the values entered by the user in EditText fields (editName) and store them
        String newName = editName.getText().toString().trim();
        String newEmail = editEmail.getText().toString().trim();

        if (newName.isEmpty() && newEmail.isEmpty()) {
            return false; // Returns false indicating that the data wasn't changed
        } else {
            Map<String, Object> updates = new HashMap<>();
            if (!newName.isEmpty()) {
                updates.put("name", newName);
            }

            if (!newEmail.isEmpty()) {
                updates.put("email", newEmail);
            }

            userRef.update(updates)
                    .addOnSuccessListener(aVoid -> {
                        User currentUser = Model.instance().getCurrentUser();
                        if (!newName.isEmpty()) {
                            currentUser.setName(newName);
                            binding.EditProfileTv.setText(newName);
                        }
                        if (!newEmail.isEmpty()) {
                            currentUser.setEmail(newEmail);
                            // Perform any necessary UI updates for email
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Error updating user information", Toast.LENGTH_SHORT).show();
                    });

            return true;
        }
    }
}