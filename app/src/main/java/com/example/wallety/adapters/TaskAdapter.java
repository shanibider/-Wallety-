package com.example.wallety.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallety.R;
import com.example.wallety.model.Saving;
import com.example.wallety.model.Task;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    Context context;
    List<Task> list;

    public TaskAdapter(Context context, List<Task> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TaskAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.task_row, parent, false ));
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.ViewHolder holder, int position) {
        holder.task.setText(list.get(position).getTaskTitle());
        holder.description.setText(list.get(position).getTaskDetail());
    }

    @Override
    public int getItemCount() {
        return  list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView task, description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            task = itemView.findViewById(R.id.task_tv1);
            description = itemView.findViewById(R.id.task_tv2);

        }
    }
}

