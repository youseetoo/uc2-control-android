package com.uc2control.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.uc2control.MotorControlFragment;

public class MotorTabPageAdapter extends FragmentStateAdapter {

    private MotorControlFragment controlFragment;

    public MotorTabPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public MotorTabPageAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public MotorTabPageAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            default:
                if (controlFragment == null)
                    controlFragment = new MotorControlFragment();
                return controlFragment;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
