package com.tancorikworld.spacesurvivor.models.demo.gameobjects;

import com.tancorikworld.spacesurvivor.models.demo.gameobjects.abstractions.GameObject;
import com.tancorikworld.spacesurvivor.models.demo.helpers.CoordinatesXY;
import com.tancorikworld.spacesurvivor.models.demo.helpers.SimpleColor;
import com.tancorikworld.spacesurvivor.models.demo.primitive.MoreTestForStars;
import com.tancorikworld.spacesurvivor.models.demo.primitive.TestForStars;

import java.util.ArrayList;
import java.util.List;

/**
 * Игровой объект звездного космоса. Задний фон для игрового поля.
 *
 * @author tancorik
 */
public class DemoStaticSpace extends GameObject {

    private static final int STARS_COUNT = 100;
    private static final int STARS_SIZE = 10;
    private static final int STARS_SECOND_SIZE = 4;
    private static final int STARS_VERTEX_COUNT = 20;

    // private final List<ISimpleFigure2D> mStars = new ArrayList<>(STARS_COUNT);
    private final TestForStars mTest;
    private final List<CoordinatesXY> mXYList = new ArrayList<>(STARS_COUNT);

    private int mSignature;
    private int mIndex;
    private float mScale;

    /**
     * Конструктор
     *
     * @param program исполняющая программа OpenGl
     */
    public DemoStaticSpace(int program) {
        super(program, 0, 0, 0);
        mScale = 0.1f;
        mIndex = 0;
        mSignature = 1;
        mTest = new TestForStars(STARS_SIZE, STARS_SECOND_SIZE, STARS_VERTEX_COUNT, STARS_COUNT, program, new SimpleColor(1,1,1));
        // for (int i = 0; i < STARS_COUNT; i++) {
        //     mStars.add(new FilledStarFigure(STARS_SIZE, STARS_SECOND_SIZE, STARS_VERTEX_COUNT, mProgram, new SimpleColor(1,1,1)));
        // }

    }

    @Override
    public void redraw(float[] vMatrix, float[] pMatrix) {
        if (mXYList.isEmpty()) {
            calculateCoordinate();
        }

        mScale = mScale + 0.08f * mSignature;
        if (mScale > 1) {
            mScale = 1f;
            mSignature = mSignature * -1;
            mIndex++;
            if (mIndex == STARS_COUNT) {
                mIndex = 0;
            }
        } else if (mScale < 0.1f) {
            mScale = 0.1f;
            mSignature = mSignature * -1;
            mXYList.set(mIndex,
                    new CoordinatesXY((float) Math.random() * mScreenSize.getValueX(), (float) Math.random() * mScreenSize.getValueY()));
        }

        mTest.draw(vMatrix, pMatrix, mXYList);
        // for (int i = 0; i < STARS_COUNT; i++) {
        //     if (i == mIndex) {
        //         mStars.get(i).draw(vMatrix, pMatrix, mXYList.get(i).getValueX(), mXYList.get(i).getValueY(), 0, mScale);
        //     } else {
        //         mStars.get(i).draw(vMatrix, pMatrix, mXYList.get(i).getValueX(), mXYList.get(i).getValueY(), 0, 1f);
        //
        //     }
        // }
    }

    private void calculateCoordinate() {
        float maxX = mScreenSize.getValueX();
        float maxY = mScreenSize.getValueY();
        for (int i = 0; i < STARS_COUNT; i++) {
            mXYList.add(new CoordinatesXY((float) Math.random() * maxX, (float) Math.random() * maxY));
        }
    }

}
