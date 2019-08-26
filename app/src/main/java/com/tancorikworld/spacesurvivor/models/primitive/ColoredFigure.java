package com.tancorikworld.spacesurvivor.models.primitive;

import android.opengl.GLES20;
import android.opengl.Matrix;

import androidx.annotation.NonNull;

import com.tancorikworld.spacesurvivor.data.OpenGlProgramCreator;
import com.tancorikworld.spacesurvivor.models.helpers.SimpleColor;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static com.tancorikworld.spacesurvivor.data.OpenGlProgramCreator.ATTRIBUTE_VERTEX_POSITION_NAME;
import static com.tancorikworld.spacesurvivor.data.OpenGlProgramCreator.FRAGMENT_COLOR_NAME;
import static com.tancorikworld.spacesurvivor.data.OpenGlProgramCreator.VERTEX_MATRIX_NAME;

/**
 * Цветная фигура с любым количеством вершин, от треугольника до круга с большим количеством точек
 *
 * @author Karpachev Aleksandr on 04.08.2019
 */
public class ColoredFigure implements IPrimitiveFigure {

    private final int mProgram;
    private final FloatBuffer mVertexData;
    private final float[] mColor;
    private final int mVertexCount;

    /**
     * Конструктор
     *
     * @param radius            радиус фигуры
     * @param vertexCount       количество вершин фигуры
     * @param color             цвет фигуры
     * @param programCreator    создатель выполняемой программы OpenGl
     */
    public ColoredFigure(int radius, int vertexCount, @NonNull SimpleColor color, @NonNull OpenGlProgramCreator programCreator) {
        mProgram = programCreator.createProgram(false);
        mColor = new float[] {color.getRed(), color.getGreen(), color.getBlue(), 1.0f};
        mVertexCount = vertexCount;

        float[] vertices = createVertexArray(radius);
        mVertexData = ByteBuffer.allocateDirect(vertices.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mVertexData.put(vertices);
        mVertexData.position(0);
    }

    @Override
    public void draw(float[] vMatrix, float[] pMatrix, float xPosition, float yPosition, int angle, float xyScale) {
        GLES20.glUseProgram(mProgram);

        // получить ссылки на необходимые параметры программы
        int verticesLocation = GLES20.glGetAttribLocation(mProgram, ATTRIBUTE_VERTEX_POSITION_NAME);
        int colorLocation = GLES20.glGetUniformLocation(mProgram, FRAGMENT_COLOR_NAME);
        int vPMatrixHandle = GLES20.glGetUniformLocation(mProgram, VERTEX_MATRIX_NAME);

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

        // рисовать многоугольник
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, mVertexCount);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(verticesLocation);
    }

    // создать массив координат фигуры
    private float[] createVertexArray(int radius) {
        float[] result = new float[mVertexCount * 2];
        for (int i = 0; i < mVertexCount; i++) {
            double angle =  2 * Math.PI/mVertexCount * i;
            double x = radius * Math.cos(angle);
            int sign = angle >= Math.PI ? -1 : 1;
            double y = sign * Math.sqrt(radius * radius - x * x);
            result[i * 2] = (float) x;
            result[i * 2 + 1] = (float) y;
        }
        return result;
    }
}
