package com.tancorikworld.spacesurvivor.models.demo.primitive;

import androidx.annotation.NonNull;

import com.tancorikworld.spacesurvivor.models.demo.helpers.SimpleColor;
import com.tancorikworld.spacesurvivor.models.demo.primitive.abstraction.BaseFilledFigure;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class FilledSomeAngleFigure extends BaseFilledFigure {

    private final int mVertexCount;
    private final FloatBuffer mVertexData;

    /**
     * Конструктор
     *
     * @param radius      радиус фигуры
     * @param vertexCount количество углов у фигуры
     * @param program     номер выполняемой программы, которая обязательно должна быть с шейдерами без текстур
     * @param simpleColor цвет фигуры
     */
    public FilledSomeAngleFigure(int radius, int vertexCount, int program, @NonNull SimpleColor simpleColor) {
        super(program, simpleColor);
        float[] vertexes = calculateVertexArray(radius, vertexCount);
        mVertexCount = vertexCount;
        mVertexData = ByteBuffer.allocateDirect(vertexes.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mVertexData.put(vertexes);
        mVertexData.position(0);
    }

    @Override
    protected int getVertexCount() {
        return mVertexCount;
    }

    @NonNull
    @Override
    protected FloatBuffer getVertexData() {
        return mVertexData;
    }

    // создать массив координат фигуры
    private float[] calculateVertexArray(int radius, int vertexCount) {
        float[] result = new float[vertexCount * 2];
        for (int i = 0; i < vertexCount; i++) {
            double angle =  2 * Math.PI/vertexCount * i;
            double x = radius * Math.cos(angle);
            int sign = angle >= Math.PI ? -1 : 1;
            double y = sign * Math.sqrt(radius * radius - x * x);
            result[i * 2] = (float) x;
            result[i * 2 + 1] = (float) y;
        }
        return result;
    }
}
