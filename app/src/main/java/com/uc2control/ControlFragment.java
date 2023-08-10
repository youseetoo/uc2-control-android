package com.uc2control;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.uc2control.adapter.MainTabPageAdapter;
import com.uc2control.databinding.ActivityMainBinding;
import com.uc2control.databinding.FragmentControlBinding;
import com.uc2control.viewmodels.MainViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ControlFragment extends Fragment {


    private MainTabPageAdapter pageAdapter;
    private TabLayoutMediator tabLayoutMediator;
    private FragmentControlBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        pageAdapter = new MainTabPageAdapter(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_control, container, false);// DataBindingUtil.setContentView(this,R.layout.activity_main);
        pageAdapter = new MainTabPageAdapter(this);
        MainViewModel mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        binding.setConnectionModel(mainViewModel.getConnectionModel());
        binding.pager.setAdapter(pageAdapter);
        binding.pager.setUserInputEnabled(false);


        tabLayoutMediator = new TabLayoutMediator(binding.tabLayout, binding.pager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch(position)
                {
                    case 1:
                        tab.setText("Led");
                        break;
                    case 2:
                        tab.setText("BT");
                        break;
                    case 3:
                        tab.setText("Motor");
                        break;
                    default:
                        tab.setText("Wifi");
                        break;
                }
            }
        });
        tabLayoutMediator.attach();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}