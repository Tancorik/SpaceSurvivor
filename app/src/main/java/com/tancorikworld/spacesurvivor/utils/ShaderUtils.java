package com.tancorikworld.spacesurvivor.utils;

import android.opengl.GLES20;

import com.tancorikworld.spacesurvivor.data.demo.RawFileUtils;

import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_LINK_STATUS;

public class ShaderUtils {
    public static int createProgram(int vertexShaderId, int fragmentShaderId) {
        final int programId = GLES20.glCreateProgram();
        if (programId == 0) {
            return 0;
        }
        GLES20.glAttachShader(programId, vertexShaderId);
        GLES20.glAttachShader(programId, fragmentShaderId);
        GLES20.glLinkProgram(programId);
        final int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(programId, GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] == 0) {
            GLES20.glDeleteProgram(programId);
            return 0;
        }
        return programId;
    }

    public static int createShader(int type, int shaderRawId) {
        String shaderText = RawFileUtils.readTextFormRaw(shaderRawId);
        return ShaderUtils.createShader(type, shaderText);
    }

    private static int createShader(int type, String shaderText) {
        final int shaderId = GLES20.glCreateShader(type);
        if (shaderId == 0) {
            return 0;
        }
        GLES20.glShaderSource(shaderId, shaderText);
        GLES20.glCompileShader(shaderId);
        final int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shaderId, GL_COMPILE_STATUS, compileStatus, 0);
        if (compileStatus[0] == 0) {
            GLES20.glDeleteShader(shaderId);
            return 0;
        }
        return shaderId;
    }
}
