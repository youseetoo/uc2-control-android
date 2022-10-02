package com.uc2control.viewmodels;

import androidx.lifecycle.ViewModel;

import com.api.RestController;
import com.uc2control.models.MotorModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MotorViewModel extends ViewModel {

    private MotorModel motorModel;

    @Inject
    public MotorViewModel(RestController restController)
    {
        motorModel = new MotorModel(restController);
    }

    public MotorModel getMotorModel() {
        return motorModel;
    }

    // TODO: Implement the ViewModel
}