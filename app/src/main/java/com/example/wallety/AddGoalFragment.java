package com.example.wallety;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import com.example.wallety.databinding.FragmentAddGoalBinding;
import com.example.wallety.model.Model;
import com.example.wallety.model.Saving;
import com.example.wallety.model.User;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class AddGoalFragment extends BottomSheetDialogFragment {
    FragmentAddGoalBinding binding;

    Button saveBtn;
    EditText savingGoal, savingDetail, savingAmount;
    ProgressBar progressBar;

    DocumentReference db;
    User user;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddGoalBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        user = Model.instance().getCurrentUser();

        saveBtn = view.findViewById(R.id.goal_input_save);
        savingGoal = view.findViewById(R.id.goal_input_name);
        savingDetail = view.findViewById(R.id.goal_input_det);
        savingAmount = view.findViewById(R.id.goal_input_amount);
        progressBar =  view.findViewById(R.id.progressBar);


        // Save the new goal to DataBase
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String goal = savingGoal.getText().toString().trim();
                String detail = savingDetail.getText().toString().trim();
                String currentAmount = "0";

                if(goal.equals("") || detail.equals("") || savingAmount.getText().toString().trim().equals(""))
                {
                    Toast.makeText(getContext(), "No field can be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                int amount = Integer.parseInt(savingAmount.getText().toString().trim());

                //add To DB
                db = FirebaseFirestore.getInstance().collection("users").document(user.getId());
                String id = db.collection("saving").document().getId();

                Map<String, Object> saving = new HashMap<>();
                saving.put("id", id);
                saving.put("goal", goal);
                saving.put("detail", detail);
                saving.put("amount", amount);
                saving.put("currentAmount", 0);

                db.collection("saving") // name of the collection
                        .document(id)
                        .set(saving);

//                (Just for show, need to be completely with server)
                Saving s = new Saving(id, goal, detail, amount);
                user.addSaving(s);
//


//                SavingFragment.savingList.add(s);
//                SavingFragment.savingAdapter.notifyDataSetChanged();


                Navigation.findNavController(view).navigate(R.id.action_addGoalFragment_to_savingMoneyFragment);


            }
        });



        return view;
    }
}