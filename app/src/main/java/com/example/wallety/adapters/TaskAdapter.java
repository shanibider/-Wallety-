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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallety.R;
import com.example.wallety.TasksFragment;
import com.example.wallety.model.Model;
import com.example.wallety.model.Task;
import com.example.wallety.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    Context context;
    List<Task> taskList;
    User user;
    DocumentReference db;
    private TasksFragment tasksFragment;
    private CheckBox checkbox;

    public TaskAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TaskAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.task_row, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        user = Model.instance().getCurrentUser();
        CheckBox taskCheckBox = holder.taskCheckBox;

        ImageButton taskDelete = holder.taskDelete;
        ImageButton taskEdit = holder.taskEdit;

        holder.taskCheckBox.setText(taskList.get(position).getName());
        holder.taskDesc.setText(taskList.get(position).getDesc());
        holder.taskDate.setText(taskList.get(position).getDate());
        holder.taskTime.setText(taskList.get(position).getTime());
        holder.taskAmount.setText(taskList.get(position).getAmount());
        holder.taskTargetChild.setText(taskList.get(position).getTargetChild());


//        //whether checkbox check/uncheck
//        taskCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//                // When a task is completed-
//
//                Integer balance = Model.instance().getCurrentUser().getBalance();
//
//                Task task = (Task) compoundButton.getTag();
//                task.setChecked(isChecked);
//                int amount = Integer.parseInt(task.getAmount());
//                user = Model.instance().getCurrentUser();
//                db = FirebaseFirestore.getInstance().collection("users").document(user.getId());
//
//                // Update the balance field in the Firebase database
//                db.update("balance", FieldValue.increment(isChecked ? -amount : amount))
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                // Success! The balance field is updated.
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                // Handle the failure to update the balance field.
//                            }
//                        });
//            }
//        });


//                // retrieve goals from db for spinner dropdown
//                user = Model.instance().getCurrentUser();
//                db = FirebaseFirestore.getInstance().collection("users").document(user.getId());
//
//                List<String> taskList = new ArrayList<>();
//
//                    if(taskList!=null)
//                        taskList.clear();
//                    db.collection("tasks")
//                            .get()
//                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                @Override
//                                public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
//                                    if (task.isSuccessful()) {
//                                        for (QueryDocumentSnapshot document : task.getResult()) {
//
//                                            String amount = (String) document .get("amount");
//
//                                            taskList.add(amount);
//                                        }
//                                    }
//                                }
//                            });
//
//
//
//            }
//        });


        // taskEdit button
        taskEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Task taskModel = taskList.get(position);

                String id = taskModel.getId();
                showAlertDialogForEditText(id, position);
            }
        });


        // taskDelete button
        taskDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Task taskModel = taskList.get(position);

                String id = taskModel.getId();
                // delete Item From DB
                db = FirebaseFirestore.getInstance().collection("users").document(user.getId());
                db.collection("tasks")
                        .document(id)
                        .delete();
                taskList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, taskList.size());
                notifyDataSetChanged();
            }
        });

    }




    // *edit task Alert dialog*
    private void showAlertDialogForEditText(String id, int position) {
        //creates a new AlertDialog.Builder instance, which is used to create an AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View customLayout = LayoutInflater.from(context).inflate(R.layout.edit_task_alert_dialog, null, false);
        //sets the custom layout view as the view for the AlertDialog
        builder.setView(customLayout);

        TextInputEditText name, desc, date, time, amount, targetChild;
        AppCompatButton ok, cancel;

        name = customLayout.findViewById(R.id.task_edit_name);
        desc = customLayout.findViewById(R.id.task_edit_desc);
        date = customLayout.findViewById(R.id.task_edit_date);
        time = customLayout.findViewById(R.id.task_edit_time);
        amount = customLayout.findViewById(R.id.task_edit_amount);
        targetChild = customLayout.findViewById(R.id.task_edit_targetChild);

        ok = customLayout.findViewById(R.id.task_edit_ok);
        cancel = customLayout.findViewById(R.id.task_edit_cancel);

        name.setText(taskList.get(position).getName());
        desc.setText(taskList.get(position).getDesc());
        date.setText(taskList.get(position).getDate());
        time.setText(taskList.get(position).getTime());
        amount.setText(taskList.get(position).getAmount());
        targetChild.setText(taskList.get(position).getTargetChild());



        // date click handler
        final String[] utime = new String[1];
        final String[] udate = new String[1];

        final long today = MaterialDatePicker.todayInUtcMilliseconds();
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("SELECT A DATE");
        materialDateBuilder.setSelection(today);
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                calendar.setTimeInMillis((long) selection);
                SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                String formattedDate = format.format(calendar.getTime());

                udate[0] = formattedDate;
                date.setText(udate[0]);
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(((FragmentActivity) context).getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
            }
        });


        //time click handler
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if (selectedHour == 0)
                            selectedHour = 24;
                        utime[0] = String.format("%02d:%02d", selectedHour, selectedMinute);
                        time.setText(utime[0]);
                    }
                }, hour, minute, true);
                // mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



        // ok button handler
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String t = time.getText().toString().trim();
                String n = name.getText().toString().trim();
                String des = desc.getText().toString().trim();
                String dt = date.getText().toString().trim();
                String am = amount.getText().toString().trim();
                String tc = targetChild.getText().toString().trim();

                if (name.equals("") || desc.equals("") || time.equals("") || date.equals("")) {
                    Toast.makeText(context, "No field can be empty!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    taskList.get(position).setName(n);
                    taskList.get(position).setDesc(des);
                    taskList.get(position).setTime(t);
                    taskList.get(position).setDate(dt);
                    taskList.get(position).setAmount(am);
                    taskList.get(position).setTargetChild(tc);

                    //update DB
                    db = FirebaseFirestore.getInstance().collection("users").document(user.getId());
                    Map<String, Object> task = new HashMap<>();
                    task.put("id", id);
                    task.put("name", n);
                    task.put("desc", des);
                    task.put("date", dt);
                    task.put("time", t);
                    task.put("amount", am);
                    task.put("target child", tc);


                    db.collection("tasks") // name of the collection
                            .document(id) // task id
                            .update(task);

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
    //// *end of edit Alertdialog*






    @Override
    public int getItemCount() {
        int size = taskList.size();
        TasksFragment.taskCount_tv.setText((String.valueOf(size)));
        return size;
    }


    // ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox taskCheckBox;
        TextView taskDesc, taskDate, taskTime, taskAmount, taskTargetChild;
        ImageButton taskDelete, taskEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            taskCheckBox = itemView.findViewById(R.id.taskCheckBox);
            taskDesc = itemView.findViewById(R.id.taskDesc_tv);
            taskDate = itemView.findViewById(R.id.taskDate_tv);
            taskTime = itemView.findViewById(R.id.taskTime_tv);

            taskDelete = itemView.findViewById(R.id.taskDeleteBtn);
            taskEdit = itemView.findViewById(R.id.taskEditBtn);

            taskAmount = itemView.findViewById(R.id.amount_tv);
            taskTargetChild = itemView.findViewById(R.id.targetChildTv);
        }
    }
}