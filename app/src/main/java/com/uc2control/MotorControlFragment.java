package com.uc2control;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uc2control.databinding.FragmentMotorControlBinding;
import com.uc2control.viewmodels.MainViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MotorControlFragment extends Fragment {

    private FragmentMotorControlBinding motorControlBinding;
    private MainViewModel viewModel;

    public MotorControlFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        motorControlBinding =  DataBindingUtil.inflate(inflater, R.layout.fragment_motor_control, container, false);
        motorControlBinding.setMotor(viewModel.getMotorModel());
        motorControlBinding.stepperAControl.setStepper(viewModel.getMotorModel().getStepperA());
        motorControlBinding.stepperXControl.setStepper(viewModel.getMotorModel().getStepperX());
        motorControlBinding.stepperYControl.setStepper(viewModel.getMotorModel().getStepperY());
        motorControlBinding.stepperZControl.setStepper(viewModel.getMotorModel().getStepperZ());
        // Inflate the layout for this fragment
        return motorControlBinding.getRoot();
    }
}