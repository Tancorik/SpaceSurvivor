package com.tancorikworld.spacesurvivor.models;

import android.opengl.GLES20;

import com.tancorikworld.spacesurvivor.R;
import com.tancorikworld.spacesurvivor.application.SpaceApplication;
import com.tancorikworld.spacesurvivor.utils.ShaderUtils;
import com.tancorikworld.spacesurvivor.utils.TextureUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class DemoSimpleSquad {

    private final int mProgram;
    private final float[] mColor;
    private final FloatBuffer mVertexData;
    private final int mTexture;

    public DemoSimpleSquad(float x, float y, float width, float height, float red, float green, float blue) {
        mColor = new float[] {red, green, blue, 1.0f};
        float[] vertex = new float[]{
                x , y, 0, 1f,
                x + width, y, 1f, 1f,
                x +width, y + height, 1f, 0,
                x, y + height, 0, 0};

        mVertexData = ByteBuffer.allocateDirect(vertex.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mVertexData.put(vertex);

        int vertexShaderId = ShaderUtils.createShader(GLES20.GL_VERTEX_SHADER, R.raw.texture_vertex_shader);
        int fragmentShaderId = ShaderUtils.createShader(GLES20.GL_FRAGMENT_SHADER, R.raw.texture_fragment_shader);

        mProgram = ShaderUtils.createProgram(vertexShaderId, fragmentShaderId);

        mTexture = TextureUtils.loadTexture(SpaceApplication.getAppComponent().getContext(), R.drawable.demo_box);
    }


    public void draw(float[] vpMatrix) {
        GLES20.glUseProgram(mProgram);


        int vertPosition = GLES20.glGetAttribLocation(mProgram, "a_Position");
        int textPosition = GLES20.glGetAttribLocation(mProgram, "a_Texture");
        int textUnitPosition = GLES20.glGetUniformLocation(mProgram, "u_TextureUnit");
        int matrixPosition = GLES20.glGetUniformLocation(mProgram, "u_Matrix");

        GLES20.glUniformMatrix4fv(matrixPosition, 1, false, vpMatrix, 0);


        mVertexData.position(0);
        GLES20.glVertexAttribPointer(vertPosition, 2, GLES20.GL_FLOAT, false, 16, mVertexData);
        GLES20.glEnableVertexAttribArray(vertPosition);

        mVertexData.position(2);
        GLES20.glVertexAttribPointer(textPosition, 2, GLES20.GL_FLOAT, false, 16, mVertexData);
        GLES20.glEnableVertexAttribArray(textPosition);


        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexture);

        GLES20.glUniform1i(textUnitPosition, 0);

        // GLES20.glEnableVertexAttribArray(vertPosition);
        // GLES20.glVertexAttribPointer(vertPosition, 2, GLES20.GL_FLOAT, false, 0, mVertexData);
        //
        // int colorPosition = GLES20.glGetUniformLocation(mProgram, "u_Color");
        // GLES20.glUniform4fv(colorPosition, 1, mColor, 0);
        //
        // GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4);


        GLES20.glDisableVertexAttribArray(vertPosition);
        GLES20.glDisableVertexAttribArray(textPosition);


    }
}
