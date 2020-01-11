package com.tancorikworld.spacesurvivor.core.graphicscorelib.models;

/**
 * Интерфейс любой фигуры для 2d
 *
 * @author tancorik on 11.01.2020
 */
public interface IFigure {

    /**
     * Нарисовать фигуру
     *
     * @param vMatrix    view - Матрица
     * @param pMatrix    projection - марица
     * @param xPosition  положение по оси X
     * @param yPosition  положение по оси Y
     * @param angle      угол поворота фигуры
     */
    void draw(float[] vMatrix, float[] pMatrix, float xPosition, float yPosition, int angle);

    /**
     * Нарисовать фигуру
     *
     * @param vMatrix    view - Матрица
     * @param pMatrix    projection - марица
     * @param xPosition  положение по оси X
     * @param yPosition  положение по оси Y
     * @param angle      угол поворота фигуры
     * @param xyScale    масштаб по двум осям
     */
    void draw(float[] vMatrix, float[] pMatrix, float xPosition, float yPosition, int angle, float xyScale);

    /**
     * Нарисовать фигуру
     *
     * @param vMatrix    view - Матрица
     * @param pMatrix    projection - марица
     * @param xPosition  положение по оси X
     * @param yPosition  положение по оси Y
     * @param angle      угол поворота фигуры
     * @param xScale     масштаб по оси Х
     * @param yScale     мфсштфб по осн Y
     */
    void draw(float[] vMatrix, float[] pMatrix, float xPosition, float yPosition, int angle, float xScale, float yScale);
}
