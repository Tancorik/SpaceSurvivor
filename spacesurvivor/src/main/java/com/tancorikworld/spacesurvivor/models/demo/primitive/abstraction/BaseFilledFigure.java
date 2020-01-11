package com.tancorikworld.spacesurvivor.models.demo.primitive.abstraction;

import android.opengl.GLES20;
import android.opengl.Matrix;

import androidx.annotation.NonNull;

import com.tancorikworld.spacesurvivor.models.demo.helpers.SimpleColor;

import java.nio.FloatBuffer;

import static com.tancorikworld.spacesurvivor.core.graphicscorelib.data.OpenGlProgramCreator.ATTRIBUTE_VERTEX_POSITION_NAME;
import static com.tancorikworld.spacesurvivor.core.graphicscorelib.data.OpenGlProgramCreator.FRAGMENT_COLOR_NAME;
import static com.tancorikworld.spacesurvivor.core.graphicscorelib.data.OpenGlProgramCreator.VERTEX_MATRIX_NAME;

/**
 * Базовый класс для закрашенной фигуры
 *
 * @author tancorik
 */
public abstract class BaseFilledFigure implements ISimpleFigure2D {

    private static final int VERTEX_AXIS = 2;

    private final int mProgram;
    private final float[] mColor = new float[4];

    /**
     * Конструктор
     *
     * @param program     номер выполняемой программы, которая обязательно должна быть с шейдерами без текстур
     * @param simpleColor цвет фигуры
     */
    public BaseFilledFigure(int program, @NonNull SimpleColor simpleColor) {
        mProgram = program;
        setColor(simpleColor);
    }

    @Override
    public void draw(@NonNull float[] vMatrix, @NonNull float[] pMatrix, float xPosition, float yPosition, int angle, float xyScale) {
        GLES20.glUseProgram(mProgram);

        // получить ссылки на необходимые параметры программы
        int verticesLocation = GLES20.glGetAttribLocation(mProgram, ATTRIBUTE_VERTEX_POSITION_NAME);
        int colorLocation = GLES20.glGetUniformLocation(mProgram, FRAGMENT_COLOR_NAME);
        int vPMatrixHandle = GLES20.glGetUniformLocation(mProgram, VERTEX_MATRIX_NAME);

        // включить массив точек
        GLES20.glEnableVertexAttribArray(verticesLocation);
        GLES20.glVertexAttribPointer(verticesLocation, VERTEX_AXIS, GLES20.GL_FLOAT, false, 0, getVertexData());

        // единичим матрицу
        float[] modelMatrix = new float[16];
        Matrix.setIdentityM(modelMatrix, 0);

        // применяем необходимые изменения к модельке
        Matrix.translateM(modelMatrix, 0, xPosition, yPosition, 0);
        Matrix.scaleM(modelMatrix, 0, xyScale, xyScale, 1f);
        Matrix.rotateM(modelMatrix, 0, angle, 0,0, 1);

        // складываем матрицы в одну, учитывая матрицу view и матрицу проекции
        float[] scratch = new float[16];
        Matrix.multiplyMM(scratch, 0, vMatrix, 0, modelMatrix, 0);
        Matrix.multiplyMM(scratch, 0, pMatrix, 0, scratch, 0);

        // применить суммарную матрицу
        GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, scratch, 0);

        // установить цвет фигуры
        GLES20.glUniform4fv(colorLocation, 1, mColor, 0);

        // рисовать многоугольник
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, getVertexCount());

        // выключить массив точек
        GLES20.glDisableVertexAttribArray(verticesLocation);
    }



    @Override
    public void setColor(@NonNull SimpleColor simpleColor) {
        mColor[0] = simpleColor.getRed();
        mColor[1] = simpleColor.getGreen();
        mColor[2] = simpleColor.getBlue();
        mColor[3] = simpleColor.getAlpha();
    }

    /**
     * @return количество вершин
     */
    protected abstract int getVertexCount();

    /**
     * @return буффер с данными о вершинах
     */
    @NonNull
    protected abstract FloatBuffer getVertexData();
}
