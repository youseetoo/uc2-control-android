package com.uc2control.models;

public class MotorModel {
    private Stepper stepperX;
    private Stepper stepperY;
    private Stepper stepperZ;
    private Stepper stepperA;

    public MotorModel()
    {
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

    }
}
