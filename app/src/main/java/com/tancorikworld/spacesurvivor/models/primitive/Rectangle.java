package com.tancorikworld.spacesurvivor.models.primitive;

import android.opengl.GLES20;
import android.opengl.Matrix;

import androidx.annotation.NonNull;

import com.tancorikworld.spacesurvivor.data.OpenGlProgramCreator;
import com.tancorikworld.spacesurvivor.models.helpers.TextureArea;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Rectangle implements IPrimitiveFigure {

    private final int mProgram;
    private final int mTexture;
    private final FloatBuffer mVertexData;
    private final float[] mModelMatrix = new float[16];


    public Rectangle(int width,
                        int height,
                        int texture,
                        @NonNull TextureArea textureArea,
                        OpenGlProgramCreator programCreator) {
        mTexture = texture;
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

    @Override
    public void draw(float[] vMatrix, float[] pMatrix, int xPosition, int yPosition, int angle, float xyScale) {
        GLES20.glUseProgram(mProgram);


        int vertPosition = GLES20.glGetAttribLocation(mProgram, "a_Position");
        int textPosition = GLES20.glGetAttribLocation(mProgram, "a_Texture");
        int textUnitPosition = GLES20.glGetUniformLocation(mProgram, "u_TextureUnit");
        int matrixPosition = GLES20.glGetUniformLocation(mProgram, "u_Matrix");


        Matrix.setIdentityM(mModelMatrix, 0);

        Matrix.translateM(mModelMatrix, 0, xPosition, yPosition, 0);
        Matrix.scaleM(mModelMatrix, 0, xyScale, xyScale, 1f);

        Matrix.rotateM(mModelMatrix, 0, angle, 0,0, 1);

        float[] scratch = new float[16];
        Matrix.multiplyMM(scratch, 0, vMatrix, 0, mModelMatrix, 0);
        Matrix.multiplyMM(scratch, 0, pMatrix, 0, scratch, 0);


        // Matrix.setRotateM(rotationMatrix, 0, angle, 0, 0, -2.0f);


        // float[] scratch = new float[16];
        // Matrix.multiplyMM(scratch, 0, vPMatrix, 0, rotationMatrix, 0);



        GLES20.glUniformMatrix4fv(matrixPosition, 1, false, scratch, 0);


        mVertexData.position(0);
        GLES20.glVertexAttribPointer(vertPosition, 2, GLES20.GL_FLOAT, false, 16, mVertexData);
        GLES20.glEnableVertexAttribArray(vertPosition);

        mVertexData.position(2);
        GLES20.glVertexAttribPointer(textPosition, 2, GLES20.GL_FLOAT, false, 16, mVertexData);
        GLES20.glEnableVertexAttribArray(textPosition);


        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexture);

        GLES20.glUniform1i(textUnitPosition, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4);


        GLES20.glDisableVertexAttribArray(vertPosition);
        GLES20.glDisableVertexAttribArray(textPosition);
    }
}
