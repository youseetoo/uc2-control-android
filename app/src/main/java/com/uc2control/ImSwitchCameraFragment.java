package com.uc2control;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.uc2control.databinding.FragmentImswitchcameraBinding;
import com.uc2control.viewmodels.MainViewModel;

public class ImSwitchCameraFragment extends Fragment {
    private MainViewModel modelView;
    private FragmentImswitchcameraBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        modelView = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        binding =  DataBindingUtil.inflate(inflater, R.layout.fragment_imswitchcamera, container, false);
        binding.setImswitchcamera(modelView.getImswitchCameraModel());
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Optionally, set some configuration options for PhotoView
        binding.monitor.setMaximumScale(5); // Set the maximum zoom scale
        binding.monitor.setMediumScale(3);  // Set the medium zoom scale

        // Set the double tap listener to reset zoom and pan to default
        binding.monitor.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                // Handle single tap (if needed)
                return false;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                // Reset zoom and pan to default
                binding.monitor.setScale(1f, true);
                binding.monitor.setTranslationX(0f);
                binding.monitor.setTranslationY(0f);
                return true;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                // Handle double tap event (if needed)
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        /*if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // If permission is not granted, request it
            requestStoragePermission();
        }*/
    }

    private static final int REQUEST_WRITE_STORAGE_PERMISSION = 1;

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted, proceed with saving the file
                } else {
                    // Permission was denied, inform the user or disable the feature
                    Toast.makeText(getContext(), "Permission to write to storage was denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
}
