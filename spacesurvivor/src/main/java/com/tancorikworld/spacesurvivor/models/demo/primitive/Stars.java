package com.tancorikworld.spacesurvivor.models.demo.primitive;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.tancorikworld.spacesurvivor.models.demo.primitive.abstraction.IPrimitiveFigure;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

import static com.tancorikworld.spacesurvivor.core.graphicscorelib.data.OpenGlProgramCreator.ATTRIBUTE_VERTEX_POSITION_NAME;
import static com.tancorikworld.spacesurvivor.core.graphicscorelib.data.OpenGlProgramCreator.FRAGMENT_COLOR_NAME;
import static com.tancorikworld.spacesurvivor.core.graphicscorelib.data.OpenGlProgramCreator.VERTEX_MATRIX_NAME;

public class Stars implements IPrimitiveFigure {

    private int mStarsCount = 10;
    private final int mProgram;
    private final float[] mColor = new float[]{0,1,0,1};
    private int mVertexCount = 5;

    private FloatBuffer mVertexData;
    private ShortBuffer mIndexBuffer;

    public Stars(int program) {
        mProgram = program;

        short[] indexArray = {0, 1,2,3,4,5,6,7,8,9,10,11,12,14,15,16};
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(2 * indexArray.length);
        byteBuffer.order(ByteOrder.nativeOrder());
        mIndexBuffer = byteBuffer.asShortBuffer();
        mIndexBuffer.put(indexArray);
        mIndexBuffer.position(0);
    }

    @Override
    public void draw(float[] vMatrix, float[] pMatrix, float xPosition, float yPosition, int angle, float xyScale) {
        if (mVertexData == null) {
            // float[] vertices = calculateStars(50, 10, 4, -50, 50);
            float[] vertices = calculateLotStars(mStarsCount,15, 3, 100);
            mVertexCount = 202;
            mVertexData = ByteBuffer.allocateDirect(vertices.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
            mVertexData.put(vertices);
            mVertexData.position(0);
        }
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

        // рисовать многоугольник
        // GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, mVertexCount);
        // GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, mVertexCount, mVertexCount);

        for (int i = 0; i < mStarsCount; i++ ) {
            // if (i == 10) {
            //
            // }
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, mVertexCount * i, mVertexCount);
        //     GLES20.glDrawElements(GLES20.GL_TRIANGLE_FAN, 17, GLES20.GL_UNSIGNED_SHORT, mIndexBuffer);
        }



        // выключить массив точек
        GLES20.glDisableVertexAttribArray(verticesLocation);
    }

    private float[] calculateLotStars(int starsCount, float radius, float innerRadius, int starsVertex) {
        List<float[]> first = new ArrayList<>();

        for (int i = 0; i < starsCount; i++) {
            first.add(calculateStars(radius, innerRadius, starsVertex, (float) Math.random() * 640, (float) Math.random() * 350));
        }
        // first.add(calculateStars(radius, innerRadius, starsVertex, 50, 50));
        // first.add(calculateStars(radius, innerRadius, starsVertex, 100, 100));

        int vertex = first.get(0).length;
        float[] result = new float[vertex * first.size()];
        for (int i = 0; i < first.size(); i ++) {
            for (int u = 0; u < vertex; u ++) {
                result[vertex * i + u] = first.get(i)[u];
            }
        }
        return result;
    }

    private float[] calculateStars(float radius, float innerRadius, int starsVertex, float xN, float yN) {
        int countVertex = starsVertex * 2;
        float specificRadius;
        float[] result = new float[countVertex * 2 + 4];
        // первая точка в начале координат
        result[0] = xN;
        result[1] = yN;
        for (int i = 0; i < countVertex; i++) {
            double angle = 2 * Math.PI / countVertex * i;
            specificRadius = i % 2f == 0 ? radius : innerRadius;
            double x = specificRadius * Math.cos(angle);
            int sign = angle >= Math.PI ? -1 : 1;
            double y = sign * Math.sqrt(Math.pow(specificRadius, 2) - Math.pow(x, 2));
            result[2 + i * 2] = (float) (x + xN);
            result[2 + i * 2 + 1] = (float) (y + yN);
            // последняя точка звезды должна совпадать с первой (не нулевой)
            if (i == 0) {
                result[result.length - 2] = (float) (x + xN);
                result[result.length - 1] = (float) (y + yN);
            }
        }
        return result;
    }

    private void calculateCoordinate() {

        // float maxX = 640;
        // float maxY = 300;
        // for (int i = 0; i < mStarsCount; i++) {
        //     mXYList.add(new CoordinatesXY((float) Math.random() * maxX, (float) Math.random() * maxY));
        // }

    }
}
