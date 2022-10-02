package com.uc2control.models;

import com.api.ApiServiceCallback;
import com.api.RestController;
import com.api.response.StepperItem;
import com.api.response.StepperRequest;

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
        StepperRequest request = new StepperRequest();
        request.stepperItem = new StepperItem[4];
        request.stepperItem[0] = stepperA.getStepper(0);
        request.stepperItem[1] = stepperX.getStepper(1);
        request.stepperItem[2] = stepperY.getStepper(2);
        request.stepperItem[3] = stepperZ.getStepper(3);
        if (restController.getRestClient() != null)
            restController.getRestClient().setMotorPins(request, new ApiServiceCallback<Void>() {
                @Override
                public void onResponse(Void response) {

                }
            });
    }
}
