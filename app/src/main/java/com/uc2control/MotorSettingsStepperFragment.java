package com.uc2control;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uc2control.databinding.FragmentMotorSettingsStepperBinding;
import com.uc2control.models.Stepper;


public class MotorSettingsStepperFragment extends Fragment {

    private Stepper stepper;
    private FragmentMotorSettingsStepperBinding steppersettingsViewBinding;

    public void setStepper(Stepper stepper)
    {
        this.stepper = stepper;
    }

    public MotorSettingsStepperFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        steppersettingsViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_motor_settings_stepper, container, false);
        steppersettingsViewBinding.setStepper(stepper);


        // Inflate the layout for this fragment
        return steppersettingsViewBinding.getRoot();
    }
}