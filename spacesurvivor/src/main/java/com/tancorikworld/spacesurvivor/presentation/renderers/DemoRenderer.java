package com.tancorikworld.spacesurvivor.presentation.renderers;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import com.tancorikworld.spacesurvivor.R;
import com.tancorikworld.spacesurvivor.application.SpaceApplication;
import com.tancorikworld.spacesurvivor.models.demo.gameobjects.DemoGameObject;
import com.tancorikworld.spacesurvivor.models.demo.gameobjects.DemoShells;
import com.tancorikworld.spacesurvivor.models.demo.gameobjects.DemoStaticSpace;
import com.tancorikworld.spacesurvivor.models.demo.gameobjects.DemoWall;
import com.tancorikworld.spacesurvivor.models.demo.gameobjects.abstractions.GameObject;
import com.tancorikworld.spacesurvivor.models.demo.helpers.SimpleColor;
import com.tancorikworld.spacesurvivor.models.demo.helpers.TextureArea;
import com.tancorikworld.spacesurvivor.models.demo.primitive.ColoredFigure;
import com.tancorikworld.spacesurvivor.models.demo.primitive.Stars;
import com.tancorikworld.spacesurvivor.models.demo.primitive.abstraction.IPrimitiveFigure;
import com.tancorikworld.spacesurvivor.models.demo.primitive.TexturedRectangle;
import com.tancorikworld.spacesurvivor.utils.TextureUtils;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class DemoRenderer implements GLSurfaceView.Renderer {

    private float mCurrentX = 100;
    private float mCurrentY = 100;
    private float mTargetX;
    private float mTargetY;
    private float mSpeed = 10;

    private volatile float mRatio;

    private int X_ORDER = 640;

    private float Y_RATIO;

    private GameObject mDemoGameObject;
    private GameObject mDemoWall;
    private GameObject mDemoShells;
    private GameObject mSpace;

    private IPrimitiveFigure mStars;

    private TexturedRectangle mRectangle;
    private TexturedRectangle mRectangle2;
    private IPrimitiveFigure mColoredFigure;
    private int mAngle;

    private final float[] vPMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private int mTexture;

    private float mX;
    private float mY;
    private boolean isNeedUpdateTargets = false;

    FPSCounter mFPSCounter = new FPSCounter();

    public void setCoord(float x, float y) {
        mX = x * mRatio;
        mY = Y_RATIO - y * mRatio;
        isNeedUpdateTargets = true;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.14f, 1.0f);

        // установки для прозрачности текстуры где необходимо
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glEnable(GLES20.GL_BLEND);

        // забить картинку в текстуры и получить id данной тектуры
        mTexture = TextureUtils.loadTexture(SpaceApplication.getAppComponent().getContext(), R.drawable.planet13, GLES20.GL_TEXTURE0);

        mRectangle = new TexturedRectangle(50, 50, mTexture, GLES20.GL_TEXTURE0, new TextureArea(0f,0f, 1f, 1f),
                SpaceApplication.getAppComponent().getProgramCreator());
        // mRectangle2 = new TexturedRectangle(50, 50, mTexture, GLES20.GL_TEXTURE0, new TextureArea(0f,0f, 0.5f, 0.5f),
        //         SpaceApplication.getAppComponent().getProgramCreator());

        mColoredFigure = new ColoredFigure(10, 48, new SimpleColor(1f, 0f, 0f),
                SpaceApplication.getAppComponent().getProgramCreator());

        int program = SpaceApplication.getAppComponent().getProgramCreator().createProgram(false);
        mDemoGameObject = new DemoGameObject(program, SpaceApplication.getAppComponent().getProgramCreator(), 300, 200, 0);
        mDemoWall = new DemoWall(program, SpaceApplication.getAppComponent().getProgramCreator(), 500, 200, 0);
        mDemoShells = new DemoShells(program, SpaceApplication.getAppComponent().getProgramCreator(), 300, 200, 0);
        mSpace = new DemoStaticSpace(program);
        mStars = new Stars(program);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        mRatio = (float) X_ORDER/width;
        // Y_RATIO = X_ORDER * (float) height / width;
        Y_RATIO = mRatio * height;

        Matrix.frustumM(projectionMatrix, 0, 0, X_ORDER, 0, Y_RATIO, 3, 7);

        mSpace.setScreenSize(X_ORDER, Y_RATIO);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        Matrix.setLookAtM(viewMatrix, 0, 0, 0, 3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        mTargetX = mX;
        mTargetY = mY;
        calculateCurrentPosition();
        // Draw shape
        mSpace.redraw(viewMatrix, projectionMatrix);

        if (isNeedUpdateTargets) {
            mDemoGameObject.updateTarget(mTargetX, mTargetY);
            mDemoShells.updateTarget(mTargetX, mTargetY);
            isNeedUpdateTargets = false;
        }

        mStars.draw(viewMatrix, projectionMatrix, 0, 0, 0, 1);

        mDemoShells.redraw(viewMatrix, projectionMatrix);
        mDemoGameObject.redraw(viewMatrix, projectionMatrix);
        mDemoWall.redraw(viewMatrix, projectionMatrix);

        mRectangle.draw(viewMatrix, projectionMatrix, mCurrentX, mCurrentY ,mAngle++, 1);

        mFPSCounter.logFrame();

        // mRectangle2.draw(viewMatrix, projectionMatrix, 25,25, -mAngle++, 3);
        // mColoredFigure.draw(viewMatrix, projectionMatrix, 100, 100, 0, 1);
    }

    private void calculateCurrentPosition() {
        if (mCurrentX == mTargetX && mCurrentY == mTargetY) {
            return;
        }

        float length = (float) Math.sqrt(Math.pow(mCurrentX - mTargetX, 2) + Math.pow(mCurrentY - mTargetY, 2));
        float moveRatio = length / mSpeed;
        if (moveRatio < 1) {
            mCurrentX = mTargetX;
            mCurrentY = mTargetY;
            return;
        }

        int sign = mCurrentX > mTargetX ? -1 : 1;
        float pathLeft =  Math.abs(mCurrentX - mTargetX);
        float step = pathLeft / moveRatio;
        mCurrentX = pathLeft > step ?  mCurrentX + step * sign : mTargetX;

        sign = mCurrentY > mTargetY ? -1 : 1;
        pathLeft = Math.abs(mCurrentY - mTargetY);
        step = pathLeft / moveRatio;
        mCurrentY = pathLeft > step ? mCurrentY + step * sign : mTargetY;

    }

    public class FPSCounter {
        long startTime = System.nanoTime();
        int frames = 0;

        public void logFrame() {
            frames++;
            if(System.nanoTime() - startTime >= 1000000000) {
                Log.d("FPSCounter", "fps: " + frames);
                frames = 0;
                startTime = System.nanoTime();
            }
        }
    }
}
