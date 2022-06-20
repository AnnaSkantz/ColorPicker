package com.example.colorroulette;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import top.defaults.colorpicker.ColorPickerPopup;

import com.example.colorroulette.databinding.FragmentColorPickerBinding;
import com.example.colorroulette.databinding.FragmentFirstBinding;

public class ColorPickerFragment extends Fragment {

    private FragmentColorPickerBinding binding;

    // text view variable to set the color for GFG text
    private TextView gfgTextView;

    // two buttons to open color picker dialog and one to
    // set the color for GFG text
    private Button mSetColorButton, mPickColorButton;

    // view box to preview the selected color
    private View mColorPreview;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentColorPickerBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // register the GFG text with appropriate ID
        //gfgTextView = getActivity().findViewById(R.id.gfg_heading);

        // register two of the buttons with their
        // appropriate IDs
        mPickColorButton = getActivity().findViewById(R.id.pick_color_button);
        mSetColorButton = getActivity().findViewById(R.id.set_color_button);

        // and also register the view which shows the
        // preview of the color chosen by the user
        mColorPreview = getActivity().findViewById(R.id.preview_selected_color);
        Log.d("success","outside onClick");
        mPickColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d("success","inside onClick");
                new ColorPickerPopup.Builder(getContext())
                        .initialColor(Color.RED)
                        .enableBrightness(true) // enable color brightness slider or not
                        .enableAlpha(true)      // enable color alpha changer on slider
                        .okTitle("Choose") // this is top right 'Choose' button
                        .cancelTitle("Cancel") // this is top left 'Cancel' button which closes the
                        .showIndicator(true) // small box which shows the chosen color at bottom of cancel button
                        .showValue(true) // shows the selected color hex code
                        .build()
                        .show(v, new ColorPickerPopup.ColorPickerObserver() {
                            @Override
                            public void onColorPicked(int color) {
                                mColorPreview.setBackgroundColor(color);
                                v.setBackgroundColor(color);
                            }
                        });
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}