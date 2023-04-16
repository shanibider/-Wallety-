package com.example.wallety.adapters;

import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallety.R;
import com.example.wallety.SavingMoneyFragment;
import com.example.wallety.model.Saving;
import com.example.wallety.model.Transactions;

import java.util.List;

public class SavingAdapter extends RecyclerView.Adapter<SavingAdapter.ViewHolder> {

    Context context;
    List<Saving> list;

    public SavingAdapter(Context context, List<Saving> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.saving_row, parent, false ));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.goal.setText(list.get(position).getGoal());
        holder.detail.setText(list.get(position).getDetail());
        holder.sum.setText(list.get(position).getSum());
        holder.progressBar.setProgress(Integer.parseInt(list.get(position).getProgressBar()));
        holder.progressBar.setMax(1200);    //need to change according to saving amount for each goal the user enter

        // Update the progress bar's value
        String progressStr  =list.get(position).getProgressBar();
        int progressInt = Integer.parseInt(progressStr);
        holder.progressText.setText(String.format("%d out of %d", progressInt, 1200));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //?
            }
        });

    }

    @Override
    public int getItemCount() {
        return  list.size();
    }



public class ViewHolder extends RecyclerView.ViewHolder{
        TextView goal, detail, sum, progressText;
        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            goal = itemView.findViewById(R.id.tv2);
            detail = itemView.findViewById(R.id.tv3);
            sum = itemView.findViewById(R.id.tv4);
            progressBar = itemView.findViewById(R.id.progressBar);
            progressText = itemView.findViewById(R.id.tv5);


        }
    }
}
