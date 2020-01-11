package com.tancorikworld.spacesurvivor.models.demo.primitive;

import android.opengl.GLES20;
import android.opengl.Matrix;

import androidx.annotation.NonNull;

import com.tancorikworld.spacesurvivor.core.graphicscorelib.data.OpenGlProgramCreator;
import com.tancorikworld.spacesurvivor.models.demo.helpers.TextureArea;
import com.tancorikworld.spacesurvivor.models.demo.primitive.abstraction.IPrimitiveFigure;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static com.tancorikworld.spacesurvivor.core.graphicscorelib.data.OpenGlProgramCreator.ATTRIBUTE_VERTEX_POSITION_NAME;
import static com.tancorikworld.spacesurvivor.core.graphicscorelib.data.OpenGlProgramCreator.ATTRIBUTE_VERTEX_TEXTURE_NAME;
import static com.tancorikworld.spacesurvivor.core.graphicscorelib.data.OpenGlProgramCreator.FRAGMENT_TEXTURE_UNIT_NAME;
import static com.tancorikworld.spacesurvivor.core.graphicscorelib.data.OpenGlProgramCreator.VERTEX_MATRIX_NAME;

/**
 * Класс отвечающий за отрисовку прямоугольника с текстурой.
 *
 * @author Karpachev Aleksandr on 04.08.2019
 */
public class TexturedRectangle implements IPrimitiveFigure {

    private final int mProgram;
    private final int mTexture;
    private final FloatBuffer mVertexData;
    private final int mTextureUnit;

    /**
     * Конструктор
     *
     * @param width             ширина
     * @param height            высота
     * @param textureId         id текстуры
     * @param textureArea       класс с параметрами области текустуры из общего файла текстуры
     * @param programCreator    класс для создания выполняемой программы
     */
    public TexturedRectangle(int width,
                             int height,
                             int textureId,
                             int glTextureUnit,
                             @NonNull TextureArea textureArea,
                             @NonNull OpenGlProgramCreator programCreator) {
        mTexture = textureId;
        mTextureUnit = glTextureUnit;
        mProgram = programCreator.createProgram(true);
        float half_width = width/2f;
        float half_height = height/2f;
        float[] vertex = new float[] {
                -half_width, -half_height, textureArea.getBottom(), textureArea.getLeft(),
                half_width, -half_height, textureArea.getBottom(), textureArea.getRight(),
                half_width, half_height, textureArea.getTop(), textureArea.getRight(),
                -half_width, half_height, textureArea.getTop(), textureArea.getLeft()
        };

        mVertexData = ByteBuffer.allocateDirect(vertex.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mVertexData.put(vertex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void draw(float[] vMatrix, float[] pMatrix, float xPosition, float yPosition, int angle, float xyScale) {
        GLES20.glUseProgram(mProgram);

        // получаем ссылки на необходимые переменные в OpenGl программе
        int vertexPositionLocation = GLES20.glGetAttribLocation(mProgram, ATTRIBUTE_VERTEX_POSITION_NAME);
        int textureLocation = GLES20.glGetAttribLocation(mProgram, ATTRIBUTE_VERTEX_TEXTURE_NAME);
        int textureUnitLocation = GLES20.glGetUniformLocation(mProgram, FRAGMENT_TEXTURE_UNIT_NAME);
        int matrixLocation = GLES20.glGetUniformLocation(mProgram, VERTEX_MATRIX_NAME);

        // единичим матрицу
        float[] modelMatrix = new float[16];
        Matrix.setIdentityM(modelMatrix, 0);

        // применяем необходимые изменения к модельке
        Matrix.translateM(modelMatrix, 0, xPosition, yPosition, 0);
        Matrix.scaleM(modelMatrix, 0, xyScale, xyScale, 1f);
        Matrix.rotateM(modelMatrix, 0, angle, 0,0, 1);

        // складываем матрыцы в одну, учитывая матрицу view и матрицу проекции
        float[] scratch = new float[16];
        Matrix.multiplyMM(scratch, 0, vMatrix, 0, modelMatrix, 0);
        Matrix.multiplyMM(scratch, 0, pMatrix, 0, scratch, 0);

        // применяем полученную суммарную марицу
        GLES20.glUniformMatrix4fv(matrixLocation, 1, false, scratch, 0);

        // устанавливаем координаты для точек
        mVertexData.position(0);
        GLES20.glVertexAttribPointer(vertexPositionLocation, 2, GLES20.GL_FLOAT, false, 16, mVertexData);
        GLES20.glEnableVertexAttribArray(vertexPositionLocation);

        // устанавливем координаты тектуры
        mVertexData.position(2);
        GLES20.glVertexAttribPointer(textureLocation, 2, GLES20.GL_FLOAT, false, 16, mVertexData);
        GLES20.glEnableVertexAttribArray(textureLocation);

        // настраивем текстуру
        // GLES20.glActiveTexture(mTextureUnit);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexture);
        GLES20.glUniform1i(textureUnitLocation, 0);

        // рисуем
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4);

        // вырубаем массивы
        GLES20.glDisableVertexAttribArray(vertexPositionLocation);
        GLES20.glDisableVertexAttribArray(textureLocation);
    }
}
