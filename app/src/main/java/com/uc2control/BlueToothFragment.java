package com.uc2control;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uc2control.adapter.BtDevicesAdapter;
import com.uc2control.databinding.FragmentBlueToothBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class BlueToothFragment extends Fragment {

    private BlueToothViewModel mViewModel;
    private FragmentBlueToothBinding blueToothBinding;
    private BtDevicesAdapter btDevicesAdapter;

    public static BlueToothFragment newInstance() {
        return new BlueToothFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(BlueToothViewModel.class);
        blueToothBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_blue_tooth, container, false);
        blueToothBinding.setBtmodel(mViewModel.getBlueToothModel());
        btDevicesAdapter = new BtDevicesAdapter(getContext(),mViewModel.getBlueToothModel().getBtScanItems());
        mViewModel.getBlueToothModel().addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (propertyId == BR.btScanItems)
                {
                    btDevicesAdapter = new BtDevicesAdapter(getContext(),mViewModel.getBlueToothModel().getBtScanItems());
                    blueToothBinding.listViewBtdevices.setAdapter(btDevicesAdapter);
                }
            }
        });
        blueToothBinding.listViewBtdevices.setAdapter(btDevicesAdapter);
        return blueToothBinding.getRoot();
    }
}