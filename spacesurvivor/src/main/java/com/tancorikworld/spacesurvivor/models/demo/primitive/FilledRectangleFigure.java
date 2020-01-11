package com.tancorikworld.spacesurvivor.models.demo.primitive;

import androidx.annotation.NonNull;

import com.tancorikworld.spacesurvivor.models.demo.helpers.SimpleColor;
import com.tancorikworld.spacesurvivor.models.demo.primitive.abstraction.BaseFilledFigure;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Прямоугольник залитый определнным цветом с центром в центре координат
 *
 * @author tancorik
 */
public class FilledRectangleFigure extends BaseFilledFigure {

    private static final int RECTANGLE_VERTEX_COUNT = 4;

    private final FloatBuffer mVertexData;

    /**
     * Конструктор
     *
     * @param program     номер выполняемой программы, которая обязательно должна быть с шейдерами без текстур
     * @param simpleColor цвет фигуры
     * @param width       ширина прямоугольника
     * @param height      высота прямоугольника
     */
    public FilledRectangleFigure(float width, float height, int program, @NonNull SimpleColor simpleColor) {
        super(program, simpleColor);
        float[] vertexes = calculateByWidthHeight(width, height);
        mVertexData = ByteBuffer.allocateDirect(vertexes.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mVertexData.put(vertexes);
        mVertexData.position(0);
    }

    @Override
    protected int getVertexCount() {
        return RECTANGLE_VERTEX_COUNT;
    }

    @NonNull
    @Override
    protected FloatBuffer getVertexData() {
        return mVertexData;
    }

    private float[] calculateByWidthHeight(float width, float height) {
        return new float[] {
                -width/2, -height/2,
                width/2, -height/2,
                width/2, height/2,
                -width/2, height/2
        };
    }
}
