package com.group0562.adventureofpost.shapeClicker;

import android.content.Context;
import android.graphics.Canvas;
import com.group0562.adventureofpost.AdventureOfPost;


public abstract class ShapeClicker{
    public ShapeClickerStats puzzleStats;
    private boolean puzzleComplete = false;
    static double[] bound;


    ShapeClicker(ShapeClickerStats statsInst) {
            puzzleStats = statsInst;
    }
    abstract void draw(Canvas canvas);
    abstract void checkWithinBall(double cursor_x, double cursor_y);
    public abstract void checkComplete();

    public void update() {
        checkComplete();

        if (puzzleStats.getTime() == 0 | puzzleComplete) {
            onStop();
        }
    }

    public void onStop() {
        if (!puzzleComplete) {
            puzzleStats.setLives(0);
        }
    }

    static void setBound(double[] bound) {
        ShapeClicker.bound = bound;
    }

    public void setPuzzleComplete(boolean b) {
        this.puzzleComplete = b;
    }

    public boolean getPuzzleComplete() {
        return this.puzzleComplete;
    }

    public void saveStats(Context context) {
        puzzleStats.saveStats(context);
    }

}
