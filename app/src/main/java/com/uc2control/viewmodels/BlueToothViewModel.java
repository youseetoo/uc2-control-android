package com.uc2control.viewmodels;

import androidx.lifecycle.ViewModel;

import com.api.RestController;
import com.uc2control.models.BlueToothModel;

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