package com.uc2control.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.uc2control.ControlFragment;
import com.uc2control.EspCameraFragment;
import com.uc2control.MotorControlFragment;

public class MainControlCamTab extends FragmentStateAdapter {

    private EspCameraFragment espCameraFragment;
    private ControlFragment controlFragment;

    public MainControlCamTab(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public MainControlCamTab(@NonNull Fragment fragment) {
        super(fragment);
    }

    public MainControlCamTab(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 1:
                if (espCameraFragment == null)
                    espCameraFragment = new EspCameraFragment();
                return espCameraFragment;
            default:
                if (controlFragment == null)
                    controlFragment = new ControlFragment();
                return controlFragment;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
