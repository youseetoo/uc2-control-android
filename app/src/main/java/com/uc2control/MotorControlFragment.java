package com.uc2control;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uc2control.viewmodels.MotorViewModel;


public class MotorControlFragment extends Fragment {

    private MotorViewModel viewModel;

    public MotorControlFragment() {
        // Required empty public constructor
    }

    public void setViewModel(MotorViewModel viewModel)
    {
        this.viewModel = viewModel;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_motor_control, container, false);
    }
}