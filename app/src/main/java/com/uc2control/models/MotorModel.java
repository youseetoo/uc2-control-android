package com.uc2control.models;

import com.api.ApiServiceCallback;
import com.api.RestController;
import com.api.response.MotorGetResponse;
import com.api.response.items.MotorSetPinsItem;
import com.api.response.MotorSetRequest;
import com.uc2control.BR;

public class MotorModel {
    private Stepper stepperX;
    private Stepper stepperY;
    private Stepper stepperZ;
    private Stepper stepperA;

    private RestController restController;
    private ConnectionModel connectionModel;

    public MotorModel(RestController restController,ConnectionModel connectionModel)
    {
        this.restController = restController;
        this.connectionModel = connectionModel;
        stepperX = new Stepper(1,restController,connectionModel);
        stepperY = new Stepper(2,restController,connectionModel);
        stepperZ = new Stepper(3,restController,connectionModel);
        stepperA = new Stepper(0,restController,connectionModel);
    }

    public Stepper getStepperA() {
        return stepperA;
    }

    public Stepper getStepperX() {
        return stepperX;
    }

    public Stepper getStepperY() {
        return stepperY;
    }

    public Stepper getStepperZ() {
        return stepperZ;
    }

    public void applyStepperSettings()
    {
        MotorSetRequest request = new MotorSetRequest();
        request.motorSetPinsItem = new MotorSetPinsItem[4];
        request.motorSetPinsItem[0] = stepperA.getStepper(0);
        request.motorSetPinsItem[1] = stepperX.getStepper(1);
        request.motorSetPinsItem[2] = stepperY.getStepper(2);
        request.motorSetPinsItem[3] = stepperZ.getStepper(3);
        if (restController.getRestClient() != null)
            restController.getRestClient().setMotorPins(request, new ApiServiceCallback<Void>() {
                @Override
                public void onResponse(Void response) {
                    stepperA.notifyPropertyChanged(BR.visibility);
                    stepperX.notifyPropertyChanged(BR.visibility);
                    stepperY.notifyPropertyChanged(BR.visibility);
                    stepperZ.notifyPropertyChanged(BR.visibility);
                }
            });
    }

    public void getMotorData()
    {
        if (restController.getRestClient() == null)
            return;
        restController.getRestClient().getMotorData(new ApiServiceCallback<MotorGetResponse>() {
            @Override
            public void onResponse(MotorGetResponse response) {
                stepperA.setMotorGetItem(response.motorGetItem[0]);
                stepperX.setMotorGetItem(response.motorGetItem[1]);
                stepperY.setMotorGetItem(response.motorGetItem[2]);
                stepperZ.setMotorGetItem(response.motorGetItem[3]);
            }
        });
    }
}
