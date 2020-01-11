package com.tancorikworld.spacesurvivor.models.demo.primitive;

import androidx.annotation.NonNull;

import com.tancorikworld.spacesurvivor.models.demo.helpers.SimpleColor;
import com.tancorikworld.spacesurvivor.models.demo.primitive.abstraction.BaseFilledFigure;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Звезда закрашенная с центром в начале координат
 *
 * @author tancorik
 */
public class FilledStarFigure extends BaseFilledFigure {

    private final FloatBuffer mVertexData;
    private final int mVertexCount;

    /**
     * Конструктор
     *
     * @param radius      размер звезды
     * @param innerRadius радиус внутренних углов
     * @param vertexCount количество вершин звезды
     * @param program     номер выполняемой программы, которая обязательно должна быть с шейдерами без текстур
     * @param simpleColor цвет фигуры
     */
    public FilledStarFigure(int radius, int innerRadius, int vertexCount, int program, @NonNull SimpleColor simpleColor) {
        super(program, simpleColor);
        float[] vertices = calculateStars(radius, innerRadius, vertexCount);
        mVertexCount = vertices.length /2;
        mVertexData = ByteBuffer.allocateDirect(vertices.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mVertexData.put(vertices);
        mVertexData.position(0);
    }

    @Override
    public void draw(@NonNull float[] vMatrix, @NonNull float[] pMatrix, float xPosition, float yPosition, int angle, float xyScale) {
        super.draw(vMatrix, pMatrix, xPosition, yPosition, angle, xyScale);
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

    private float[] calculateStars(float radius, float innerRadius, int starsVertex) {
        int countVertex = starsVertex * 2;
        float specificRadius;
        float [] result = new float[countVertex * 2 + 4];
        // первая точка в начале координат
        result[0] = 0;
        result[1] = 0;
        for (int i = 0; i < countVertex; i++) {
            double angle =  2 * Math.PI/countVertex * i;
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
}
