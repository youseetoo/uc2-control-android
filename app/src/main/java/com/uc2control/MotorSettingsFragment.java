package com.uc2control;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uc2control.databinding.FragmentMotorSettingsBinding;
import com.uc2control.viewmodels.MotorViewModel;


public class MotorSettingsFragment extends Fragment {

    private MotorViewModel viewModel;
    private FragmentMotorSettingsBinding settingsBinding;

    public void setViewModel(MotorViewModel viewModel)
    {
        this.viewModel = viewModel;
    }

    public MotorSettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        settingsBinding =  DataBindingUtil.inflate(inflater, R.layout.fragment_motor_settings, container, false);
        settingsBinding.setMotor(viewModel.getMotorModel());
        settingsBinding.stepperX.setStepper(viewModel.getMotorModel().getStepperX());
        settingsBinding.stepperY.setStepper(viewModel.getMotorModel().getStepperY());
        settingsBinding.stepperZ.setStepper(viewModel.getMotorModel().getStepperZ());
        settingsBinding.stepperA.setStepper(viewModel.getMotorModel().getStepperA());
        return settingsBinding.getRoot();
    }
}