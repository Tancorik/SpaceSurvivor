package com.tancorikworld.spacesurvivor.di.modules;

import com.tancorikworld.spacesurvivor.data.OpenGlProgramCreator;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {

    @Singleton
    @Provides
    static OpenGlProgramCreator provideOpenGLProgramCreator() {
        return new OpenGlProgramCreator();
    }

}
