package com.group0562.adventureofpost.shapeClicker;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.group0562.adventureofpost.Puzzles;


public class ShapeClicker extends Puzzles {
    /* bound indexes follow from left, right, up and down */
    static double[] bound;
    private Shape s_object;
    private Paint paint;
    private static String shape;
    private boolean within = false;
    private static boolean changed;

    ShapeClicker(long time, Paint p) {
        super(new ShapeClickerStats(time));
        this.paint = p;
//        puzzleStats = new ShapeClickerStats(time);
        s_object = new Circle(50, 50, this.paint);
        s_object.setLocation();
    }

    static void setBound(double[] bound) {
        ShapeClicker.bound = bound;
    }

    void checkWithinBall(double cursor_x, double cursor_y) {
        this.within = s_object.checkWithin(cursor_x, cursor_y);
        update();
        checkComplete();
        //if(this.within){
            //update();
        //    checkComplete();
        //}
    }

    void draw(Canvas canvas){
        checkChangedObject();
        s_object.draw(canvas);
    }

    public static void setShape(String type_of_shape){
        ShapeClicker.shape = type_of_shape;
        ShapeClicker.changed = true;
    }

    private void checkChangedObject(){
        if (ShapeClicker.changed){
            if (ShapeClicker.shape.equals("Circle")){
                s_object = new Circle(s_object.getCoordinate_x(), s_object.getCoordinate_y(), this.paint);
            }
            else if (ShapeClicker.shape.equals("Square")){
                s_object = new Square(s_object.getCoordinate_x(), s_object.getCoordinate_y(), this.paint);
            }
            else{
                s_object = new Triangle(s_object.getCoordinate_x(), s_object.getCoordinate_y(), this.paint);
            }
        }
        ShapeClicker.changed = false;
    }

    @Override
    public void update() {
        super.update();
        if (this.within) {
            this.puzzleStats.setPoints(1);
            notifyObservers();
            s_object.setLocation();
        }
        else {
            this.puzzleStats.setLives(1);
            s_object.setLocation();
        }
    }

    @Override
    public void checkComplete() {
        if (this.puzzleStats.getTime()<= 0) {
            this.setPuzzleComplete(true);
            this.end();
        }
        else if (this.puzzleStats.getPoints() >= 50) {
            this.setPuzzleComplete(true);
            this.puzzleStats.setPoints((int) this.puzzleStats.getTime()/1000);
            this.end();
        }
    }
    public int end(){
        return this.puzzleStats.getPoints();
    }
}
