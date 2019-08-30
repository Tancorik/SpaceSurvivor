package com.tancorikworld.spacesurvivor.models.demo.gameobjects;

import androidx.annotation.NonNull;

import com.tancorikworld.spacesurvivor.data.OpenGlProgramCreator;
import com.tancorikworld.spacesurvivor.models.demo.gameobjects.abstractions.GameObject;
import com.tancorikworld.spacesurvivor.models.demo.helpers.Border;
import com.tancorikworld.spacesurvivor.models.demo.helpers.CoordinatesXY;
import com.tancorikworld.spacesurvivor.models.demo.helpers.SimpleColor;
import com.tancorikworld.spacesurvivor.models.demo.primitive.ColoredFigure;
import com.tancorikworld.spacesurvivor.models.demo.primitive.IPrimitiveFigure;

/**
 * Демо класс снарядов
 *
 * @author tancorik
 */
public class DemoShells extends GameObject {

    private static final int SPEED = 5;
    private static final int SIZE = 5;

    private final Border mScreenBorder = new Border(400, 0, 0, 640);

    private final Border mWallBorder = new Border(275, 495, 125, 505);

    private final IPrimitiveFigure mSelf;
    private final CoordinatesXY mStartCoordinate;
    private float mStepX;
    private float mStepY;
    private float mCollisionStepX;
    private float mCollisionStepY;
    private int mCountCollisionStep;

    private boolean mIsShoot = false;

    public DemoShells(@NonNull OpenGlProgramCreator programCreator, float currentPositionX, float currentPositionY, int angle) {
        super(programCreator, currentPositionX, currentPositionY, angle);
        mSelf = new ColoredFigure(SIZE, 9, new SimpleColor(1, 0, 0), programCreator);
        mStartCoordinate = new CoordinatesXY(currentPositionX, currentPositionY);
    }

    @Override
    public void updateTarget(float xPosition, float yPosition) {
        super.updateTarget(xPosition, yPosition);
        if (!mIsShoot) {
            mIsShoot = true;
            double angleRadian = calculateAngle();
            mStepX = calculateStepX(angleRadian, SPEED);
            mStepY = calculateStepY(angleRadian, SPEED);
            if (SPEED > SIZE) {
                mCollisionStepX = calculateStepX(angleRadian, SIZE);
                mCollisionStepY = calculateStepY(angleRadian, SIZE);
                mCountCollisionStep = (int) Math.max(Math.ceil(mStepX / mCollisionStepX), Math.ceil(mStepY / mCollisionStepY));
            } else {
                mCollisionStepX = mStepX;
                mCollisionStepY = mStepY;
                mCountCollisionStep = 1;
            }
        }
    }

    @Override
    public void redraw(float[] vMatrix, float[] pMatrix) {
        calculateNewPosition();
        mSelf.draw(vMatrix, pMatrix, mCurrentPositionX, mCurrentPositionY, 0, 1);
    }

    private void calculateNewPosition() {
        if (!mIsShoot) {
            return;
        }

        mCurrentPositionX = mCurrentPositionX + mStepX;
        mCurrentPositionY = mCurrentPositionY + mStepY;

        if ( outOfScreen() || calculateDifficult()) {
            mIsShoot = false;
            mCurrentPositionX = mStartCoordinate.getValueX();
            mCurrentPositionY = mStartCoordinate.getValueY();
        }

    }

    private boolean outOfScreen() {
        return mCurrentPositionY < mScreenBorder.getBottom()
                || mCurrentPositionY > mScreenBorder.getTop()
                || mCurrentPositionX < mScreenBorder.getLeft()
                || mCurrentPositionX > mScreenBorder.getRight();
    }

    private boolean calculateDifficult() {
        for (int x = 0; x < mCountCollisionStep; x++) {
            if (calculateCollision(mCurrentPositionX - mCollisionStepX * x, mCurrentPositionY - mCollisionStepY * x)) {
                return true;
            }
        }
        return false;
    }

    private boolean calculateCollision(float x, float y) {
        return calculateEntry(x + SIZE, y + SIZE) ||
                calculateEntry(x + SIZE, y - SIZE) ||
                calculateEntry(x - SIZE, y - SIZE) ||
                calculateEntry(x - SIZE, y + SIZE);
    }

    private boolean calculateEntry(float x, float y) {
        return x < mWallBorder.getRight() &&
                x > mWallBorder.getLeft() &&
                y < mWallBorder.getTop() &&
                y > mWallBorder.getBottom();
    }

    // возвращает угол в радианах
    private double calculateAngle() {
        double a = Math.abs(mTargetPositionY - mCurrentPositionY);
        double b = Math.abs(mTargetPositionX - mCurrentPositionX);
        return Math.atan(a/b);
    }

    private float calculateStepX(double angle, int mainStep) {
        int sign = mCurrentPositionX > mTargetPositionX ? -1 : 1;
        return (float) Math.cos(angle) * mainStep * sign;
    }

    private float calculateStepY(double angle, int mainStep) {
        int sign = mCurrentPositionY > mTargetPositionY ? -1 : 1;
        return (float) Math.sin(angle) * mainStep * sign;
    }

}
