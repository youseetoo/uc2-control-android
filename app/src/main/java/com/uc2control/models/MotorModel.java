package com.uc2control.models;

import com.api.ApiServiceCallback;
import com.api.RestController;
import com.api.response.MotorGetResponse;
import com.uc2control.BR;

public class MotorModel implements ConnectionModel.WebSocketMotorDataEvent {
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
        connectionModel.motorDataEvent = this;
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

    @Override
    public void onMotorDataChanged(int id, int pos) {
        switch (id)
        {
            case 0 :
                stepperA.setPosition(String.valueOf(pos));
                break;
            case 1 :
                stepperX.setPosition(String.valueOf(pos));
            break;
            case 2 :
                stepperY.setPosition(String.valueOf(pos));
            break;
            case 3 :
                stepperZ.setPosition(String.valueOf(pos));
            break;
        }
    }
}
