package com.uc2control;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.uc2control.adapter.MotorSettingsStepperAdapter;
import com.uc2control.adapter.MotorTabPageAdapter;
import com.uc2control.databinding.FragmentMotorSettingsBinding;
import com.uc2control.viewmodels.MotorViewModel;


public class MotorSettingsFragment extends Fragment {

    private MotorViewModel viewModel;
    private FragmentMotorSettingsBinding settingsBinding;
    private MotorSettingsStepperAdapter stepperAdapter;
    private TabLayout tabLayout;
    private TabLayoutMediator tabLayoutMediator;

    public void setViewModel(MotorViewModel viewModel)
    {
        this.viewModel = viewModel;
    }

    public MotorSettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        settingsBinding =  DataBindingUtil.inflate(inflater, R.layout.fragment_motor_settings, container, false);
        settingsBinding.setMotor(viewModel.getMotorModel());
        stepperAdapter = new MotorSettingsStepperAdapter(this);
        stepperAdapter.setViewModel(viewModel);
        settingsBinding.pagermotorstepper.setAdapter(stepperAdapter);
        tabLayoutMediator = new TabLayoutMediator(settingsBinding.tabLayoutmotorstepper, settingsBinding.pagermotorstepper, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch(position)
                {
                    case 1:
                        tab.setText("X");
                        break;
                    case 2:
                        tab.setText("Y");
                        break;
                    case 3:
                        tab.setText("Z");
                        break;
                    default:
                        tab.setText("A");
                        break;
                }
            }
        });
        tabLayoutMediator.attach();
        return settingsBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.getMotorModel().getMotorData();
    }
}