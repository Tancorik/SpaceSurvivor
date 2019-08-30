package com.tancorikworld.spacesurvivor.models.demo.gameobjects;

import androidx.annotation.NonNull;

import com.tancorikworld.spacesurvivor.data.OpenGlProgramCreator;
import com.tancorikworld.spacesurvivor.models.demo.gameobjects.abstractions.GameObject;
import com.tancorikworld.spacesurvivor.models.demo.helpers.SimpleColor;
import com.tancorikworld.spacesurvivor.models.demo.primitive.ColoredFigure;
import com.tancorikworld.spacesurvivor.models.demo.primitive.IPrimitiveFigure;

/**
 * Демо стенка
 *
 * @author tancorik
 */
public class DemoWall extends GameObject {

    private final IPrimitiveFigure mWall;

    public DemoWall(@NonNull OpenGlProgramCreator programCreator, float currentPositionX, float currentPositionY, int angle) {
        super(programCreator, currentPositionX, currentPositionY, angle);
        mWall = new ColoredFigure(10f, 150f, new SimpleColor(0, 0, 1), programCreator);
    }

    @Override
    public void redraw(float[] vMatrix, float[] pMatrix) {
        mWall.draw(vMatrix, pMatrix, mCurrentPositionX, mCurrentPositionY, 0, 1);
    }
}
