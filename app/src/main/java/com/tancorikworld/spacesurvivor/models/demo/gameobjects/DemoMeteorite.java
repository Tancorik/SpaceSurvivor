package com.tancorikworld.spacesurvivor.models.demo.gameobjects;

import androidx.annotation.NonNull;

import com.tancorikworld.spacesurvivor.data.OpenGlProgramCreator;
import com.tancorikworld.spacesurvivor.models.demo.gameobjects.abstractions.GameObject;
import com.tancorikworld.spacesurvivor.models.demo.helpers.SimpleColor;
import com.tancorikworld.spacesurvivor.models.demo.primitive.ColoredFigure;
import com.tancorikworld.spacesurvivor.models.demo.primitive.IPrimitiveFigure;

public class DemoMeteorite extends GameObject {

    private final IPrimitiveFigure mFigure;

    protected DemoMeteorite(@NonNull OpenGlProgramCreator programCreator,
                            float currentPositionX,
                            float currentPositionY,
                            int angle) {
        super(programCreator, currentPositionX, currentPositionY, angle);
        mFigure = new ColoredFigure(10, 18, new SimpleColor(0, 1, 0), programCreator);
    }

    @Override
    public void redraw(float[] vMatrix, float[] pMatrix) {
        mFigure.draw(vMatrix, pMatrix, mCurrentPositionX, mCurrentPositionY, mAngle, 1);
    }
}
