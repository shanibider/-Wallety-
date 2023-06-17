package com.example.wallety.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallety.R;
import com.example.wallety.model.User;

import java.util.List;
import java.util.Map;

public class SearchedChildAdapter extends RecyclerView.Adapter<SearchedChildAdapter.ViewHolder> {
    Context context;
    List<User> childrenList;
    LinearLayout addedChildrenListView;
    Map<String, CheckBox> addedCheckboxesByEmail;
    List<User> addedChildren;

    public SearchedChildAdapter(Context context, List<User> childrenList, LinearLayout addedChildrenListView,
                                Map<String, CheckBox> addedCheckboxesByEmail, List<User> addedChildrenIds) {
        this.context = context;
        this.childrenList = childrenList;
        this.addedChildrenListView = addedChildrenListView;
        this.addedCheckboxesByEmail = addedCheckboxesByEmail;
        this.addedChildren = addedChildrenIds;
    }

    private void createAddedChildCheckboxHandler(CheckBox searchedChildCheckBox, User child) {
        addedChildrenListView.removeView(addedCheckboxesByEmail.get(child.getEmail()));
        addedCheckboxesByEmail.remove(child.getEmail());
        addedChildren.removeIf(user -> user.getEmail().equals(child.getEmail()));
        searchedChildCheckBox.setChecked(false);
    }

    @NonNull
    @Override
    public SearchedChildAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchedChildAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.searched_child_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchedChildAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        CheckBox searchedChildCheckBox = holder.childCb;
        User child = childrenList.get(position);
        String name = child.getName();
        String email = child.getEmail();
        searchedChildCheckBox.setText(name);
        searchedChildCheckBox.setChecked(addedCheckboxesByEmail.containsKey(email));

        if (addedCheckboxesByEmail.containsKey(email)) {
            addedCheckboxesByEmail.get(email).setOnClickListener(v1 -> {
                createAddedChildCheckboxHandler(searchedChildCheckBox, child);
            });
        }
        holder.phoneNumberTv.setText(child.getPhone());
        holder.emailTv.setText(email);

        searchedChildCheckBox.setOnClickListener(v -> {
            if (((CheckBox) v).isChecked()) {
                CheckBox addedChildCheckBox = new CheckBox(context);
                addedChildCheckBox.setText(name);
                addedChildCheckBox.setChecked(true);
                addedChildCheckBox.setButtonTintList(ColorStateList.valueOf(context.getColor(R.color.teal_500)));
                addedCheckboxesByEmail.put(email, addedChildCheckBox);
                addedChildren.add(child);
                addedChildrenListView.addView(addedChildCheckBox);

                addedChildCheckBox.setOnClickListener(v1 -> {
                    createAddedChildCheckboxHandler(searchedChildCheckBox, child);
                });
            } else {
                CheckBox childToDeleteCheckbox = addedCheckboxesByEmail.get(email);
                createAddedChildCheckboxHandler(childToDeleteCheckbox, child);

            }
        });
    }

    @Override
    public int getItemCount() {
        int size = childrenList.size();
        return size;
    }


    // ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox childCb;
        TextView phoneNumberTv, emailTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            childCb = itemView.findViewById(R.id.childCb);
            phoneNumberTv = itemView.findViewById(R.id.phoneNumberTv);
            emailTv = itemView.findViewById(R.id.emailTv);
        }
    }
}