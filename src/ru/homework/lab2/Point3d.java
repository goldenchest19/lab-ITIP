package com.kirill;

public class Point3d extends Point2d {
    private double zCoord;

    public Point3d(double xCoord, double yCoord, double zCoord) {
        super(xCoord, yCoord);
        this.zCoord = zCoord;
    }

    public Point3d() {
        this(0.0, 0.0, 0.0);
    }

    //  method for compare two points
    public boolean isCompare(Point3d newPoint) {
        return newPoint.getxCoord() == this.getxCoord() & newPoint.getyCoord() == this.getyCoord() &
                newPoint.getzCoord() == this.getzCoord();
    }

    public double distanceTo(Point3d secondPoint) {

//      calculate distance between points
        double result = Math.sqrt(
                (secondPoint.getxCoord() - this.getxCoord()) * (secondPoint.getxCoord() - this.getxCoord()) +
                        (secondPoint.getyCoord() - this.getyCoord()) * (secondPoint.getyCoord() - this.getyCoord()) +
                        (secondPoint.getzCoord() - this.getzCoord()) * (secondPoint.getzCoord() - this.getzCoord())
        );

//      rounding the side length to two decimal places
        String finalResult = String.format("%.2f", result);
        System.out.println(finalResult);
        return Double.parseDouble(finalResult);
    }

    public double getzCoord() {
        return zCoord;
    }

    public void setzCoord(double zCoord) {
        this.zCoord = zCoord;
    }
}
