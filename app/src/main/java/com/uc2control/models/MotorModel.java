package com.uc2control.models;

import com.api.ApiServiceCallback;
import com.api.RestController;
import com.api.response.MotorGetResponse;
import com.api.response.items.MotorSetPinsItem;
import com.api.response.MotorSetRequest;

public class MotorModel {
    private Stepper stepperX;
    private Stepper stepperY;
    private Stepper stepperZ;
    private Stepper stepperA;

    private RestController restController;

    public MotorModel(RestController restController)
    {
        this.restController = restController;
        stepperX = new Stepper();
        stepperY = new Stepper();
        stepperZ = new Stepper();
        stepperA = new Stepper();
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
