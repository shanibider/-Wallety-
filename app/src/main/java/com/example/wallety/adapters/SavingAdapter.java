package com.example.wallety.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallety.R;
import com.example.wallety.SavingFragment;
import com.example.wallety.TasksFragment;
import com.example.wallety.model.Model;
import com.example.wallety.model.Saving;
import com.example.wallety.model.Task;
import com.example.wallety.model.User;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class SavingAdapter extends RecyclerView.Adapter<SavingAdapter.ViewHolder> {
    Context context;
    List<Saving> savingList;
    User user;
    DocumentReference db;
    private SavingFragment savingFragment;

    public SavingAdapter(Context context, List<Saving> savingList) {
        this.context = context;
        this.savingList = savingList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.saving_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        user = Model.instance().getCurrentUser();

        ImageButton savingDelete = holder.savingDelete;
        ImageButton savingEdit = holder.savingEdit;
        Saving saving = savingList.get(position);

        holder.goal.setText(saving.getGoal());
        holder.detail.setText(saving.getDetails());
        holder.amount.setText(String.valueOf(saving.getAmount()));
        holder.currentAmount.setText(String.valueOf(saving.getCurrentAmount()));

        //progressBar max
        holder.progressBar.setMax(saving.getAmount());

        //*progressBar progress - not working*
        int current = saving.getCurrentAmount();
        holder.progressBar.setProgress(current);

        //progressBar text
        holder.progressText2.setText(String.valueOf(saving.getAmount()));


        // taskEdit button
        savingEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Saving saving = savingList.get(position);

                String id = saving.getId();
                showAlertDialogForEditText(id, position);
            }
        });


        // taskDelete button
        savingDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Saving saving = savingList.get(position);

                String id = saving.getId();
                // delete Item From DB
                db = FirebaseFirestore.getInstance().collection("users").document(user.getId());
                db.collection("saving")
                        .document(id)
                        .delete();
                savingList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, savingList.size());
                notifyDataSetChanged();
            }
        });

    }


    //   *edit goal Alert dialog*
    private void showAlertDialogForEditText(String id, int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View customLayout = LayoutInflater.from(context).inflate(R.layout.edit_saving_alert_dialog, null, false);
        builder.setView(customLayout);

        TextInputEditText name, det, amount;
        AppCompatButton ok, cancel;

        // initialize objects with corresponding elements
        name = customLayout.findViewById(R.id.saving_edit_name);
        det = customLayout.findViewById(R.id.saving_edit_det);
        amount = customLayout.findViewById(R.id.saving_edit_amount);

        ok = customLayout.findViewById(R.id.task_edit_ok);
        cancel = customLayout.findViewById(R.id.task_edit_cancel);

        // set the text of the EditTexts to the corresponding values of the savingList object at the given position
        name.setText(savingList.get(position).getGoal());
        det.setText(savingList.get(position).getDetails());
        amount.setText(savingList.get(position).getAmount());

        // creates AlertDialog object
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // retrieves the values
                String n = name.getText().toString().trim();
                String d = det.getText().toString().trim();
                String am = amount.getText().toString().trim();

                if (name.equals("") || det.equals("") || amount.equals("")) {
                    Toast.makeText(context, "No field can be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                // values are used to update the corresponding object in savingList
                else {
                    savingList.get(position).setGoal(n);
                    savingList.get(position).setDetails(d);
                    savingList.get(position).setAmount(Integer.parseInt(am));

                    //update DB with the new values
                    db = FirebaseFirestore.getInstance().collection("users").document(user.getId());
                    Map<String, Object> saving = new HashMap<>();
                    saving.put("id", id);
                    saving.put("goal", n);
                    saving.put("detail", d);
                    saving.put("amount", am);

                    db.collection("saving") // name of the collection
                            .document(id) // task id
                            .update(saving);


                    notifyItemChanged(position);
                    notifyDataSetChanged();
                    dialog.dismiss();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
//    //// *end of edit Alertdialog*


    @Override
    public int getItemCount() {
        if (savingList == null) {
            return 0;
        }
        int size = savingList.size();
        SavingFragment.goalCountTv.setText((String.valueOf(size)));
        return size;

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView goal, detail, amount, currentAmount, progressText2;
        ProgressBar progressBar;
        ImageButton savingDelete, savingEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            goal = itemView.findViewById(R.id.tv2);
            detail = itemView.findViewById(R.id.tv3);
            amount = itemView.findViewById(R.id.tv4);
            progressBar = itemView.findViewById(R.id.progressBar);
            currentAmount = itemView.findViewById(R.id.saving_tv5);
            progressText2 = itemView.findViewById(R.id.saving_tv6);

            savingDelete = itemView.findViewById(R.id.savingDeleteBtn);
            savingEdit = itemView.findViewById(R.id.savingEditBtn);


        }
    }
}
