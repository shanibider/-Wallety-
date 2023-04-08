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
import com.example.wallety.model.Transactions;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    Context context;
    List<Transactions> list;

    public HomeAdapter(Context context, List<Transactions> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_row, parent, false ));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView.setImageResource(list.get(position).getImage());
        holder.source.setText(list.get(position).getSource());
        holder.date.setText(list.get(position).getDate());
        holder.sum.setText(list.get(position).getSum());

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
        TextView source, date, sum;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.iv);
            source = itemView.findViewById(R.id.textView11);
            date = itemView.findViewById(R.id.textView12);
            sum = itemView.findViewById(R.id.textView13);



        }
    }
}
