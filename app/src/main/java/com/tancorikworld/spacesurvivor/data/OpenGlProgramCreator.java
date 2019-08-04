package com.tancorikworld.spacesurvivor.data;

import android.opengl.GLES20;

import androidx.annotation.NonNull;

/**
 * Класс для создания выполняемой программы OpenGL ES
 * Может создать программу из шейдеров включащих текстурные параметры или без текстных параметров, но матрица положения есть обязательно.
 *
 * @author Karpachev Aleksandr on 04.08.2019
 */
public class OpenGlProgramCreator {

    private static final String SIMPLE_VERTEX_SHADER_CONTEXT = "uniform mat4 uMVPMatrix;\n" +
            "attribute vec4 a_Position;\n" +
            "\n" +
            "void main() {\n" +
            "    gl_Position = uMVPMatrix * a_Position;\n" +
            "}";

    private static final String SIMPLE_FRAGMENT_SHADER_CONTEXT = "precision mediump float;\n" +
            "uniform vec4 u_Color;\n" +
            "\n" +
            "void main() {\n" +
            "    gl_FragColor = u_Color;\n" +
            "}";

    private static final String TEXTURED_VERTEX_SHADER_CONTEXT = "attribute vec4 a_Position;\n" +
            "uniform mat4 u_Matrix;\n" +
            "attribute vec2 a_Texture;\n" +
            "varying vec2 v_Texture;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    gl_Position = u_Matrix * a_Position;\n" +
            "    v_Texture = a_Texture;\n" +
            "}";
    private static final String TEXTURED_FRAGMENT_SHADER_CONTEXT = "precision mediump float;\n" +
            "\n" +
            "uniform sampler2D u_TextureUnit;\n" +
            "varying vec2 v_Texture;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    gl_FragColor = texture2D(u_TextureUnit, v_Texture);\n" +
            "}";

    /**
     * Создать выполняемую программу OpenGl
     *
     * @param isTexturedShader  будет ли использоваться текстурный шейдер
     * @return  номер исполняемой программы OpenGL
     */
    public int createProgram(boolean isTexturedShader) {
        int programId = GLES20.glCreateProgram();
        if (programId == 0) {
            return 0;
        }
        GLES20.glAttachShader(programId, getShader(GLES20.GL_VERTEX_SHADER, isTexturedShader));
        GLES20.glAttachShader(programId, getShader(GLES20.GL_FRAGMENT_SHADER, isTexturedShader));

        GLES20.glLinkProgram(programId);
        final int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(programId, GLES20.GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] == 0) {
            GLES20.glDeleteProgram(programId);
            return 0;
        }


        return programId;
    }

    private int getShader(int type, boolean isTexturedShader) {
        String shaderText = getShaderContext(type, isTexturedShader);
        int shaderId = GLES20.glCreateShader(type);
        if (shaderId == 0) {
            return 0;
        }
        GLES20.glShaderSource(shaderId, shaderText);
        GLES20.glCompileShader(shaderId);

        int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shaderId, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
        if (compileStatus[0] == 0) {
            GLES20.glDeleteShader(shaderId);
            return 0;
        }
        return shaderId;
    }

    private @NonNull String getShaderContext(int type, boolean isTexturedShader) {
        String result;
        if (GLES20.GL_VERTEX_SHADER == type) {
            result = isTexturedShader
                    ? TEXTURED_VERTEX_SHADER_CONTEXT
                    : SIMPLE_VERTEX_SHADER_CONTEXT;
        } else {
            result = isTexturedShader
                    ? TEXTURED_FRAGMENT_SHADER_CONTEXT
                    : SIMPLE_FRAGMENT_SHADER_CONTEXT;
        }
        return result;
    }
}
