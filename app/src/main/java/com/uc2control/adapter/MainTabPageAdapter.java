package com.uc2control.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.uc2control.BlueToothFragment;
import com.uc2control.LedFragment;
import com.uc2control.MotorControlFragment;
import com.uc2control.WifiSettingsFragment;

public class MainTabPageAdapter extends FragmentStateAdapter {

    private LedFragment ledFragment;
    private BlueToothFragment blueToothFragment;
    private MotorControlFragment motorFragment;
    private WifiSettingsFragment wifiSettingsFragment;
    public MainTabPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        createFragments();
    }

    public MainTabPageAdapter(@NonNull Fragment fragment) {
        super(fragment);
        createFragments();
    }

    public MainTabPageAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
        createFragments();
    }

    private void createFragments()
    {
        ledFragment = new LedFragment();
        blueToothFragment = new BlueToothFragment();
        motorFragment = new MotorControlFragment();
        wifiSettingsFragment = new WifiSettingsFragment();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 1:
                return ledFragment;
            case 2:
                return blueToothFragment;
            case 3:
                return motorFragment;
            default:
                return wifiSettingsFragment;
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
