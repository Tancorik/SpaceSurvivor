package com.tancorikworld.spacesurvivor.core.graphicscorelib.presentation;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class DemoCoreGlSurfaceView extends GLSurfaceView {

    private final Renderer mRenderer;

    public DemoCoreGlSurfaceView(Context context) {
        super(context);
        setEGLContextClientVersion(2);

        mRenderer = new DemoCoreRenderer();
        setRenderer(mRenderer);

    }
}
