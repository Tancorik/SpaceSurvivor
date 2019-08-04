package com.tancorikworld.spacesurvivor.presentation.renderers;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.tancorikworld.spacesurvivor.R;
import com.tancorikworld.spacesurvivor.application.SpaceApplication;
import com.tancorikworld.spacesurvivor.models.DemoSimpleSquad;
import com.tancorikworld.spacesurvivor.models.DemoTriangle;
import com.tancorikworld.spacesurvivor.models.helpers.TextureArea;
import com.tancorikworld.spacesurvivor.models.primitive.Rectangle;
import com.tancorikworld.spacesurvivor.utils.TextureUtils;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class DemoRenderer implements GLSurfaceView.Renderer {

    private float mRatio;

    private int X_ORDER = 640;

    private float Y_RATIO;

    DemoTriangle mDemoTriangle;
    DemoSimpleSquad mSquad;
    Rectangle mRectangle;
    Rectangle mRectangle2;
    private int mAngle;

    private final float[] vPMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private int mTexture;

    private volatile float mX;
    private volatile float mY;

    private float[] rotationMatrix = new float[16];

    public DemoRenderer() {

    }


    public void setCoord(float x, float y) {
        mX = x * mRatio;
        mY = Y_RATIO - y * mRatio;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(1.0f, 1.0f, 0.0f, 1.0f);

        mTexture = TextureUtils.loadTexture(SpaceApplication.getAppComponent().getContext(), R.drawable.demo_box);

        mDemoTriangle = new DemoTriangle(0f, 0.9f, 0f, -0.9f, 0.9f, 0f, 1.0f, 0.0f, 0f);
        mRectangle = new Rectangle(150, 150, mTexture, new TextureArea(0.2f,0.2f, 0.8f, 0.8f),
                SpaceApplication.getAppComponent().getProgramCreator());
        mRectangle2 = new Rectangle(50, 50, mTexture, new TextureArea(0f,0f, 0.5f, 0.5f),
                SpaceApplication.getAppComponent().getProgramCreator());

        // mSquad = new DemoSimpleSquad(-0f, -0f, 200f, 200f, 1f, 0, 0);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        mRatio = (float) X_ORDER/width;
        Y_RATIO = X_ORDER * (float) height / width;
        // float phoneRatio = X_ORDER * (float) height / width;

        // Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
        Matrix.frustumM(projectionMatrix, 0, 0, X_ORDER, 0, Y_RATIO, 3, 7);

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);


        Matrix.setLookAtM(viewMatrix, 0, 0, 0, 3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0);


        long time = SystemClock.uptimeMillis() % 4000L;
        float angle = 0.090f * ((int) time);
        // Matrix.setRotateM(rotationMatrix, 0, angle, 0, 0, -2.0f);


        // float[] scratch = new float[16];
        // Matrix.multiplyMM(scratch, 0, vPMatrix, 0, rotationMatrix, 0);


        // Draw shape
        // mDemoTriangle.draw(scratch);
        mRectangle.draw(viewMatrix, projectionMatrix, (int)mX, (int)mY,mAngle++, 1);
        mRectangle2.draw(viewMatrix, projectionMatrix, 25,25, -mAngle++, 3);
        // mSquad.draw(scratch);
        // mDemoTriangle.draw();
    }
}
