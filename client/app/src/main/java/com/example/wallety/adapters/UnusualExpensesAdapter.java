package com.example.wallety.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallety.R;
import com.example.wallety.UnusualExpensesFragment;
import com.example.wallety.model.Model;
import com.example.wallety.model.Transaction;
import com.example.wallety.model.User;
import java.util.List;

public class UnusualExpensesAdapter extends RecyclerView.Adapter<UnusualExpensesAdapter.ViewHolder> {
    Context context;
    List<Transaction> unusualExpensesList;
    User user;

    public UnusualExpensesAdapter(Context context, List<Transaction> unusualExpensesList) {
        this.context = context;
        this.unusualExpensesList = unusualExpensesList;
    }

    @NonNull
    @Override
    public UnusualExpensesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UnusualExpensesAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.unusual_expense_row, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull UnusualExpensesAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        user = Model.instance().getCurrentUser();
        Transaction currentUnusualExpense = unusualExpensesList.get(position);

        holder.amount.setText(Integer.toString(currentUnusualExpense.getAmount()));
        holder.title.setText(currentUnusualExpense.getName());
        holder.childName.setText(currentUnusualExpense.getChild());
        holder.date.setText(currentUnusualExpense.getDate());
    }

    @Override
    public int getItemCount() {
        int size = unusualExpensesList.size();
        UnusualExpensesFragment.unusualExpensesAmountTv.setText((String.valueOf(size)));
        return size;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, childName, date, amount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.amountTv);
            title = itemView.findViewById(R.id.titleTv);
            childName = itemView.findViewById(R.id.childNameTv);
            date = itemView.findViewById(R.id.dateTv);
        }
    }
}
