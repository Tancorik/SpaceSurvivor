package com.tancorikworld.spacesurvivor.application;

import android.app.Application;

import com.tancorikworld.spacesurvivor.di.components.AppComponent;
import com.tancorikworld.spacesurvivor.di.components.DaggerAppComponent;

/**
 * Собственная реализация {@link Application}, для создания главного dagger-компонента.
 *
 * @author Karpachev Aleksandr on 01.08.2019
 */
public class SpaceApplication extends Application {

    private static AppComponent sAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        sAppComponent = DaggerAppComponent
                .builder()
                .application(this)
                .build();
    }

    /**
     * @return dagger-компонент приложения
     */
    public static AppComponent getAppComponent() {
        return sAppComponent;
    }
}
