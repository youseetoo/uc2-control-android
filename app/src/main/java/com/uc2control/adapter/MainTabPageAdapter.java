package com.uc2control.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.uc2control.BlueToothFragment;
import com.uc2control.LedFragment;
import com.uc2control.MotorFragment;
import com.uc2control.WifiSettingsFragment;

public class MainTabPageAdapter extends FragmentStateAdapter {
    public MainTabPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public MainTabPageAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public MainTabPageAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 1:
                return new LedFragment();
            case 2:
                return new BlueToothFragment();
            case 3:
                return new MotorFragment();
            default:
                return new WifiSettingsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
