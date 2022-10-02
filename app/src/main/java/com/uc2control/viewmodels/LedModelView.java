package com.uc2control.viewmodels;

import androidx.lifecycle.ViewModel;

import com.api.RestController;
import com.uc2control.models.LedModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class LedModelView extends ViewModel {

    private LedModel ledModel;

    @Inject
    public LedModelView(RestController restController)
    {
        ledModel = new LedModel(restController);
    }

    public LedModel getLedModel() {
        return ledModel;
    }
}
