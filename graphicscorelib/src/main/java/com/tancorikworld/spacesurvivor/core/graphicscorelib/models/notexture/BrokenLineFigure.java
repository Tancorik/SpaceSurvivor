package com.tancorikworld.spacesurvivor.core.graphicscorelib.models.notexture;

import android.opengl.GLES20;

import androidx.annotation.NonNull;

/**
 * Ломаная линия
 *
 * @author tancorik on 11.01.2020
 */
public class BrokenLineFigure extends BaseNoTextureFigure {

    private final float mLineWidth;

    /**
     * Публичный конструктор
     *
     * @param vertices2d набор координат вершин для фигуры, должне содержать XY - координаты
     */
    public BrokenLineFigure(@NonNull float[] vertices2d, @NonNull float[] color, float lineWidth) {
        super(vertices2d, color);
        mLineWidth = lineWidth;
    }

    @Override
    protected void drawSelf() {
        // установить цвет линии
        GLES20.glUniform4fv(mColorLocation, 1, mColor, 0);

        // рисовать линию
        GLES20.glLineWidth(mLineWidth);
        GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, mVertexCount);
    }
}
