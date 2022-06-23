package com.example.colorroulette;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.colorroulette.databinding.FragmentSecondBinding;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.Callable;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private View bgView = null;
    private int currentColorIndex = -1;
    private int extraIterations = 0;
    private MediaPlayer mediaPlayerTick = null;
    private MediaPlayer mediaPlayerCircus = null;
    private MediaPlayer mediaPlayerSong = null;
    private MediaPlayer mediaPlayerBoom = null;
    private boolean isColorMode = true;
    private int playerAScore = 0;
    private int playerBScore = 0;
    private int round = 0;
    private boolean hasGivenPoints = false;

    private Handler handler = new Handler();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        mediaPlayerTick = MediaPlayer.create(getContext(), R.raw.tick);
        mediaPlayerCircus = MediaPlayer.create(getContext(), R.raw.circus);
        mediaPlayerSong = MediaPlayer.create(getContext(), R.raw.pirates);
        mediaPlayerBoom = MediaPlayer.create(getContext(), R.raw.boom);
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bgView = view;
        resetGame();
        hideResults();
        bgView.findViewById(R.id.playerAResultsText).setVisibility(View.GONE);
        bgView.findViewById(R.id.playerBResultsText).setVisibility(View.GONE);
        hasGivenPoints = false;

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
                if (!hasGivenPoints) {
                    return;
                }
                hasGivenPoints = false;

                if (round == 3) {
                    NavHostFragment.findNavController(SecondFragment.this)
                            .navigate(R.id.action_SecondFragment_to_FirstFragment);
                    return;
                }
                hideResults();
                iteration = 1;

                if (isColorMode)
                    colorModeClick();
                else {
                    bgView.setBackgroundColor(Color.BLACK);
                    darkModeClick();
                }
            }
        });
    }

    private void colorModeClick() {
        round++;
        bgView.findViewById(R.id.button_colorMode).setVisibility(View.GONE);
        bgView.findViewById(R.id.button_darkMode).setVisibility(View.GONE);
        isColorMode = true;
        iteration = 1;
        extraIterations = new Random().nextInt(20);
        handler.post(colorRunnable);
        mediaPlayerCircus.start();
    }

    private void darkModeClick() {
        bgView.setBackgroundColor(Color.BLACK);
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

    public int getRandomColor() {
        int[] colors = {
                Color.rgb(237, 43, 88), // RED
                Color.rgb(6, 214, 160), // GREEN
                Color.rgb(17, 138, 178), // BLUE
                Color.rgb(255, 209, 102) // YELLOW
        };
        int rnd = currentColorIndex;
        while (rnd == currentColorIndex)
            rnd = new Random().nextInt(colors.length);
        currentColorIndex = rnd;
        return colors[rnd];
    }

    private void playerButtonClick(char player, View view) {
        hasGivenPoints = true;
        switch (player) {
            case 'A':
                ((TextView) view.findViewById(R.id.playerAScoreText)).setText(Integer.toString(++playerAScore));
                break;
            case 'B':
                ((TextView) view.findViewById(R.id.playerBScoreText)).setText(Integer.toString(++playerBScore));
                break;
        }
        if (round == 3) {
            startTimer(new Callable<Void>() {
                public Void call() {
                    showResultsText(view);
                    return null;
                }
            }, 1000, 100);
        }
    }

    private void showResultsText(View view) {
        bgView.findViewById(R.id.playerAPlusBtn).setVisibility(View.GONE);
        bgView.findViewById(R.id.playerBPlusBtn).setVisibility(View.GONE);

        if (playerAScore > playerBScore) {
            ((TextView) view.findViewById(R.id.playerAResultsText)).setText("You won!");
            ((TextView) view.findViewById(R.id.playerBResultsText)).setText("You lost!");
        } else {
            ((TextView) view.findViewById(R.id.playerBResultsText)).setText("You won!");
            ((TextView) view.findViewById(R.id.playerAResultsText)).setText("You lost!");
        }
        bgView.findViewById(R.id.playerAResultsText).setVisibility(View.VISIBLE);
        bgView.findViewById(R.id.playerBResultsText).setVisibility(View.VISIBLE);
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
                mediaPlayerCircus.stop();
                try {
                    mediaPlayerCircus.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                startTimer(new Callable<Void>() {
                    public Void call() {
                        showResults();
                        return null;
                    }
                }, 3000, 100);
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
            } else {
                handler.post(darkBoomRunnable);
                mediaPlayerSong.stop();
                try {
                    mediaPlayerSong.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bgView.setBackgroundColor(getRandomColor());
                startTimer(new Callable<Void>() {
                    public Void call() {
                        showResults();
                        return null;
                    }
                }, 3000, 100);
            }
        }
    };

    void startTimer(Callable<Void> method, int msInFuture, int cdInterval) {
        CountDownTimer cTimer = null;
        cTimer = new CountDownTimer(msInFuture, cdInterval) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                try {
                    method.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        cTimer.start();
    }

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

    private void resetGame() {
        round = 0;
        playerAScore = 0;
        playerBScore = 0;
        ((TextView) bgView.findViewById(R.id.playerAScoreText)).setText(Integer.toString(0));
        ((TextView) bgView.findViewById(R.id.playerBScoreText)).setText(Integer.toString(0));
    }

    Runnable darkBoomRunnable = new Runnable() {
        @Override
        public void run() {
            mediaPlayerBoom.start();
        }
    };

}