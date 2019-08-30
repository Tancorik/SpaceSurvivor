package com.tancorikworld.spacesurvivor.models.demo.primitive;

/**
 * Интерфейс приметивной фигуры
 *
 * @author Karpachev Aleksandr on 04.08.2019
 */
public interface IPrimitiveFigure {

   /**
    * Нарисовать фигуру
    *
    * @param vMatrix    view - Матрица
    * @param pMatrix    projection - марица
    * @param xPosition  положение по оси X
    * @param yPosition  положение по оси Y
    * @param angle      угол поворота фигуры
    * @param xyScale    масштаб по двум осям //todo надо переделать на настраиваимую переменную
    */
   void draw(float[] vMatrix, float[] pMatrix, float xPosition, float yPosition, int angle, float xyScale);

}
