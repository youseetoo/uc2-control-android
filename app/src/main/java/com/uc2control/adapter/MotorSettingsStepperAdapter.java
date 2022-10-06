package com.uc2control.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.uc2control.MotorSettingsStepperFragment;
import com.uc2control.viewmodels.MainViewModel;

public class MotorSettingsStepperAdapter extends FragmentStateAdapter {
    private MotorSettingsStepperFragment a;
    private MotorSettingsStepperFragment x;
    private MotorSettingsStepperFragment y;
    private MotorSettingsStepperFragment z;

    private MainViewModel viewModel;

    public MotorSettingsStepperAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public MotorSettingsStepperAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public MotorSettingsStepperAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    private void createFragments()
    {
        a = new MotorSettingsStepperFragment();
        a.setStepper(viewModel.getMotorModel().getStepperA());

        x = new MotorSettingsStepperFragment();
        x.setStepper(viewModel.getMotorModel().getStepperX());

        y = new MotorSettingsStepperFragment();
        y.setStepper(viewModel.getMotorModel().getStepperY());

        z = new MotorSettingsStepperFragment();
        z.setStepper(viewModel.getMotorModel().getStepperZ());

    }

    public void setViewModel(MainViewModel viewModel)
    {
        this.viewModel = viewModel;
        createFragments();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 1:
                return x;
            case 2:
                return y;
            case 3:
                return z;
            default:
                return a;
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
