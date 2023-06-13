package com.example.wallety;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.braintreepayments.cardform.view.CardForm;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.wallety.databinding.FragmentMyFamilyBinding;

import java.util.ArrayList;


public class MyFamilyFragment extends Fragment {

    FragmentMyFamilyBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMyFamilyBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        ImageSlider imageSlider = view.findViewById(R.id.imageSlider);
        ArrayList<SlideModel> slideModels = new ArrayList<>();

        slideModels.add(new SlideModel(R.drawable.visa_card, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.visa_card, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.visa_card, ScaleTypes.FIT));

        imageSlider.setImageList(slideModels, ScaleTypes.FIT);


        return view;
    }
}







