package com.example.wallety.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallety.R;
import com.example.wallety.model.Saving;
import com.example.wallety.model.Transaction;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    Context context;
    List<Transaction> list;

    public HomeAdapter(Context context, List<Transaction> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView.setImageResource(R.drawable.shopping_bag);
        holder.receiver.setText(list.get(position).getReceiver());
        holder.date.setText(list.get(position).getDate());
        holder.amount.setText(Integer.toString(list.get(position).getAmount()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //?
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView receiver, date, amount;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            receiver = itemView.findViewById(R.id.receiver);
            date = itemView.findViewById(R.id.date);
            amount = itemView.findViewById(R.id.amount);
            imageView = itemView.findViewById(R.id.iv);

        }
    }
}
//        this.id = id;
//        this.date = date;
//        this.amount = amount;
//        this.receiver = receiver;
//        this.isUnusual = isUnusual;