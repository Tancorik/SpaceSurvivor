package com.tancorikworld.spacesurvivor.core.graphicscorelib.models.textured;

import com.tancorikworld.spacesurvivor.core.graphicscorelib.models.IFigure;

public abstract class BaseTexturedFIgure implements IFigure {


    @Override
    public void draw(float[] vMatrix, float[] pMatrix, float xPosition, float yPosition, int angle) {
        this.draw(vMatrix, pMatrix, xPosition, yPosition, angle, 1);
    }

    @Override
    public void draw(float[] vMatrix, float[] pMatrix, float xPosition, float yPosition, int angle, float xyScale) {
        this.draw(vMatrix, pMatrix, xPosition, yPosition, angle, xyScale, xyScale);
    }

    @Override
    public void draw(float[] vMatrix, float[] pMatrix, float xPosition, float yPosition, int angle, float xScale, float yScale) {

    }
}
