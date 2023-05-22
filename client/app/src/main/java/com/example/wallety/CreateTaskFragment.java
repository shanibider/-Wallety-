package com.example.wallety;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.wallety.databinding.FragmentCreateTaskBinding;
import com.example.wallety.model.Model;
import com.example.wallety.model.Task;
import com.example.wallety.model.User;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class CreateTaskFragment extends BottomSheetDialogFragment {
    FragmentCreateTaskBinding binding;

    public static final String TAG = "ActionBottomDialog";

    private TextInputEditText taskName, taskDesc, taskDate, taskTime, taskAmount;
    private AppCompatButton saveBtn;
    DocumentReference db;
    User user;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.fragment_create_task, null);
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreateTaskBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        user = Model.instance().getCurrentUser();

        saveBtn = view.findViewById(R.id.task_input_save);
        taskName = view.findViewById(R.id.task_input_name);
        taskDesc = view.findViewById(R.id.task_input_desc);
        taskDate = view.findViewById(R.id.task_input_date);
        taskTime = view.findViewById(R.id.task_input_time);
        taskAmount = view.findViewById(R.id.task_input_amount);

        //arrays to store the user-selected time and date
        final String[] utime = new String[1];
        final String[] udate = new String[1];

        //initializes today var with the current UTC time
        final long today = MaterialDatePicker.todayInUtcMilliseconds();

        // MaterialDatePicker is initialized using the MaterialDatePicker.Builder
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("SELECT A DATE");
        materialDateBuilder.setSelection(today);
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();


        // Date Picker
        taskDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //The materialDatePicker is shown using the show() method
                materialDatePicker.show(getActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
            }
        });
        // When user selects a date, this method called on the materialDatePicker object
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                calendar.setTimeInMillis((long)selection);
                //the selected date is formatted into a string in the format "dd.MM.yyyy" using a SimpleDateFormat
                SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                String formattedDate  = format.format(calendar.getTime());

                // The formatted date stored in the udate array and displayed in the taskDate using setText()
                udate[0] = formattedDate;
                taskDate.setText(udate[0]);
            }
        });


        // Time Picker
        // current time is set as the initial selection, and the dialog is shown using the show() method.
        // When the user selects a time, it is stored as a formatted string in the utime array and displayed in the taskTime using setText()
        taskTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if (selectedHour==0)
                            selectedHour = 24;
                        utime[0] = String.format("%02d:%02d", selectedHour, selectedMinute);
                        taskTime.setText(utime[0]);
                    }
                }, hour, minute, true);
                // mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });


//          MY time picker (duration time)
//        taskTime.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int durationInMinutes = 0;
//                if (taskTime.getText().toString().length() > 0) {
//                    durationInMinutes = Integer.parseInt(taskTime.getText().toString());
//                }
//
//                int hours = durationInMinutes / 60;
//                int minutes = durationInMinutes % 60;
//
//                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                        int durationInMinutes = hourOfDay * 60 + minute;
//                        utime[0] = String.valueOf(durationInMinutes);
//                        taskTime.setText(utime[0]);
//                    }
//                }, hours, minutes, true);
//
//                timePickerDialog.show();
//            }
//        });




        // Save the new task to DataBase
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = taskName.getText().toString().trim();
                String desc = taskDesc.getText().toString().trim();
                String time = taskTime.getText().toString().trim();
                String date = taskDate.getText().toString().trim();
                String amount = taskAmount.getText().toString().trim();

                if(name.equals("") || desc.equals("") || time.equals("") || date.equals(""))
                {
                    Toast.makeText(getContext(), "No field can be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                //add To DB
                db = FirebaseFirestore.getInstance().collection("users").document(user.getId());
                String id = db.collection("tasks").document().getId();

                Map<String, Object> task = new HashMap<>();
                task.put("id", id);
                task.put("name", name);
                task.put("desc", desc);
                task.put("date", date);
                task.put("desc", desc);
                task.put("time", time);
                task.put("amount", amount);


                db.collection("tasks") // name of the collection
                        .document(id)
                        .set(task);

                Task t = new Task(id, name, desc, date, time, amount);
                TasksFragment.taskList.add(t);
                TasksFragment.taskAdapter.notifyDataSetChanged();

                dismiss();

            }
        });
        return view;
    }
}