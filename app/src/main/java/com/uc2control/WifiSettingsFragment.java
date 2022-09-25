package com.uc2control;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.databinding.adapters.TextViewBindingAdapter;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.uc2control.databinding.FragmentWifiSettingsBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class WifiSettingsFragment extends Fragment {

    private final String TAG = WifiSettingsFragment.class.getSimpleName();
    private WifiSettingsModelView wifiSettingsModelView;
    private FragmentWifiSettingsBinding wifiSettingsFragmentBinding;
    private ArrayAdapter<String> adapter;

    public WifiSettingsFragment() {
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
        wifiSettingsModelView = new ViewModelProvider(this).get(WifiSettingsModelView.class);
        wifiSettingsFragmentBinding =  DataBindingUtil.inflate(inflater, R.layout.fragment_wifi_settings, container, false);
        wifiSettingsFragmentBinding.setWifimodel(wifiSettingsModelView.getWifiSettingsModel());
        adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, wifiSettingsModelView.getWifiSettingsModel().getWifi_ssids());
        wifiSettingsModelView.getWifiSettingsModel().addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (propertyId == BR.wifi_ssids)
                {
                    adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, wifiSettingsModelView.getWifiSettingsModel().getWifi_ssids());
                    wifiSettingsFragmentBinding.listviewWifissids.setAdapter(adapter);
                }
            }
        });
        wifiSettingsFragmentBinding.listviewWifissids.setAdapter(adapter);

        wifiSettingsFragmentBinding.listviewWifissids.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (view instanceof TextView)
                    wifiSettingsModelView.getWifiSettingsModel().setSsid((String) ((TextView) view).getText());
            }
        });
        return wifiSettingsFragmentBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");
    }
}