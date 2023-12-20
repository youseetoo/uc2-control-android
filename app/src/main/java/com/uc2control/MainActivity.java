package com.uc2control;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.uc2control.adapter.MainControlCamTab;
import com.uc2control.adapter.MainTabPageAdapter;
import com.uc2control.databinding.ActivityMainBinding;
import com.uc2control.viewmodels.MainViewModel;

import dagger.hilt.android.AndroidEntryPoint;
import dagger.hilt.android.HiltAndroidApp;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private ViewPager2 pager;
    private MainControlCamTab pageAdapter;
    private TabLayout tabLayout;
    private TabLayoutMediator tabLayoutMediator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainViewModel mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        getLifecycle().addObserver(mainViewModel);
        pageAdapter = new MainControlCamTab(this);
        pager = findViewById(R.id.pagermotor);
        pager.setAdapter(pageAdapter);
        tabLayout = findViewById(R.id.tab_layoutmotor);
        tabLayoutMediator = new TabLayoutMediator(tabLayout, pager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch(position)
                {
                    case 1:
                        tab.setText("Cam");
                    break;
                    case 2:
                        tab.setText("IS");
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