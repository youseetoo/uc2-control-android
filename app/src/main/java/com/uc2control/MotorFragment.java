package com.uc2control;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.uc2control.adapter.MainTabPageAdapter;
import com.uc2control.adapter.MotorTabPageAdapter;
import com.uc2control.viewmodels.MotorViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MotorFragment extends Fragment {

    private MotorViewModel mViewModel;
    private ViewPager2 pager;
    private MotorTabPageAdapter pageAdapter;
    private TabLayout tabLayout;
    private TabLayoutMediator tabLayoutMediator;

    public static MotorFragment newInstance() {
        return new MotorFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(MotorViewModel.class);
        pageAdapter = new MotorTabPageAdapter(this);
        pageAdapter.setViewModel(mViewModel);
        return inflater.inflate(R.layout.fragment_motor, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pager = view.findViewById(R.id.pagermotor);
        pager.setAdapter(pageAdapter);
        tabLayout = view.findViewById(R.id.tab_layoutmotor);
        tabLayoutMediator = new TabLayoutMediator(tabLayout, pager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch(position)
                {
                    case 1:
                        tab.setText("Settings");
                        break;
                    default:
                        tab.setText("Control");
                        break;
                }
            }
        });
        tabLayoutMediator.attach();
    }
}