package com.tancorikworld.spacesurvivor.core.graphicscorelib.models.notexture;

import android.opengl.GLES20;
import android.opengl.Matrix;

import androidx.annotation.NonNull;

import com.tancorikworld.spacesurvivor.core.graphicscorelib.data.OpenGlProgramCreator;
import com.tancorikworld.spacesurvivor.core.graphicscorelib.models.IFigure;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static com.tancorikworld.spacesurvivor.core.graphicscorelib.data.OpenGlProgramCreator.ATTRIBUTE_VERTEX_POSITION_NAME;
import static com.tancorikworld.spacesurvivor.core.graphicscorelib.data.OpenGlProgramCreator.FRAGMENT_COLOR_NAME;
import static com.tancorikworld.spacesurvivor.core.graphicscorelib.data.OpenGlProgramCreator.VERTEX_MATRIX_NAME;

/**
 * Базовый абстрактный класс для отрисовки простой фигуры, о текстурах тут и слуху нет.
 *
 * @author tancorik on 11.01.2020
 */
public abstract class BaseNoTextureFigure implements IFigure {

    private static final int SIMPLE_POINT_SIZE = 2;
    private static final int NO_SCALE = 1;

    private final int mProgram;
    private final FloatBuffer mVertexData;

    /**
     * Массив содержит значения компонентов цвета
     */
    protected final float[] mColor;

    /**
     * указатель на переменную цвета в программе
     */
    int mColorLocation;
    int mVertexCount;

    /**
     * Публичный конструктор
     *
     * @param vertices2d набор координат вершин для фигуры, должне содержать XY - координаты
     * @param color      массив цвета (RGBW)
     */
    BaseNoTextureFigure(@NonNull float[] vertices2d, @NonNull float[] color) {
        mVertexCount = vertices2d.length/SIMPLE_POINT_SIZE;
        mColor = color;

        mProgram = OpenGlProgramCreator.createSimpleProgramId();

        mVertexData = ByteBuffer.allocateDirect(vertices2d.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mVertexData.put(vertices2d);
        mVertexData.position(0);
    }

    @Override
    public void draw(float[] vMatrix, float[] pMatrix, float xPosition, float yPosition, int angle) {
        this.draw(vMatrix, pMatrix, xPosition, yPosition, angle, NO_SCALE);
    }

    @Override
    public void draw(float[] vMatrix, float[] pMatrix, float xPosition, float yPosition, int angle, float xyScale) {
        this.draw(vMatrix, pMatrix, xPosition, yPosition, angle, xyScale, xyScale);
    }

    @Override
    public void draw(float[] vMatrix, float[] pMatrix, float xPosition, float yPosition, int angle, float xScale, float yScale) {
        GLES20.glUseProgram(mProgram);

        // получить ссылки на необходимые параметры программы
        int verticesLocation = GLES20.glGetAttribLocation(mProgram, ATTRIBUTE_VERTEX_POSITION_NAME);
        mColorLocation = GLES20.glGetUniformLocation(mProgram, FRAGMENT_COLOR_NAME);
        int vPMatrixHandle = GLES20.glGetUniformLocation(mProgram, VERTEX_MATRIX_NAME);

        // включить массив точек
        GLES20.glEnableVertexAttribArray(verticesLocation);
        GLES20.glVertexAttribPointer(verticesLocation, SIMPLE_POINT_SIZE, GLES20.GL_FLOAT, false, 0, mVertexData);

        // единичим матрицу модели
        float[] modelMatrix = new float[16];
        Matrix.setIdentityM(modelMatrix, 0);

        // применяем необходимые изменения к модельке
        Matrix.translateM(modelMatrix, 0, xPosition, yPosition, 0); // по оси Z не переносим
        Matrix.scaleM(modelMatrix, 0, xScale, yScale, 1f); // по оси Z не скалируем
        Matrix.rotateM(modelMatrix, 0, angle, 0,0, 1); // поворот только по оси Z

        // складываем матрицы в одну, учитывая матрицу view и матрицу проекции
        float[] scratch = new float[16];
        Matrix.multiplyMM(scratch, 0, vMatrix, 0, modelMatrix, 0);
        Matrix.multiplyMM(scratch, 0, pMatrix, 0, scratch, 0);

        // применить суммарную матрицу
        GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, scratch, 0);

        // собственно рисование фигуры, оставим наследникам
        drawSelf();

        // выключить массив точек
        GLES20.glDisableVertexAttribArray(verticesLocation);
    }

    /**
     * Реализация самого рисования фигуры
     */
    protected abstract void drawSelf();
}
