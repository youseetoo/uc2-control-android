package com.uc2control.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.uc2control.MotorSettingsStepperFragment;
import com.uc2control.viewmodels.MotorViewModel;

public class MotorSettingsStepperAdapter extends FragmentStateAdapter {

    private MotorViewModel viewModel;

    public MotorSettingsStepperAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public MotorSettingsStepperAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public MotorSettingsStepperAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    public void setViewModel(MotorViewModel viewModel)
    {
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        MotorSettingsStepperFragment f = new MotorSettingsStepperFragment();
        switch (position)
        {
            case 1:
                f.setStepper(viewModel.getMotorModel().getStepperX());
                break;
            case 2:
                f.setStepper(viewModel.getMotorModel().getStepperY());
                break;
            case 3:
                f.setStepper(viewModel.getMotorModel().getStepperZ());
            default:
                f.setStepper(viewModel.getMotorModel().getStepperA());
                break;
        }
        return f;
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
