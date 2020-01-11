package com.tancorikworld.spacesurvivor.models.demo.primitive;

import android.opengl.GLES20;
import android.opengl.Matrix;

import androidx.annotation.NonNull;

import com.tancorikworld.spacesurvivor.models.demo.helpers.CoordinatesXY;
import com.tancorikworld.spacesurvivor.models.demo.helpers.SimpleColor;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

import static com.tancorikworld.spacesurvivor.core.graphicscorelib.data.OpenGlProgramCreator.ATTRIBUTE_VERTEX_POSITION_NAME;
import static com.tancorikworld.spacesurvivor.core.graphicscorelib.data.OpenGlProgramCreator.FRAGMENT_COLOR_NAME;
import static com.tancorikworld.spacesurvivor.core.graphicscorelib.data.OpenGlProgramCreator.VERTEX_MATRIX_NAME;

public class TestForStars {

    private static final int VERTEX_AXIS = 2;

    private final List<CoordinatesXY> mXYList = new ArrayList<>();
    private final int mProgram;
    private final float[] mColor = new float[4];
    private final FloatBuffer mVertexData;
    private final int mVertexCount;
    private final int mStarsCount;
    private ShortBuffer mIndexBuffer;

    public TestForStars(int radius, int innerRadius, int vertexCount, int starsCount, int program, @NonNull SimpleColor simpleColor) {
        mColor[0] = simpleColor.getRed();
        mColor[1] = simpleColor.getGreen();
        mColor[2] = simpleColor.getBlue();
        mColor[3] = simpleColor.getAlpha();
        mStarsCount = starsCount;
        mProgram = program;
        calculateCoordinate();
        float[] vertices = calculateStars(radius, innerRadius, vertexCount);
        mVertexCount = vertexCount * 2 + 2;
        mVertexData = ByteBuffer.allocateDirect(vertices.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mVertexData.put(vertices);
        mVertexData.position(0);

        short[] indexArray = {0, 1,2,3,4,5,6,7,8,9};
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(2 * indexArray.length);
        byteBuffer.order(ByteOrder.nativeOrder());
        mIndexBuffer = byteBuffer.asShortBuffer();
        mIndexBuffer.put(indexArray);
        mIndexBuffer.position(0);
    }

    public void draw(@NonNull float[] vMatrix, @NonNull float[] pMatrix, List<CoordinatesXY> coordList) {
        GLES20.glUseProgram(mProgram);

        // получить ссылки на необходимые параметры программы
        int verticesLocation = GLES20.glGetAttribLocation(mProgram, ATTRIBUTE_VERTEX_POSITION_NAME);
        int colorLocation = GLES20.glGetUniformLocation(mProgram, FRAGMENT_COLOR_NAME);
        int vPMatrixHandle = GLES20.glGetUniformLocation(mProgram, VERTEX_MATRIX_NAME);

        // включить массив точек
        GLES20.glEnableVertexAttribArray(verticesLocation);
        GLES20.glVertexAttribPointer(verticesLocation, VERTEX_AXIS, GLES20.GL_FLOAT, false, 0, mVertexData);

        // единичим матрицу
        float[] modelMatrix = new float[16];
        for (int i = 0; i < coordList.size(); i++) {
            Matrix.setIdentityM(modelMatrix, 0);

            // применяем необходимые изменения к модельке
            Matrix.translateM(modelMatrix, 0, coordList.get(i).getValueX(), coordList.get(i).getValueY(), 0);
            Matrix.scaleM(modelMatrix, 0, 1, 1, 1f);
            Matrix.rotateM(modelMatrix, 0, 0, 0, 0, 1);

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
            // GLES20.glDrawElements(GLES20.GL_TRIANGLE_FAN, 10, GLES20.GL_UNSIGNED_SHORT, mIndexBuffer);


        }
        // выключить массив точек
        GLES20.glDisableVertexAttribArray(verticesLocation);
    }

    private float[] calculateStars(float radius, float innerRadius, int starsVertex) {
            int countVertex = starsVertex * 2;
            float specificRadius;
            float[] result = new float[countVertex * 2 + 4];
            // первая точка в начале координат
            result[0] = 0;
            result[1] = 0;
            for (int i = 0; i < countVertex; i++) {
                double angle = 2 * Math.PI / countVertex * i;
                specificRadius = i % 2f == 0 ? radius : innerRadius;
                double x = specificRadius * Math.cos(angle);
                int sign = angle >= Math.PI ? -1 : 1;
                double y = sign * Math.sqrt(Math.pow(specificRadius, 2) - Math.pow(x, 2));
                result[2 + i * 2] = (float) x;
                result[2 + i * 2 + 1] = (float) y;
                // последняя точка звезды должна совпадать с первой (не нулевой)
                if (i == 0) {
                    result[result.length - 2] = (float) x;
                    result[result.length - 1] = (float) y;
                }
            }
        return result;
    }

    private void calculateCoordinate() {
        float maxX = 640;
        float maxY = 300;
        for (int i = 0; i < mStarsCount; i++) {
            mXYList.add(new CoordinatesXY((float) Math.random() * maxX, (float) Math.random() * maxY));
        }
    }
}
