package com.tancorikworld.spacesurvivor.models.demo.gameobjects.abstractions;

import com.tancorikworld.spacesurvivor.models.demo.helpers.CoordinatesXY;

/**
 * Абстракция для игровых обьектов
 */
public abstract class GameObject {

    protected float mCurrentPositionX;
    protected float mCurrentPositionY;
    protected float mTargetPositionX;
    protected float mTargetPositionY;
    protected int mAngle;
    protected int mProgram;

    protected CoordinatesXY mScreenSize;

    protected GameObject(int program, float currentPositionX, float currentPositionY, int angle) {
        mCurrentPositionX = currentPositionX;
        mCurrentPositionY = currentPositionY;
        mAngle = angle;
        mProgram = program;
    }

    /**
     * Обновить координаты цели объекта, что бы объект поворачивался или двигался к это цели
     *
     * @param xPosition координата x
     * @param yPosition координата y
     */
    public void updateTarget(float xPosition, float yPosition) {
        mTargetPositionX = xPosition;
        mTargetPositionY = yPosition;
    }

    public void setScreenSize(float x, float y) {
        mScreenSize = new CoordinatesXY(x, y);
    }

    /**
     * Перерисовать обьект
     *
     * @param vMatrix view матрица
     * @param pMatrix projection матрица
     */
    public abstract void redraw(float[] vMatrix, float[] pMatrix);
}
