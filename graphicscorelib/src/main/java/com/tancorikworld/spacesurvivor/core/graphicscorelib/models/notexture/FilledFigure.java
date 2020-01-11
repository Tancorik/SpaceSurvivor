package com.tancorikworld.spacesurvivor.core.graphicscorelib.models.notexture;

import android.opengl.GLES20;

import androidx.annotation.NonNull;

/**
 * Закрашенная фигура
 *
 * @author tancorik on 11.01.2020
 */
public class FilledFigure extends BaseNoTextureFigure {

    /**
     * Публичный конструктор
     *
     * @param vertices2d набор координат вершин для фигуры, должне содержать XY - координаты, последовательность вершин должна задваваться против ЧС
     * @param color      массив цвета (RGBW)
     */
    public FilledFigure(@NonNull float[] vertices2d, @NonNull float[] color) {
        super(vertices2d, color);
    }

    @Override
    protected void drawSelf() {
        // установить цвет фигуры
        GLES20.glUniform4fv(mColorLocation, 1, mColor, 0);

        // рисовать многоугольник
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, mVertexCount);
    }
}
