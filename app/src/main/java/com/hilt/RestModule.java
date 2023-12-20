package com.hilt;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.api.RestController;
import com.uc2control.models.EspCameraModel;
import com.uc2control.models.ImSwitchCameraModel;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ActivityContext;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.android.scopes.ActivityScoped;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class RestModule {
    @Provides
    @Singleton
    public static RestController restController()
    {
        return new RestController();
    }

    @Singleton
    @Provides
    public static SharedPreferences sharedPreferences(@ApplicationContext Context context)
    {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Singleton
    @Provides
    public static EspCameraModel espCameraModel(SharedPreferences sharedPreferences,@ApplicationContext Context context)
    {
        return new EspCameraModel(sharedPreferences,context);
    }

    @Singleton
    @Provides
    public static ImSwitchCameraModel ImSwitchCameraModel(SharedPreferences sharedPreferences,@ApplicationContext Context context)
    {
        return new ImSwitchCameraModel(sharedPreferences,context);
    }

}
