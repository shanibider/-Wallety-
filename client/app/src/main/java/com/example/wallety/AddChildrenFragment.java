package com.example.wallety;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.wallety.adapters.SearchedChildAdapter;
import com.example.wallety.databinding.FragmentAddChildrenBinding;
import com.example.wallety.model.Model;
import com.example.wallety.model.User;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AddChildrenFragment extends Fragment {
    FragmentAddChildrenBinding binding;
    List<User> allChildrenWithoutParent;
    SearchedChildAdapter searchedChildAdapter;
    Map<String, CheckBox> addedCheckboxesByEmail = SignUpFragment.addedCheckboxesByEmail;
    List<String> addedChildrenIds = SignUpFragment.addedChildrenIds;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddChildrenBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.searchedChildrenRv.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.addedChildrenList.setEnabled(false);

        for (CheckBox addedChildCb : addedCheckboxesByEmail.values()) {
            ((ViewGroup) addedChildCb.getParent()).removeView(addedChildCb);
            addedChildCb.setEnabled(false);
            binding.addedChildrenList.addView(addedChildCb);
        }

        Model.instance().getChildrenWithoutParent(
                (fetchedChildrenWithoutParent) -> {
                    allChildrenWithoutParent = fetchedChildrenWithoutParent;
                    searchedChildAdapter = new SearchedChildAdapter(getContext(), allChildrenWithoutParent,
                            binding.addedChildrenList, addedCheckboxesByEmail, addedChildrenIds);
                    binding.searchedChildrenRv.setAdapter(searchedChildAdapter);
                    binding.progressBar.setVisibility(View.GONE);
                    searchedChildAdapter.notifyDataSetChanged();

                    for (CheckBox addedChildCb : addedCheckboxesByEmail.values())
                        addedChildCb.setEnabled(true);
                    binding.searchChildrenSv.setInputType(InputType.TYPE_CLASS_TEXT);
                },
                (onFailure) -> {
                    Toast.makeText(getContext(), "Error occurred", Toast.LENGTH_SHORT).show();
                });

        binding.saveChildrenBtn.setOnClickListener(view1 ->
                Navigation.findNavController(view1).popBackStack()
        );

        binding.searchChildrenSv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<User> filteredChildrenWithoutParent = allChildrenWithoutParent.stream()
                        .filter(user -> {
                            String filterText = newText.toLowerCase();
                            boolean includedInName = user.getName().toLowerCase().contains(filterText);
                            boolean includedInPhone = user.getPhone().toLowerCase().contains(filterText);
                            boolean includedInEmail = user.getEmail().toLowerCase().contains(filterText);

                            return includedInName || includedInPhone || includedInEmail;
                        })
                        .collect(Collectors.toList());
                searchedChildAdapter = new SearchedChildAdapter(getContext(), filteredChildrenWithoutParent,
                        binding.addedChildrenList, addedCheckboxesByEmail, addedChildrenIds);
                binding.searchedChildrenRv.setAdapter(searchedChildAdapter);

                return false;
            }
        });

        return view;
    }
}