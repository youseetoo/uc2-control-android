package com.uc2control.models;

import android.view.View;
import android.widget.SeekBar;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.api.RestController;
import com.api.response.MotorActRequest;
import com.api.response.items.MotorActItem;
import com.api.response.items.MotorGetItem;
import com.uc2control.BR;

public class Stepper extends BaseObservable {
    private int id;
    private final int speedRanges[] = {-4000, -2000,-1000,-500,-200,-100,-50,-20,-10,-5,-4,-3,-2,-1,0,1,2,3,4,5,10,20,50,100,200,500,1000,2000, 4000};
    private int speedPos = getDefaultPosition();
    private boolean stopMotorOnSeekbarRelease = false;
    private boolean isActivated = false;


    String position = "0";

    private RestController restController;
    private ConnectionModel connectionModel;

    public Stepper(int id, RestController restController,ConnectionModel connectionModel)
    {
        this.id = id;
        this.restController = restController;
        this.connectionModel = connectionModel;
    }


    public void setStopMotorOnSeekbarRelease(boolean stopMotorOnSeekbarRelease) {
        if (stopMotorOnSeekbarRelease == this.stopMotorOnSeekbarRelease)
            return;
        this.stopMotorOnSeekbarRelease = stopMotorOnSeekbarRelease;
    }

    @Bindable
    public boolean getStopMotorOnSeekbarRelease() {
        return stopMotorOnSeekbarRelease;
    }

    public void setMotorGetItem(MotorGetItem m)
    {
        setActivated(m.isActivated);
        notifyChange();
    }

    @Bindable
    public boolean getActivated()
    {
        return  isActivated;
    }

    void setActivated(boolean activated)
    {
        if (this.isActivated != activated)
        {
            this.isActivated = activated;
            notifyPropertyChanged(BR.activated);
        }
    }

    public void setPosition(String position) {
        if (position == this.position)
            return;
        this.position = position;
        notifyPropertyChanged(BR.position);
    }

    @Bindable
    public String getPosition() {
        return position;
    }

    public void driverForward()
    {
        setSpeedPos(getSpeedRangesSize());
    }

    public void stopDrive()
    {
        setSpeedPos(getDefaultPosition());
    }

    public void driveBackward()
    {
       setSpeedPos(0);
    }

    @Bindable
    public int getSpeedRangesSize()
    {
        return speedRanges.length-1;
    }

    @Bindable
    public int getDefaultPosition()
    {
        return speedRanges.length/2;
    }

    public void setSpeedPos(int speedPos) {
        if (speedPos == this.speedPos)
            return;
        this.speedPos = speedPos;
        notifyPropertyChanged(BR.speedPos);
        notifyPropertyChanged(BR.speedString);

        MotorActRequest request = new MotorActRequest();
        MotorActItem item = new MotorActItem();
        request.motorActItem = new MotorActItem[1];
        request.motorActItem[0] = item;
        item.stepperid = id;
        item.speed = (long) speedRanges[speedPos];
        item.maxspeed = 20000L;
        if (item.speed != 0)
            item.isforever = 1;
        else item.isforever =0;
        connectionModel.sendSocketMessage(request);
        /*if (restController.getRestClient() != null)
            restController.getRestClient().setMotorData(request, new ApiServiceCallback<Void>() {
                @Override
                public void onResponse(Void response) {

                }
            });*/
    }

    @Bindable
    public int getSpeedPos() {
        return speedPos;
    }

    @Bindable
    public int getVisibility()
    {
        //TODO
        /*if (!step_pin.equals("0"))
            return View.VISIBLE;
        else
            return View.GONE;*/
        return View.VISIBLE;
    }

    @Bindable
    public String getSpeedString()
    {
        return "" + speedRanges[speedPos];
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        if (stopMotorOnSeekbarRelease)
            setSpeedPos(getDefaultPosition());
    }

}
