package com.tancorikworld.spacesurvivor.core.graphicscorelib.models;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.tancorikworld.spacesurvivor.core.graphicscorelib.data.OpenGlProgramCreator;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static com.tancorikworld.spacesurvivor.core.graphicscorelib.data.OpenGlProgramCreator.ATTRIBUTE_VERTEX_POSITION_NAME;
import static com.tancorikworld.spacesurvivor.core.graphicscorelib.data.OpenGlProgramCreator.FRAGMENT_COLOR_NAME;
import static com.tancorikworld.spacesurvivor.core.graphicscorelib.data.OpenGlProgramCreator.VERTEX_MATRIX_NAME;

// todo удалить этот класс нафиг как только можно будет
public class DemoFigure {

    private final int mProgram;
    private float[] mColor;
    private final int mVertexCount;
    private final FloatBuffer mVertexData;

    public DemoFigure() {
        mVertexCount = 4;
        float[] vertices = new float[mVertexCount * 2];
        vertices = new float[]{0f,0f, 100f, 100f, 50, -50, -50, 50};


        mColor = new float[]{1f, 1f, 0f, 1f};

        // mProgram = OpenGlProgramCreator.createSimpleProgramId();
        mProgram = OpenGlProgramCreator.createProgram(false);

        mVertexData = ByteBuffer.allocateDirect(mVertexCount * 2 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mVertexData.put(vertices);
        mVertexData.position(0);
    }

    public void draw(float[] vMatrix, float[] pMatrix, float xPosition, float yPosition, int angle, float xyScale) {
        GLES20.glUseProgram(mProgram);

        // получить ссылки на необходимые параметры программы
        int verticesLocation = GLES20.glGetAttribLocation(mProgram, ATTRIBUTE_VERTEX_POSITION_NAME);
        int colorLocation = GLES20.glGetUniformLocation(mProgram, FRAGMENT_COLOR_NAME);
        int vPMatrixHandle = GLES20.glGetUniformLocation(mProgram, VERTEX_MATRIX_NAME);

        // включить массив точек
        GLES20.glEnableVertexAttribArray(verticesLocation);
        GLES20.glVertexAttribPointer(verticesLocation, 2, GLES20.GL_FLOAT, false, 0, mVertexData);

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
        // GLES20.glUniform4f(colorLocation, 1,0,1, 1);

        GLES20.glLineWidth(2f);
        // рисовать многоугольник
        GLES20.glDrawArrays(GLES20.GL_LINES, 0, 4);

        // выключить массив точек
        GLES20.glDisableVertexAttribArray(verticesLocation);

    }
}
