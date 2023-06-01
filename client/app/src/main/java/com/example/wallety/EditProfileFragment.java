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


public class EditProfileFragment extends Fragment {
    FragmentEditProfileBinding binding;

    private static DocumentReference db;
    static User user;
    EditText editName;
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

        String userId = Model.instance().getCurrentUser().getId();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(userId);

        //retrieve the values entered by the user in EditText fields (editName) and store them
        String newName = editName.getText().toString();
        if (newName.isEmpty()) {
            // Returns false indicating that the data wasn't changed
            return false;
        } else {
            // Update User Name in Firestore
            userRef.update("name", newName)
                    .addOnSuccessListener(aVoid -> {
                        // The user's name has been updated successfully
                        // Update the user name in the app's data model
                        Model.instance().getCurrentUser().setName(newName);
                        // Update the UI element with the new user name
                        binding.EditProfileTv.setText(newName);
                    })
                    .addOnFailureListener(e -> {
                        // An error occurred while updating the user's name
                        Toast.makeText(getContext(), "Error Found", Toast.LENGTH_SHORT).show();
                    });

            return true;
        }
    }

}





