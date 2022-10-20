package com.kirill;

import com.kirill.Point2d;

public class Point2dExample {

    public static void main(String[] args) {
        Point2d myPoint = new Point2d ();
        Point2d myOtherPoint = new Point2d (5,3);
        Point2d aThirdPoint = new Point2d ();

        System.out.println(myPoint == aThirdPoint);
        System.out.println("X:" + myOtherPoint.getxCoord() + " Y:" + myOtherPoint.getyCoord());
    }
}
