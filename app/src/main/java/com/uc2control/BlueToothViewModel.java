package com.uc2control;

import androidx.lifecycle.ViewModel;

import com.api.RestController;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class BlueToothViewModel extends ViewModel {

    private BlueToothModel blueToothModel;

    @Inject
    public BlueToothViewModel(RestController restController)
    {
        blueToothModel = new BlueToothModel(restController);
    }

    public BlueToothModel getBlueToothModel() {
        return blueToothModel;
    }
}