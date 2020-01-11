package com.tancorikworld.spacesurvivor.core.graphicscorelib.data;

import android.opengl.GLES20;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;

/**
 * Класс для создания выполняемой программы OpenGL ES
 * Может создать программу из шейдеров включащих текстурные параметры или без текстных параметров, но матрица положения есть обязательно.
 *
 * @author Karpachev Aleksandr on 04.08.2019
 */
public class OpenGlProgramCreator {

    public static final String ATTRIBUTE_VERTEX_POSITION_NAME = "a_Position";
    public static final String ATTRIBUTE_VERTEX_TEXTURE_NAME = "a_Texture";
    public static final String FRAGMENT_TEXTURE_UNIT_NAME = "u_TextureUnit";
    public static final String VERTEX_MATRIX_NAME = "u_Matrix";
    public static final String FRAGMENT_COLOR_NAME = "u_Color";

    //todo надо подумать и может убрать это страшное дело и загружать из файлов, притом файлы уже готовы
    private static final String SIMPLE_VERTEX_SHADER_CONTEXT = "uniform mat4 " + VERTEX_MATRIX_NAME + ";\n" +
            "attribute vec4 " + ATTRIBUTE_VERTEX_POSITION_NAME + ";\n" +
            "\n" +
            "void main() {\n" +
            "    gl_Position = " + VERTEX_MATRIX_NAME + " * " + ATTRIBUTE_VERTEX_POSITION_NAME + ";\n" +
            "}";

    private static final String SIMPLE_FRAGMENT_SHADER_CONTEXT = "precision mediump float;\n" +
            "uniform vec4 " + FRAGMENT_COLOR_NAME + ";\n" +
            "\n" +
            "void main() {\n" +
            "    gl_FragColor = " + FRAGMENT_COLOR_NAME + ";\n" +
            "}";

    private static final String TEXTURED_VERTEX_SHADER_CONTEXT = "attribute vec4 " + ATTRIBUTE_VERTEX_POSITION_NAME + ";\n" +
            "uniform mat4 "+ VERTEX_MATRIX_NAME +";\n" +
            "attribute vec2 "+ ATTRIBUTE_VERTEX_TEXTURE_NAME + ";\n" +
            "varying vec2 v_Texture;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    gl_Position = " + VERTEX_MATRIX_NAME + " * " + ATTRIBUTE_VERTEX_POSITION_NAME + ";\n" +
            "    v_Texture = " + ATTRIBUTE_VERTEX_TEXTURE_NAME + ";\n" +
            "}";
    private static final String TEXTURED_FRAGMENT_SHADER_CONTEXT = "precision mediump float;\n" +
            "\n" +
            "uniform sampler2D " + FRAGMENT_TEXTURE_UNIT_NAME + ";\n" +
            "varying vec2 v_Texture;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    gl_FragColor = texture2D(" + FRAGMENT_TEXTURE_UNIT_NAME + ", v_Texture);\n" +
            "}";

    /**
     * Создать выполняемую программу OpenGl
     *
     * @param isTexturedShader  будет ли использоваться текстурный шейдер
     * @return  номер исполняемой программы OpenGL
     */
    public static int createProgram(boolean isTexturedShader) {
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

    public static int createSimpleProgramId() {
        int programId = GLES20.glCreateProgram();
        if (programId == 0) {
            return 0;
        }
        GLES20.glAttachShader(programId, getShaderId(GLES20.GL_VERTEX_SHADER, SIMPLE_VERTEX_SHADER_CONTEXT));
        GLES20.glAttachShader(programId, getShaderId(GLES20.GL_FRAGMENT_SHADER, SIMPLE_FRAGMENT_SHADER_CONTEXT));

        GLES20.glLinkProgram(programId);
        int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(programId, GLES20.GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] == 0) {
            GLES20.glDeleteProgram(programId);
            return 0;
        }
        return programId;
    }

    private static int getShader(int type, boolean isTexturedShader) {
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

    private static int getShaderId(int type, String shader) {
        int shaderId = GLES20.glCreateShader(type);
        if (shaderId == 0) {
            return 0;
        }
        GLES20.glShaderSource(shaderId, shader);
        GLES20.glCompileShader(shaderId);

        int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shaderId, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
        if (compileStatus[0] == 0) {
            GLES20.glDeleteShader(shaderId);
            return 0;
        }
        return shaderId;
    }

    @NonNull
    private static String getShaderContext(int type, boolean isTexturedShader) {
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
