package ru.homework.lab2;

public class Lab1 {

    public static void main(String[] args) {
        Point3d firstPoint = new Point3d(0, 0, 0);
        Point3d secondPoint = new Point3d(1, 2, 3);
        Point3d thirdPoint = new Point3d(1, 5, 3);

        //      test the first triangle
        System.out.println("Площадь первого треугольника: " + computeArea(firstPoint, secondPoint, thirdPoint));

        Point3d t1 = new Point3d(3, 3, 3);
        Point3d t2 = new Point3d(1, 2, 3);
        Point3d t3 = new Point3d(1, 5, 3);

//      test the second triangle
        System.out.println("Площадь второго треугольника: " + computeArea(t1, t2, t3));

        Point3d t4 = new Point3d(11, 12, 12);
        Point3d t5 = new Point3d(10, 10, 10);
        Point3d t6 = new Point3d(8, 5, 7);

//      test the third triangle
        System.out.println("Площадь третьего треугольника: " + computeArea(t4, t5, t6));

        Point3d t8 = new Point3d(10, 10, 10);
        Point3d t9 = new Point3d(10, 10, 10);
        Point3d t10 = new Point3d(8, 5, 7);

//      test the fourth triangle
        System.out.println("Площадь четвертого треугольника: " + computeArea(t8, t9, t10));

    }

    public static double computeArea(Point3d firstPoint, Point3d secondPoint, Point3d thirdPoint) {

//      checking for matching points
        if (firstPoint.isCompare(secondPoint) || firstPoint.isCompare(thirdPoint) || secondPoint.isCompare(thirdPoint)) {
            System.out.println(firstPoint.isCompare(secondPoint) & firstPoint.isCompare(thirdPoint) &
                    secondPoint.isCompare(thirdPoint) ? "Все три точки совпадают" :
                    firstPoint.isCompare(secondPoint) ? "Первая точка совпадает со второй" :
                            firstPoint.isCompare(thirdPoint) ? "Первая точка совпадает с третьей" : "Вторая точка совпадает с третьей");
            return 0.0;
        } else {

//          calculate the area of the triangle
            double a = firstPoint.distanceTo(secondPoint); // первая сторона треугольника
            double b = firstPoint.distanceTo(thirdPoint);  // вторая сторона треугольника
            double c = thirdPoint.distanceTo(secondPoint); // третья сторона треугольника


            double p = (a + b + c) / 2;
            double S = Math.sqrt(p * (p - a) * (p - b) * (p - c));

            String finalResult = String.format("%.2f", S);
            return Double.parseDouble(finalResult);

        }

    }
}
