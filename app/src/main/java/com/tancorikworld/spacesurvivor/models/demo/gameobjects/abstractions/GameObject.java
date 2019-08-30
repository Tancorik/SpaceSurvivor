package com.tancorikworld.spacesurvivor.models.demo.gameobjects.abstractions;

import androidx.annotation.NonNull;

import com.tancorikworld.spacesurvivor.data.OpenGlProgramCreator;

/**
 * Абстракция для игровых обьектов
 */
public abstract class GameObject {

    private OpenGlProgramCreator mGlProgramCreator;
    protected float mCurrentPositionX;
    protected float mCurrentPositionY;
    protected float mTargetPositionX;
    protected float mTargetPositionY;
    protected int mAngle;

    protected GameObject(@NonNull OpenGlProgramCreator programCreator, float currentPositionX, float currentPositionY, int angle) {
        mGlProgramCreator = programCreator;
        mCurrentPositionX = currentPositionX;
        mCurrentPositionY = currentPositionY;
        mAngle = angle;
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

    /**
     * Перерисовать обьект
     *
     * @param vMatrix view матрица
     * @param pMatrix projection матрица
     */
    public abstract void redraw(float[] vMatrix, float[] pMatrix);
}
