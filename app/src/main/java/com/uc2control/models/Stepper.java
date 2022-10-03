package com.uc2control.models;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.api.response.items.MotorGetItem;
import com.api.response.items.MotorSetPinsItem;
import com.uc2control.BR;

public class Stepper extends BaseObservable {
    String step_pin = "0";
    String dir_pin = "0";
    String power_pin = "0";

    boolean step_inverted = false;
    boolean dir_inverted = false;
    boolean power_inverted = false;
    @Bindable
    public String getStep_pin() {
        return step_pin;
    }

    public void setStep_pin(String step_pin) {
        if (this.step_pin == step_pin)
            return;
        this.step_pin = step_pin;
        notifyPropertyChanged(BR.step_pin);
    }
    @Bindable
    public String getDir_pin() {
        return dir_pin;
    }

    public void setDir_pin(String dir_pin) {
        if (this.dir_pin == dir_pin)
            return;
        this.dir_pin = dir_pin;
        notifyPropertyChanged(BR.dir_pin);
    }
    @Bindable
    public String getPower_pin() {
        return power_pin;
    }

    public void setPower_pin(String power_pin) {
        if (this.power_pin == power_pin)
            return;
        this.power_pin = power_pin;
        notifyPropertyChanged(BR.power_pin);
    }


    public void setDir_inverted(boolean dir_inverted) {
        if (this.dir_inverted == dir_inverted)
            return;
        this.dir_inverted = dir_inverted;
        notifyPropertyChanged(BR.dir_inverted);
    }
    @Bindable
    public boolean isDir_inverted() {
        return dir_inverted;
    }

    public void setPower_inverted(boolean power_inverted) {
        if (this.power_inverted = power_inverted)
            return;
        this.power_inverted = power_inverted;
        notifyPropertyChanged(BR.power_inverted);
    }
    @Bindable
    public boolean isPower_inverted() {
        return power_inverted;
    }

    public void setStep_inverted(boolean step_inverted) {
        if (this.step_inverted == step_inverted)
            return;
        this.step_inverted = step_inverted;
        notifyPropertyChanged(BR.step_inverted);
    }

    @Bindable
    public boolean isStep_inverted() {
        return step_inverted;
    }

    public MotorSetPinsItem getStepper(int id)
    {
        MotorSetPinsItem item =new MotorSetPinsItem();
        item.dir = Integer.parseInt(dir_pin);
        item.step = Integer.parseInt(step_pin);
        item.enable = Integer.parseInt(power_pin);
        item.dir_inverted = dir_inverted;
        item.step_inverted = step_inverted;
        item.enable_inverted = power_inverted;
        item.stepperid = id;
        return item;
    }

    public void setMotorGetItem(MotorGetItem m)
    {
        step_pin = ""+m.step;
        dir_pin = ""+m.dir;
        power_pin = ""+m.enable;
        step_inverted = m.step_inverted;
        dir_inverted = m.dir_inverted;
        power_inverted = m.enable_inverted;
        notifyChange();
    }
}
