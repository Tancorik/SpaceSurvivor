package com.tancorikworld.spacesurvivor.models.demo.primitive.abstraction;

import androidx.annotation.NonNull;

import com.tancorikworld.spacesurvivor.models.demo.helpers.SimpleColor;

/**
 * Интерфейс простой двумерной фигуры
 *
 * @author tancorik
 */
public interface ISimpleFigure2D {

    /**
     * Нарисовать фигуру не меняя цвет
     *
     * @param vMatrix   view матрица
     * @param pMatrix   матрица проекции
     * @param xPosition координата X
     * @param yPosition координата Y
     * @param angle     угол отрисовки
     * @param xyScale   масштаб отрисовки
     */
    void draw(float[] vMatrix, float[] pMatrix, float xPosition, float yPosition, int angle, float xyScale);

    /**
     * Установить цвет, если требуется поменять цвет, вызвать перед {@link this#draw(float[], float[], float, float, int, float)}
     *
     * @param simpleColor цвет
     */
    void setColor(@NonNull SimpleColor simpleColor);
}
