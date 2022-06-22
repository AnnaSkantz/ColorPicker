package com.example.colorroulette;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.colorroulette.databinding.FragmentSecondBinding;

import java.io.IOException;
import java.util.Random;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private View bgView = null;
    private int currentColorIndex = 0;
    private int extraIterations = 0;
    private MediaPlayer mediaPlayerTick = null;
    private MediaPlayer mediaPlayerSong = null;
    private MediaPlayer mediaPlayerBoom = null;
    private boolean isColorMode = true;
    private int playerAScore = 0;
    private int playerBScore = 0;
    private int round = 0;

    private Handler handler = new Handler();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        mediaPlayerTick = MediaPlayer.create(getContext(), R.raw.tick);
        mediaPlayerSong = MediaPlayer.create(getContext(), R.raw.pirates);
        mediaPlayerBoom = MediaPlayer.create(getContext(), R.raw.boom);
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bgView = view;
        hideResults();

        binding.buttonColorMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View buttonView) {
                colorModeClick();
            }
        });

        binding.buttonDarkMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View buttonView) {
                darkModeClick();
            }
        });

        binding.playerAPlusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View buttonView) {
                playerButtonClick('A', bgView);
            }
        });

        binding.playerBPlusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View buttonView) {
                playerButtonClick('B', bgView);
            }
        });

        binding.goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View buttonView) {
                hideResults();
                bgView.setBackgroundColor(Color.BLACK);
                iteration = 1;
                if (isColorMode)
                    colorModeClick();
                else
                    darkModeClick();
            }
        });
    }

    private void colorModeClick(){
        round++;
        bgView.findViewById(R.id.button_colorMode).setVisibility(View.GONE);
        bgView.findViewById(R.id.button_darkMode).setVisibility(View.GONE);
        isColorMode = true;
        iteration = 1;
        extraIterations = new Random().nextInt(20);
        handler.post(colorRunnable);
    }

    private void darkModeClick(){
        round++;
        bgView.findViewById(R.id.button_colorMode).setVisibility(View.GONE);
        bgView.findViewById(R.id.button_darkMode).setVisibility(View.GONE);
        isColorMode = false;
        iteration = 1;
        extraIterations = new Random().nextInt(20);
        handler.post(darkRunnable);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public int getRandomColor(){
        int[] colors = {
                Color.RED,
                Color.GREEN,
                Color.BLUE,
                Color.GRAY,
                Color.MAGENTA,
        };
        int rnd = currentColorIndex;
        while (rnd == currentColorIndex)
            rnd = new Random().nextInt(colors.length);
        return colors[rnd];
    }

    private void playerButtonClick(char player, View view) {
        switch(player){
            case 'A':
                ((TextView) view.findViewById(R.id.playerAScoreText)).setText(Integer.toString(++playerBScore));
                break;
            case 'B':
                ((TextView) view.findViewById(R.id.playerBScoreText)).setText(Integer.toString(++playerBScore));
                break;
        }
        if (round == 3)
            if (playerAScore > playerBScore) {
                ((TextView) view.findViewById(R.id.playerAScoreText)).setText("You won!");
                ((TextView) view.findViewById(R.id.playerBScoreText)).setText("You lost!");
            } else {
                ((TextView) view.findViewById(R.id.playerBScoreText)).setText("You won!");
                ((TextView) view.findViewById(R.id.playerAScoreText)).setText("You lost!");
            }
    }

    private int iteration = 1;

    Runnable colorRunnable = new Runnable() {
        @Override
        public void run() {
            bgView.setBackgroundColor(getRandomColor());
            mediaPlayerTick.start();

            int delay = Math.min(1000, 5 * iteration);
            if (iteration < 50 + extraIterations) {
                if (iteration < 30)
                    delay = 200;
                handler.postDelayed(this, delay);
            } else {
                showResults();
            }
            iteration++;
        }
    };

    Runnable darkRunnable = new Runnable() {
        @Override
        public void run() {
            iteration++;
            int delay = 10000 + extraIterations * 500;
            if (iteration <= 2) {
                mediaPlayerSong.start();
                handler.postDelayed(this, delay);
            }
            else {
                mediaPlayerBoom.start();
                mediaPlayerSong.stop();
                try {
                    mediaPlayerSong.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bgView.setBackgroundColor(getRandomColor());
                showResults();
            }
        }
    };

    //TODO: Should have delay
    private void showResults() {
        bgView.findViewById(R.id.playerAScoreText).setVisibility(View.VISIBLE);
        bgView.findViewById(R.id.playerBScoreText).setVisibility(View.VISIBLE);
        bgView.findViewById(R.id.playerAPlusBtn).setVisibility(View.VISIBLE);
        bgView.findViewById(R.id.playerBPlusBtn).setVisibility(View.VISIBLE);
        bgView.findViewById(R.id.goButton).setVisibility(View.VISIBLE);
    }

    private void hideResults() {
        bgView.findViewById(R.id.playerAScoreText).setVisibility(View.GONE);
        bgView.findViewById(R.id.playerBScoreText).setVisibility(View.GONE);
        bgView.findViewById(R.id.playerAPlusBtn).setVisibility(View.GONE);
        bgView.findViewById(R.id.playerBPlusBtn).setVisibility(View.GONE);
        bgView.findViewById(R.id.goButton).setVisibility(View.GONE);
    }

}