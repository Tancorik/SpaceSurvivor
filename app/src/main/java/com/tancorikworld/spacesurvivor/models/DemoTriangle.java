package com.tancorikworld.spacesurvivor.models;

import android.opengl.GLES20;

import com.tancorikworld.spacesurvivor.R;
import com.tancorikworld.spacesurvivor.utils.ShaderUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class DemoTriangle {

    private final int mProgramId;
    private final FloatBuffer mVertexData;
    private final float[] mColor;

    public DemoTriangle(float x1, float y1, float x2, float y2, float x3, float y3, float red, float green, float blue) {
        mColor = new float[]{red, green, blue, 1f};
        float[] vertices = new float[]{x1, y1, x2, y2, x3, y3};
        mVertexData = ByteBuffer.allocateDirect(vertices.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mVertexData.put(vertices);
        mVertexData.position(0);

        int vertexShaderId = ShaderUtils.createShader(GLES20.GL_VERTEX_SHADER, R.raw.simple_vertex_shader);
        int fragmentShaderId = ShaderUtils.createShader(GLES20.GL_FRAGMENT_SHADER, R.raw.simple_fragment_shader);
        mProgramId = ShaderUtils.createProgram(vertexShaderId, fragmentShaderId);
    }

    public void draw(float[] mvpMatrix) {
        GLES20.glUseProgram(mProgramId);

        int verticesLocation = GLES20.glGetAttribLocation(mProgramId, "a_Position");
        GLES20.glEnableVertexAttribArray(verticesLocation);
        GLES20.glVertexAttribPointer(verticesLocation, 2, GLES20.GL_FLOAT, false, 0, mVertexData);

        int colorLocation = GLES20.glGetUniformLocation(mProgramId, "u_Color");
        GLES20.glUniform4fv(colorLocation, 1, mColor, 0);


        int vPMatrixHandle = GLES20.glGetUniformLocation(mProgramId, "uMVPMatrix");

        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, mvpMatrix, 0);

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(verticesLocation);

    }
}
