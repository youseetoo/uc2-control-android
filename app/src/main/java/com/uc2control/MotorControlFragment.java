package com.uc2control;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uc2control.databinding.FragmentMotorControlBinding;
import com.uc2control.viewmodels.MotorViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MotorControlFragment extends Fragment {

    private MotorViewModel viewModel;
    private FragmentMotorControlBinding motorControlBinding;

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