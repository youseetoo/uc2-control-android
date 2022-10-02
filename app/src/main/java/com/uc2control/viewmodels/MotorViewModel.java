package com.uc2control.viewmodels;

import androidx.lifecycle.ViewModel;

import com.uc2control.models.MotorModel;

public class MotorViewModel extends ViewModel {

    private MotorModel motorModel;

    public MotorViewModel()
    {
        motorModel = new MotorModel();
    }

    public MotorModel getMotorModel() {
        return motorModel;
    }

    // TODO: Implement the ViewModel
}