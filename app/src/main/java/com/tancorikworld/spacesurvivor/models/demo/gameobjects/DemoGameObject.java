package com.tancorikworld.spacesurvivor.models.demo.gameobjects;

import androidx.annotation.NonNull;

import com.tancorikworld.spacesurvivor.data.OpenGlProgramCreator;
import com.tancorikworld.spacesurvivor.models.demo.gameobjects.abstractions.GameObject;
import com.tancorikworld.spacesurvivor.models.demo.helpers.SimpleColor;
import com.tancorikworld.spacesurvivor.models.demo.primitive.ColoredFigure;
import com.tancorikworld.spacesurvivor.models.demo.primitive.IPrimitiveFigure;

public class DemoGameObject extends GameObject {

    private static final int ROTATE_SPEED = 15;

    private IPrimitiveFigure mFigure;
    private IPrimitiveFigure mFigure1;
    private IPrimitiveFigure mFigure2;
    private int mTargetAngle;

    public DemoGameObject(@NonNull OpenGlProgramCreator programCreator, float currentPositionX, float currentPositionY, int angle) {
        super(programCreator, currentPositionX, currentPositionY, angle);
        float[] vertex = {
                0, -5,
                50, -5,
                50, 5,
                0, 5
        };
        // mFigure = new ColoredFigure(50, 4, new SimpleColor(1, 0, 0), programCreator);
        // mFigure = new ColoredFigure(100.0f, 20.0f, new SimpleColor(1, 0, 0), programCreator);
        mFigure2 = new ColoredFigure(110f, 50, new SimpleColor(0, 0, 1), programCreator);
        mFigure1 = new ColoredFigure(20, 6, new SimpleColor(1, 0, 0), programCreator);
        mFigure = new ColoredFigure(vertex, new SimpleColor(1, 0, 0), programCreator);



    }

    @Override
    public void updateTarget(float xPosition, float yPosition) {
        super.updateTarget(xPosition, yPosition);

        mTargetAngle = calculateAngle();
        // mCurrentPositionX = xPosition;
        // mCurrentPositionY = yPosition;
    }

    @Override
    public void redraw(float[] vMatrix, float[] pMatrix) {
        //todo как то надо упростить

        if (mAngle != mTargetAngle) {
            if (Math.abs(mAngle - mTargetAngle) <= ROTATE_SPEED || Math.abs(mAngle - mTargetAngle) >= (360 - ROTATE_SPEED)) {
                mAngle = mTargetAngle;
            } else {
                int sign = Math.abs(mAngle - mTargetAngle) > 180 ? -1 : 1;
                if (mAngle < mTargetAngle) {
                    mAngle = mAngle + (sign * ROTATE_SPEED);
                } else {
                    mAngle = mAngle - (sign * ROTATE_SPEED);
                }
                if (mAngle < 0) {
                    mAngle = 360 - mAngle;
                } else if (mAngle > 360) {
                    mAngle = mAngle - 360;
                }
            }
        }
        mFigure2.draw(vMatrix, pMatrix, mCurrentPositionX, mCurrentPositionY, 0, 1);
        mFigure.draw(vMatrix, pMatrix, mCurrentPositionX, mCurrentPositionY, mAngle, 1);
        mFigure1.draw(vMatrix, pMatrix, mCurrentPositionX, mCurrentPositionY, mAngle, 1);

    }

    private int calculateAngle() {
        double a = Math.abs(mTargetPositionY - mCurrentPositionY);
        double b = Math.abs(mTargetPositionX - mCurrentPositionX);
        int result = (int) (Math.toDegrees(Math.atan(a/b)));

        if (mTargetPositionY > mCurrentPositionY && mTargetPositionX < mCurrentPositionX) {
            result = 180 - result;
        } else if (mTargetPositionY < mCurrentPositionY && mTargetPositionX < mCurrentPositionX) {
            result = 180 + result;
        } else if (mTargetPositionY < mCurrentPositionY && mTargetPositionX > mCurrentPositionX) {
            result = 360 - result;
        }
        return result;
    }
}
