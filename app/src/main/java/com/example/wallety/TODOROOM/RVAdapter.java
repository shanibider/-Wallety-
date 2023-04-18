package com.example.wallety.TODOROOM;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallety.R;
import com.example.wallety.model.Task;

import java.util.List;

public class RVAdapter extends ListAdapter<Task, RVAdapter.ViewHolder> {
    List<Task> list;


    public RVAdapter(){
    super(CALLBACK);
    }

private static final DiffUtil.ItemCallback<Task> CALLBACK = new DiffUtil.ItemCallback<Task>() {
    @Override
    public boolean areItemsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
        return oldItem.getTaskId()== newItem.getTaskId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
        return oldItem.getTaskTitle().equals(newItem.getTaskTitle())
                && oldItem.getTaskDetail().equals(newItem.getTaskDetail());
    }
};

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.task.setText(list.get(position).getTaskTitle());
        holder.description.setText(list.get(position).getTaskDetail());


    }

    public Task getTask(int position){
        return getItem(position);
    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView task, description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            task = itemView.findViewById(R.id.task_tv1);
            description = itemView.findViewById(R.id.task_tv2);


        }
    }

}