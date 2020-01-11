package com.tancorikworld.spacesurvivor.models.demo.gameobjects;

import androidx.annotation.NonNull;

import com.tancorikworld.spacesurvivor.core.graphicscorelib.data.OpenGlProgramCreator;
import com.tancorikworld.spacesurvivor.models.demo.gameobjects.abstractions.GameObject;
import com.tancorikworld.spacesurvivor.models.demo.helpers.SimpleColor;
import com.tancorikworld.spacesurvivor.models.demo.primitive.ColoredFigure;
import com.tancorikworld.spacesurvivor.models.demo.primitive.abstraction.IPrimitiveFigure;

/**
 * Демо стенка
 *
 * @author tancorik
 */
public class DemoWall extends GameObject {

    private final IPrimitiveFigure mWall;

    public DemoWall(int prog, @NonNull OpenGlProgramCreator programCreator, float currentPositionX, float currentPositionY, int angle) {
        super(prog, currentPositionX, currentPositionY, angle);
        mWall = new ColoredFigure(10f, 150f, new SimpleColor(0, 0, 1), prog);
    }

    @Override
    public void redraw(float[] vMatrix, float[] pMatrix) {
        mWall.draw(vMatrix, pMatrix, mCurrentPositionX, mCurrentPositionY, 0, 1);
        mWall.draw(vMatrix, pMatrix, mCurrentPositionX + 30, mCurrentPositionY, 0, 1);
    }
}
