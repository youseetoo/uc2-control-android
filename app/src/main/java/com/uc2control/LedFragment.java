package com.uc2control;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uc2control.databinding.FragmentLedBinding;
import com.uc2control.viewmodels.LedModelView;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LedFragment extends Fragment {

    private final String TAG = LedFragment.class.getSimpleName();
    private LedModelView ledModelView;
    private FragmentLedBinding ledBinding;

    public LedFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ledModelView = new ViewModelProvider(this).get(LedModelView.class);
        ledBinding =  DataBindingUtil.inflate(inflater, R.layout.fragment_led, container, false);
        ledBinding.setLed(ledModelView.getLedModel());
        // Inflate the layout for this fragment
        return ledBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        ledModelView.getLedModel().getLedSettings();
        ledModelView.getLedModel().resumeWebSocket();
        Log.d(TAG,"onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        ledModelView.getLedModel().pauseWebSocket();
    }
}