package com.tancorikworld.spacesurvivor.utils;

/**
 * Утилитный класс калькулятора двумерных [x, y]координат вершин по входным параметрам.
 *
 * @author tancorik
 */
public final class VertexCalculator2D {

    private static final int FLOAT_SIZE = 4;

    private VertexCalculator2D() {
        throw new RuntimeException();
    }

    /**
     * Расчитать массив с координатами с заданными шириной и высотой (центр фигуры в центре координат)
     *
     * @param width  ширина фигуры
     * @param height высота фигуры
     * @return массив с координатами
     */
    public float[] calculateByWidthHeight(float width, float height) {
        return new float[] {
                -width/2, -height/2,
                width/2, -height/2,
                width/2, height/2,
                -width/2, height/2
        };
    }

    /**
     * Расчитать массив с координатами фигуры звезды
     *
     * @param radius      внешний радиус
     * @param innerRadius внутренний радиус
     * @param starsVertex количество вершин звезды
     * @return массив с координатами
     */
    public float[] calculateStars(float radius, float innerRadius, int starsVertex) {
        int countVertex = starsVertex * 2;
        float specificRadius;
        float [] result = new float[countVertex * 2 + FLOAT_SIZE];
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
            // последняя точка звезды должна совпадать с первой, которая не в центре звезды.
            if (i == 0) {
                result[result.length - 2] = (float) x;
                result[result.length - 1] = (float) y;
            }
        }
        return result;
    }
}
