package com.tancorikworld.spacesurvivor.di.components;

import android.content.Context;

import com.tancorikworld.spacesurvivor.data.OpenGlProgramCreator;
import com.tancorikworld.spacesurvivor.di.modules.MainModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Component(
        modules = {
                MainModule.class
        })
@Singleton
public interface AppComponent {

    /**
     * @return контекст приложения
     */
    Context getContext();

    /**
     * @return создатель исполняемых программ OpenGl
     */
    OpenGlProgramCreator getProgramCreator();

    @Component.Builder
    interface Builder {
        @BindsInstance Builder application(Context context);
        AppComponent build();
    }
}
