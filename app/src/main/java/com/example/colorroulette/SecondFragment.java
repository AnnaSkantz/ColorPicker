package com.example.colorroulette;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.colorroulette.databinding.FragmentSecondBinding;

import java.util.Random;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View buttonView) {
                view.setBackgroundColor(getRandomColor());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public int getRandomColor(){
        int[] colors = {
                Color.rgb(255, 162, 55),
                Color.rgb(91, 3, 182),
                Color.rgb(0, 100, 200),
                Color.rgb(17, 74, 191),
                Color.rgb(182, 85, 1),
        };
        int rnd = new Random().nextInt(colors.length);
        return colors[rnd];
    }

}