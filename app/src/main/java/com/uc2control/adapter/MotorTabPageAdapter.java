package com.uc2control.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.uc2control.MotorControlFragment;
import com.uc2control.MotorSettingsFragment;
import com.uc2control.viewmodels.MotorViewModel;

public class MotorTabPageAdapter extends FragmentStateAdapter {

    private MotorViewModel viewModel;

    public MotorTabPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public MotorTabPageAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public MotorTabPageAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    public void setViewModel(MotorViewModel viewModel)
    {
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 1:
                MotorSettingsFragment settingsFragment = new MotorSettingsFragment();
                settingsFragment.setViewModel(viewModel);
                return settingsFragment;
            default:
                MotorControlFragment controlFragment = new MotorControlFragment();
                controlFragment.setViewModel(viewModel);
                return controlFragment;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
